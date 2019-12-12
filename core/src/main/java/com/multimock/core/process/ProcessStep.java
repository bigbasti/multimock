package com.multimock.core.process;

import java.util.Map;

/**
 * describes a single task inside a more complex mock process
 */
public interface ProcessStep {
    String getName();
    String getClassName();
    String getProcessGroup();       // what type of functionality is offered? file|text|xml...
    String getDescription();        // a short description of the offered functionality
    String getDocumentation();      // detailed description
    String getIcon();               // font awesome icon to display as a symbol
    String getInputType();          // what is expected as input
    String getOutputType();         // what will be produced if everything goes well
    Map<String, String> getParameters();    // relevant parameters for the process

    void execute(Map<Object, Object> context);
 }
