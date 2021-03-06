package com.multimock.watcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.WatchEvent.Kind;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
public class DirectoryWatcher implements Watcher{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String watchPath;
    private Kind [] watchFor;
    private boolean watchRepeat;
    private boolean abort = false;
    private Function<Object, Object> handler;
    private Consumer<Object> resultHandler;

    public DirectoryWatcher(){
    }

    public DirectoryWatcher(String watchPath, Kind[] watchFor, boolean watchRepeat) {
        this.watchPath = watchPath;
        this.watchFor = watchFor;
        this.watchRepeat = watchRepeat;
    }

    public DirectoryWatcher(String watchPath, Kind[] watchFor, boolean watchRepeat, Function<Object, Object> handler, Consumer<Object> resultHandler) {
        this.watchPath = watchPath;
        this.watchFor = watchFor;
        this.watchRepeat = watchRepeat;
        this.handler = handler;
        this.resultHandler = resultHandler;
    }

    @Override
    public String getId() {
        return "mm-directory-watcher";
    }

    @Override
    public String getName() {
        return "Directory Watcher";
    }

    @Override
    public String getDescription() {
        return "Watches a directory for changes";
    }

    @Override
    public String getDocumentation() {
        return "tbd";
    }

    @Override
    public List<WatcherParameter> getParameters() {
        List<WatcherParameter> params = new ArrayList<>();
        params.add(new WatcherParameter("path", "string", "Defines the path to a directory that should be watched for changes.", true));
        params.add(new WatcherParameter("watchFor", "list string", "Defines the types of events this watcher should watch for.<br/>Possible values are create | modify | delete<br/>You must specify at least one of the possible values.<br/>Example: create,delete<br/>Default: create", false, Arrays.asList("create", "modify", "delete")));
        params.add(new WatcherParameter("repeat", "boolean", "Defines whether the watcher should continue watching the directory after the first event fired.<br/>false -> stop watcher after first event<br/>true -> watch until the watcher explicitly gets stopped<br/>Default: true", false, Arrays.asList(true, false)));
        return params;
    }

    public void setHandlers(Function<Object, Object> executionHandler, Consumer<Object> resultHandler) {
        this.handler = executionHandler;
        this.resultHandler = resultHandler;
    }

    @Override
    public Watcher create(List<WatcherParameter> params) { //}, Function<Object, Object> handler, Consumer<Object> resultCallback) {
        logger.debug("creating DirectoryWatcher instance...");
        parseParemtersFromInput(params);
        return new DirectoryWatcher(watchPath, watchFor, watchRepeat);
    }

    private void parseParemtersFromInput(List<WatcherParameter> params) {
        String path = "";
        Object [] watchers = null;
        boolean repeat = true;

        logger.trace("reading parameters from input...");
        // check if all parameters are provided - if not try to use defaults
        for (WatcherParameter p : params) {
            logger.trace("reading value for {}", p.getName());
            if (p.getName().equals("path")) {
                path = (String) p.getValue().orElse("");
            }
            if (p.getName().equals("watchFor")){
                String values = (String) p.getValue().orElse("create");
                values = values.replaceAll(" ", ""); // remove all whitespaces
                watchers = Arrays.stream(values.split(",")).map(v -> {
                    if (v.equals("create")) {
                         return StandardWatchEventKinds.ENTRY_CREATE;
                    }else if (v.equals("modify")) {
                        return StandardWatchEventKinds.ENTRY_MODIFY;
                    } else {
                        return StandardWatchEventKinds.ENTRY_DELETE;
                    }
                }).toArray();
            }
            if (p.getName().equals("repeat")){
                repeat = (boolean) p.getValue().orElse(true);
            }
        }
        logger.trace("finished reading parameters, found following values:");
        logger.trace("path: {}, watchers: {}, repeat: {}", path, watchers, repeat);
        if (StringUtils.isEmpty(path)) {
            // no path provided -> this is not allowed -> error
            logger.error("No path provided to parameter \"path\" - aborting!");
            throw new InvalidPathException("", "A path must be provided in order for this watcher to work!");
        }
        this.watchPath = path;
        this.watchFor = new Kind[watchers.length];
        for(int i = 0; i < watchers.length; i++){
            this.watchFor[i] = (Kind)watchers[i];
        }
        this.watchRepeat = repeat;
    }

    @Override
    public void stop() {
        logger.debug("aborting DirectoryWatcher...");
        this.abort = true;
    }

    @Override
    public void run() {
        logger.debug("starting DirectoryWatcher for {}", watchPath);
        try {
            WatchService ws = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(watchPath);
            path.register(ws, watchFor);

            WatchKey key;
            while ((key = ws.take()) != null && !abort) {
                // a modify event is being fired twice since there are two changes (file content and file attributes)
                // to avoid triggering the callback twice for each modify event, the process sleeps 50 msecs
                // this causes the file system watcher to group those two events to one with the count property set to 2
                // this results in a single execution of the callback instead of two
                Thread.sleep(50);
                for (WatchEvent<?> event : key.pollEvents()) {
                    logger.trace("received {} event for {}", event.kind().name(), watchPath);
                    // check if the event is one of those we want to watch for
                    if (Arrays.stream(watchFor).anyMatch(k -> k.name().equals(event.kind().name()))) {
                        String combined = Paths.get(watchPath, event.context() + "").toAbsolutePath().toString();
                        logger.debug("event {} is one of the trigger events -> trigger callback for Watcher", combined);
                        Object processResult = handler.apply(combined);
                        resultHandler.accept(processResult);
                    }
                }
                if (watchRepeat && !abort) {
                    logger.trace("waiting for the next directory event...");
                    key.reset();
                }
            }
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                logger.debug("DirectoryWatcher for {} was aborted", watchPath);
            } else {
                logger.error("error while watching directory {}", watchPath);
                logger.error("error: ", e);
            }
        }
    }
}
