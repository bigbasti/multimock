package com.multimock.bpmn.controller;

import com.multimock.bpmn.service.ProcessService;
import com.multimock.bpmn.watcher.DirectoryWatcher;
import com.multimock.bpmn.watcher.Watcher;
import com.multimock.bpmn.watcher.WatcherParameter;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Controller
@RequestMapping(path = "/api/bpmn")
public class HomeController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private DirectoryWatcher dw;

    @Autowired
    private ProcessService processService;

    @GetMapping("/searchfile")
    public @ResponseBody
    ResponseEntity getUserByLogin() {
        logger.debug("registering directory watcher...");
//        variables.put("initial", "C:/TEMP/jawshtml.html");

        String processResult = "temp";
        AtomicInteger counter = new AtomicInteger();
        Consumer<Object> handler = filePath -> {
            counter.getAndIncrement();
            logger.debug("starting new process readFileProcess for file {}", filePath);
            Map<String, Object> variables = new HashMap<>();
            variables.put("initial", filePath);
            variables.put("ERROR", new ArrayList<String>());

            ProcessInstanceWithVariables instance = runtimeService.createProcessInstanceByKey("readFileProcess")
                    .setVariables(variables)
                    .executeWithVariablesInReturn();
            while (!instance.isEnded()){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            logger.debug("PROCESS RESULT: {}", instance.getVariables().get("output"));
            if (counter.get() >= 3) {
                logger.debug("canceling watcher for {}...", Thread.currentThread().getName());
                processService.stopProcess(Thread.currentThread().getName());
            }
        };

        Watcher dirCreateWatcher = dw.create(Arrays.asList(
                new WatcherParameter("path", "C:/TEMP"),
                new WatcherParameter("repeat", true),
                new WatcherParameter("watchFor", "create")), handler);
        Watcher dirModifyWatcher = dw.create(Arrays.asList(
                new WatcherParameter("path", "C:/TEMP"),
                new WatcherParameter("repeat", true),
                new WatcherParameter("watchFor", "modify")), handler);

        processService.startWatcherAsync(dirCreateWatcher);
        processService.startWatcherAsync(dirModifyWatcher);

        return ResponseEntity.ok(processResult);
    }
}
