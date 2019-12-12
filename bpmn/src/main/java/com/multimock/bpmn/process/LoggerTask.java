package com.multimock.bpmn.process;

import org.apache.logging.log4j.util.Strings;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerTask implements JavaDelegate {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String input = (String) delegateExecution.getVariable("input");

        if(Strings.isEmpty(input)) {
            logger.warn("result was: no match found!");
            delegateExecution.setVariable("errorMsg", "sorry could not find anything!");
        } else {
            logger.debug("successfully found match: {}", input);
        }
    }
}
