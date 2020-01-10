package com.company;

public class NamedJSONString implements INamedJsonObject {
    private String obj;
    public NamedJSONString(String obj) {
        if (obj == null || obj.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.obj = obj;
    }
    @Override
    public String getName() {
        return obj;
    }

    @Override
    public String toString() {
        return String.format("\"%s\"", obj);
    }
}
