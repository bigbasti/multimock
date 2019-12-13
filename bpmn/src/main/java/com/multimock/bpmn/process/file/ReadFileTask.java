package com.multimock.bpmn.process.file;

import com.multimock.bpmn.process.BaseTask;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReadFileTask extends BaseTask {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void executeTask(DelegateExecution delegateExecution) {
        Path path = Paths.get(input);
        File file = path.toFile();
        if (file.exists()) {
            if (file.canRead()) {
                try {
                    String content = new String(Files.readAllBytes(path));
                    delegateExecution.setVariable("output", content);
                    return;
                } catch (IOException e) {
                    logger.error("error while reading from file", e);
                    addError("error while reading from file " + e.getMessage());
                }
            } else {
                logger.error("can not read the file - permissions?");
                addError("can not read the file - permissions? " + input);
            }


        } else {
            logger.error("file does not exist {}", input);
            addError("file does not exist " + input);
        }


        delegateExecution.setVariable("output", "");
    }
}
