package com.multimock.bpmn.process;

import org.apache.logging.log4j.util.Strings;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerTask extends BaseTask {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void executeTask(DelegateExecution delegateExecution) {
        if(Strings.isEmpty(input)) {
            logger.warn("result was: no match found!");
            delegateExecution.setVariable("errorMsg", "sorry could not find anything!");
        } else {
            logger.debug("successfully found match: {}", input);
        }
    }
}
