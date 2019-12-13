package com.multimock.bpmn.process;

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
    public void execute(DelegateExecution delegateExecution) {
        String currentActivity = delegateExecution.getCurrentActivityName();
        String currentActivityId = delegateExecution.getCurrentActivityId();
        logger.debug("[# Executing Task]: {} - [{}]", currentActivity, currentActivityId);

        errors = (List<String>) delegateExecution.getVariable("ERROR");
        input = (String) delegateExecution.getVariable("input");

        try {
            executeTask(delegateExecution);
        } catch (Exception ex) {
            addError(currentActivity + ": " + ex.getMessage());
            logger.error("Exception while executing task {}", currentActivity);
        }
    }

    protected void addError(String message) {
        errors.add(message);
    }
    public List<String> getErrors() {
        return errors;
    }
}
