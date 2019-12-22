package com.multimock.controller;

import com.multimock.handler.ProcessHandler;
import com.multimock.mock.ReactiveMock;
import com.multimock.service.ProcessService;
import com.multimock.watcher.DirectoryWatcher;
import com.multimock.watcher.Watcher;
import com.multimock.watcher.WatcherParameter;
import org.camunda.bpm.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

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
//
    @GetMapping("/searchfile2")
    public @ResponseBody
    ResponseEntity createWatcher() {
        logger.debug("registering directory watcher...");

        String processResult = "temp";
        ProcessHandler processHandler = new ProcessHandler("Handles file action inside a directory", "readFileProcess", runtimeService);

        Watcher dirCreateWatcher = dw.create(Arrays.asList(
                new WatcherParameter("path", "C:/TEMP"),
                new WatcherParameter("repeat", true),
                new WatcherParameter("watchFor", "create")));
        Watcher dirModifyWatcher = dw.create(Arrays.asList(
                new WatcherParameter("path", "C:/TEMP"),
                new WatcherParameter("repeat", true),
                new WatcherParameter("watchFor", "modify")));

        ReactiveMock mock = new ReactiveMock("Check modified files for regex", dirModifyWatcher, processHandler, processService);
        mock.start();
        ReactiveMock mock2 = new ReactiveMock("Check new files for regex", dirCreateWatcher, processHandler, processService);
        mock2.start();

        return ResponseEntity.ok(processResult);
    }
}
