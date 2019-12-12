package com.multimock.bpmn.process;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindByRegExTask implements JavaDelegate {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String input = (String) delegateExecution.getVariable("input");
        String regex = (String) delegateExecution.getVariable("regex");

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        if (m.find()) {
            String hit = m.group(1);
            delegateExecution.setVariable("output", hit);
            logger.debug("found matching string {}", hit);
        } else {
            logger.warn("no matching string found!");
            delegateExecution.setVariable("output", null);
        }
        delegateExecution.getVariableNames().forEach( n -> logger.debug("{} -> {}", n, delegateExecution.getVariable(n)));
    }
}
