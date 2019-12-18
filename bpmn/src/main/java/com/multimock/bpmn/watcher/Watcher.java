package com.multimock.bpmn.watcher;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface Watcher {
    String getId();
    String getName();
    String getDescription();
    String getDocumentation();

    List<WatcherParameter> getParameters();

    Watcher create(List<WatcherParameter> params);
    Watcher create(List<WatcherParameter> params, String handle);
    void start(Consumer<Object> callback);
    void stop();
}
