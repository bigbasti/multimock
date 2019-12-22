package com.multimock.handler;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ProcessHandler implements Handler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String name;
    private String processKey;
    private RuntimeService engine;

    public ProcessHandler(String name, String processKey, RuntimeService engine) {
        this.name = name;
        this.processKey = processKey;
        this.engine = engine;
    }

    @Override
    public Function<Object, Object> getHandler() {
        Function<Object, Object> handler = filePath -> {
            logger.debug("starting new process readFileProcess for file {}", filePath);
            Map<String, Object> variables = new HashMap<>();
            variables.put("initial", filePath);
            variables.put("ERROR", new ArrayList<String>());

            ProcessInstanceWithVariables instance = engine.createProcessInstanceByKey("readFileProcess")
                    .setVariables(variables)
                    .executeWithVariablesInReturn();
            while (!instance.isEnded()){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Object result = instance.getVariables().get("output");
            logger.debug("PROCESS RESULT: {}", result);
            return result;
        };
        return handler;
    }
}
