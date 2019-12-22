package com.multimock.service;

import com.multimock.watcher.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ProcessService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<ProcessInstance> instances = new ArrayList<>();

    public ProcessService() {
    }

    public List<ProcessInstance> getRunningProcesses() {
        return instances
                .stream()
                .filter(p -> !p.getThread().isInterrupted())
                .collect(Collectors.toList());
    }

    public void startWatcherAsync(Watcher watcher) {
        String handle = "W-" + new Random().nextInt(10000000);
        logger.debug("starting async execution of Watcher thread {}", handle);
        ProcessInstance pi = new ProcessInstance();
        pi.setHandle(handle);
        pi.setContent(watcher);

        Thread t = new Thread(watcher);
        t.setName(handle);
        pi.setThread(t);
        instances.add(pi);

        t.start();
    }

    public void stopProcess(String handle) {
        logger.debug("trying to stop thread {}", handle);
        Optional<ProcessInstance> pi = instances.stream().filter(i -> i.getHandle().equals(handle) && !i.getThread().isInterrupted()).findFirst();
        pi.ifPresent(i -> {
            logger.debug("found ProcessInstance for {} - trying to stop it", handle);
            if (i.getContent() instanceof Watcher) {
                ((Watcher) i.getContent()).stop();
                i.getThread().interrupt();
                instances.remove(i);
            } else {
                logger.warn("could not determine type of ProcessInstance - unable to stop it");
            }
        });
    }

    public void stopAll() {
        logger.debug("stopping all running ProcessInstances...");
        instances.forEach(pi -> {
            if (!pi.getThread().isInterrupted()) {
                if (pi.getContent() instanceof Watcher) {
                    ((Watcher) pi.getContent()).stop();
                    pi.getThread().interrupt();
                    instances.remove(pi);
                }
            }
        });
    }
}
