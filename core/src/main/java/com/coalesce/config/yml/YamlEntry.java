package com.coalesce.config.yml;

import com.coalesce.config.IConfig;
import com.coalesce.config.IEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class YamlEntry implements IEntry {

    private String key;
    private Object value;
    private final IConfig config;

    public YamlEntry(IConfig config, String key) {
        this.config = config;
        this.key = key;
        this.value = getConfig().getValue(key);
    }

    public YamlEntry(IConfig config, String key, Object value) {
        this.config = config;
        this.key = key;
        this.value = value;
    }

    @Override
    public String getString() {
        return (String)value;
    }

    @Override
    public double getDouble() {
        return (Double)value;
    }

    @Override
    public int getInt() {
        return (Integer)value;
    }

    @Override
    public long getLong() {
        return (Long)value;
    }

    @Override
    public boolean getBoolean() {
        return (Boolean)value;
    }

    @Override
    public List<?> getList() {
        //From Bukkit MemorySection#getList
        return (value instanceof List) ? (List<?>)value : null;
    }

    @Override
    public List<String> getStringList() {
        //From Bukkit MemorySection#getStringList
        if (getList() == null) {
            return new ArrayList<>(0);
        }

        List<String> result = new ArrayList<>();

        for (Object object : getList()) {
            if (object instanceof String) {
                result.add(String.valueOf(object));
            }
        }

        return result;
    }

    @Override
    public String getName() {
        if (getPath().contains(".")) {
            return getPath().substring(getPath().lastIndexOf(".") + 1);
        }
        return getPath();
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
    public IEntry setConfig(IConfig config) {
        return null;
    }

    @Override
    public void remove() {
        setValue(key, null);
    }

    private void setValue(String key, Object value) {
        YamlConfig cfg = ((YamlConfig)config);
        this.value = value;
        cfg.getBase().set(key, value);
        try {
            cfg.getBase().save(cfg.getFile());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
