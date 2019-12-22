package com.multimock.handler;

import java.util.function.Function;

public interface Handler {
    Function<Object, Object> getHandler();
}
