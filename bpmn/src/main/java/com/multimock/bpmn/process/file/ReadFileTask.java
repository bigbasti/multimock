package com.multimock.bpmn.process.file;

import com.multimock.bpmn.exception.TaskExecutionException;
import com.multimock.bpmn.process.BaseTask;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Read the content of a file into a process variable
 * --> input  : path to the file which should be read
 * <-- output : the contents of the read file
 */
public class ReadFileTask extends BaseTask {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void executeTask(DelegateExecution exec) {
        exec.setVariable("output", "");

        Path path = Paths.get(input);
        File file = path.toFile();
        if (file.exists()) {
            if (file.canRead()) {
                try {
                    String content = new String(Files.readAllBytes(path));
                    exec.setVariable("output", content);
                    return;
                } catch (IOException e) {
                    logger.error("error while reading from file", e);
                    throw new TaskExecutionException("error while reading from file" + e.getMessage());
                }
            } else {
                throw new TaskExecutionException("can not read the file - permissions? " + input);
            }
        } else {
            throw new TaskExecutionException("file does not exist " + input);
        }
    }
}
