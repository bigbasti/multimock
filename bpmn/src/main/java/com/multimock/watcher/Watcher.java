package com.multimock.watcher;

import java.util.List;
import java.util.function.Consumer;

public interface Watcher extends Runnable  {
    String getId();
    String getName();
    String getDescription();
    String getDocumentation();

    List<WatcherParameter> getParameters();

    Watcher create(List<WatcherParameter> params, Consumer<Object> callback);
    void run();
    void stop();
}
