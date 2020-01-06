package com.multimock.bpmn.process.file;

import com.multimock.bpmn.exception.TaskExecutionException;
import com.multimock.bpmn.process.BaseTask;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * write the contents of a process variable into a file (overwrite if exists)
 * --> input  : the content to write into a file
 * --> path   : the path to write the content to
 * <-- output : no output (empty string)
 */
public class WriteFileTask extends BaseTask {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String path = "";

    @Override
    protected void executeTask(DelegateExecution exe) {
        path = (String) exe.getVariable("path");
        if (StringUtils.isEmpty(path)) {
            logger.error("no target path provided - please provide a valid path variable to this task");
            throw new TaskExecutionException("No target path provided to task");
        }
        if(StringUtils.isEmpty(input)){
            logger.warn("no content was provided - content will be set to EMPTY");
            input = "";
        }

        Path filePath = Paths.get(path);
        File file = filePath.toFile();
        if (file.canWrite()) {
            try {
                if (file.exists()) {
                    logger.debug("file {} already exists, deleting it first", path);
                    boolean deleteSuccessful = file.delete();
                    logger.debug("deleting file successful: {}", deleteSuccessful);
                }
                OpenOption[] options = new OpenOption[] { WRITE, CREATE_NEW };
                Files.write(filePath, input.getBytes(), options);
            } catch (IOException e) {
                logger.error("error while writing file", e);
                throw new TaskExecutionException("error while writing file" + e.getMessage());
            }
        } else {
            logger.error("can not write the file - permissions?");
            throw new TaskExecutionException("can not write to file - missing permissions?");
        }

        exe.setVariable("output", "");
    }
}
