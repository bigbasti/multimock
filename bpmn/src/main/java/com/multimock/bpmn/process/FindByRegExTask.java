package com.multimock.bpmn.process;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * search a text using a regex pattern
 * --> input  : text which should be searched
 * --> regex  : regular expression to match against
 * --> all    : (true|false:default) if true -> find all matches TODO
 * <-- output : the first positive match | null (no match) | list of matches (if all = true)
 */
public class FindByRegExTask extends BaseTask {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void executeTask(DelegateExecution exec) {
        String regex = (String) exec.getVariable("regex");

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        if (m.find()) {
            String hit = m.group(1);
            exec.setVariable("output", hit);
            logger.debug("found matching string {}", hit);
        } else {
            logger.warn("no matching string found!");
            exec.setVariable("output", null);
        }
        exec.getVariableNames().forEach( n -> logger.debug("{} -> {}", n, exec.getVariable(n)));
    }
}
