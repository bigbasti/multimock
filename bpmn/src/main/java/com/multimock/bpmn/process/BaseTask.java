package com.multimock.bpmn.process;

import com.multimock.bpmn.exception.TaskExecutionException;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTask implements JavaDelegate {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    abstract protected void executeTask(DelegateExecution exe);

    private List<String> errors = new ArrayList<>();
    protected String input = "";

    @Override
    public void execute(DelegateExecution exec) {
        String currentActivity = exec.getCurrentActivityName();
        String currentActivityId = exec.getCurrentActivityId();
        logger.debug("[# Executing Task]: {} - [{}]", currentActivity, currentActivityId);

        errors = (List<String>) exec.getVariable("ERROR");
        input = (String) exec.getVariable("input");

        try {
            executeTask(exec);
        } catch (TaskExecutionException ex) {
            addError(currentActivity + ": " + ex.getMessage());
            logger.error("Exception while executing task {}", currentActivity);
        }

        exec.setVariable("ERROR", errors);
    }

    protected void addError(String message) {
        errors.add(message);
    }
    public List<String> getErrors() {
        return errors;
    }
}
