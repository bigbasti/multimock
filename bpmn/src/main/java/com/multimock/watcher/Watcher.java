package com.multimock.watcher;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public interface Watcher extends Runnable  {
    String getId();
    String getName();
    String getDescription();
    String getDocumentation();

    List<WatcherParameter> getParameters();

//    Watcher create(List<WatcherParameter> params, Function<Object, Object> callback, Consumer<Object> resultCallback);
    Watcher create(List<WatcherParameter> params);
    void setHandlers(Function<Object, Object> executionHandler, Consumer<Object> resultHandler);
    void run();
    void stop();
}
