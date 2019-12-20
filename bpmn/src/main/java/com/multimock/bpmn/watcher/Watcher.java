package com.multimock.bpmn.watcher;

import java.util.List;
import java.util.function.Consumer;

public interface Watcher {
    String getId();
    String getName();
    String getDescription();
    String getDocumentation();

    List<WatcherParameter> getParameters();

    Watcher create(List<WatcherParameter> params);
    String start(Consumer<Object> callback);
    void stop();
}
