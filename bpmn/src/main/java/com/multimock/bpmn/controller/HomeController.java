package com.multimock.bpmn.controller;

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
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path = "/api/bpmn")
public class HomeController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RuntimeService runtimeService;

    @GetMapping("/searchfile")
    public @ResponseBody
    ResponseEntity getUserByLogin() {
        logger.debug("executing process search file {}...", "C:\\TEMP\\demofile1.txt");
        Map<String, Object> variables = new HashMap<>();
        variables.put("initial", "C:/TEMP/demofile.txt");
        variables.put("ERROR", new ArrayList<String>());
//        variables.put("initial", "C:/TEMP/jawshtml.html");

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
        return ResponseEntity.ok(instance.getVariables().get("output"));
    }
}
