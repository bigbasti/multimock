package com.multimock.mock;

import com.multimock.handler.Handler;
import com.multimock.service.ProcessService;
import com.multimock.watcher.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class ReactiveMock {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String name;
    private Watcher watcher;
    private Handler handler;
    private ProcessService processService;
    private boolean executed = false;

    public ReactiveMock(String name, Watcher watcher, Handler handler, ProcessService processService) {
        this.name = name;
        this.watcher = watcher;
        this.handler = handler;
        this.processService = processService;
        logger.trace("Mock {} created", name);
    }

    public void start() {
        if (executed) {
            logger.warn("this mock was already executed");
            return;
        }
        logger.debug("starting mock {}...", name);

        Consumer<Object> resultHandler = result -> {
            logger.warn("HANDLER RESULT: {}", result);
            // todo: call here the result action
        };

        logger.debug("setting handlers for mock");
        watcher.setHandlers(handler.getHandler(), resultHandler);

        logger.debug("executing watcher process...");
        processService.startWatcherAsync(watcher);
        this.executed = true;
    }
}
