package com.multimock.bpmn.process;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReadFileTask implements JavaDelegate {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.debug("executing task {}", this.getClass().getName());

        String input = (String) delegateExecution.getVariable("input");

        Path path = Paths.get(input);
        File file = path.toFile();
        if(!file.canRead()){
            logger.error("can not read the file - permissions?");
            return;
        }
        if(!file.exists()){
            logger.error("file does not exist {}", input);
            return;
        }

        try {
            String content = new String(Files.readAllBytes(path));
            delegateExecution.setVariable("output", content);
        } catch (IOException e) {
            logger.error("error while reading from file", e);
            return;
        }
    }
}
