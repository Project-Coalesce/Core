package com.coalesce.config.json;

import com.coalesce.config.IConfig;
import com.coalesce.config.IEntry;

public class JsonEntry implements IEntry {

    private String key;
    private Object value;
    private final IConfig config;

    public JsonEntry(IConfig config, String key) {
        this.config = config;
        this.key = key;
    }

    public JsonEntry(IConfig config, String key, Object value) {
        this.config = config;
        this.key = key;
        this.value = value;
    }

    @Override
    public String getPath() {
        return key;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public IEntry setPath(String newpath) {
        remove();
        setValue(newpath, value);
        return this;
    }

    @Override
    public IEntry setValue(Object value) {
        setValue(key, value);
        return this;
    }

    @Override
    public IConfig getConfig() {
        return config;
    }

    @Override
    public void remove() {
        setValue(key, null);
    }

    private void setValue(String key, Object value) {
        JsonConfig jsonConfig = (JsonConfig) config;
        jsonConfig.getJson().put(key, value); //If anyone could remove the annoying bitching from intellij, that would be great.
    }

}
