package com.multimock.bpmn.watcher;

import java.util.List;
import java.util.Optional;

public class WatcherParameter {
    private String name;
    private String type;
    private String docs;
    private Optional<Object> value;
    private boolean mandatory;
    private List<Object> possibleValues;

    public WatcherParameter() {
    }

    public WatcherParameter(String name, Object value) {
        this.name = name;
        this.value = Optional.of(value);
    }

    public WatcherParameter(String name, String type, String docs, boolean mandatory) {
        this.name = name;
        this.type = type;
        this.docs = docs;
        this.mandatory = mandatory;
    }
    public WatcherParameter(String name, String type, String docs, boolean mandatory, List<Object> possibleValues) {
        this.name = name;
        this.type = type;
        this.docs = docs;
        this.mandatory = mandatory;
        this.possibleValues = possibleValues;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDocs() {
        return docs;
    }

    public void setDocs(String docs) {
        this.docs = docs;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public Optional<Object> getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = Optional.of(value);
    }

    public List<Object> getPossibleValues() {
        return possibleValues;
    }

    public void setPossibleValues(List<Object> possibleValues) {
        this.possibleValues = possibleValues;
    }

    @Override
    public String toString() {
        return "WatcherParameter{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", docs='" + docs + '\'' +
                ", value=" + value.orElse("") +
                ", mandatory=" + mandatory +
                '}';
    }
}
