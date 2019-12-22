package com.multimock.controller;

import com.multimock.service.ProcessService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(path = "/api/process")
public class ProcessInformationController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private HistoryService historyService;

    @GetMapping("/running")
    public @ResponseBody
    ResponseEntity getRunningProcesses() {
        return ResponseEntity.ok(processService.getRunningProcesses());
    }

    @GetMapping("/history")
    public @ResponseBody
    ResponseEntity getProcessHistory() {
        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
        List<HistoricProcessInstance> finishedProcesesses = query.finished().list();
        return ResponseEntity.ok(finishedProcesesses);
    }
}
