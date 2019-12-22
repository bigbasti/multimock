package com.multimock.service;

public class ProcessInstance {
    private String handle;
    private Thread thread;
    private Object content;

    public ProcessInstance() {
    }

    public ProcessInstance(String handle, Thread thread, Object content) {
        this.handle = handle;
        this.thread = thread;
        this.content = content;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ProcessInstance{" +
                "handle='" + handle + '\'' +
                ", thread=" + thread +
                ", content=" + content +
                '}';
    }
}
