package com.coalesce.config.common;

import com.coalesce.config.IConfig;
import com.coalesce.config.IEntry;
import com.coalesce.config.ISection;
import com.coalesce.plugin.CoPlugin;

import java.util.HashSet;
import java.util.Set;

public final class Section implements ISection {

    private final String path;
    private final IConfig config;
    private final CoPlugin plugin;

    public Section(String path, IConfig config, CoPlugin plugin) {
        this.path = path;
        this.config = config;
        this.plugin = plugin;
    }

    @Override
    public Set<String> getKeys(boolean deep) {
        Set<String> keys = new HashSet<>();
        if (deep) {
            config.getEntries().stream().filter(e -> e.getPath().startsWith(path + ".")).forEach(e -> keys.add(e.getPath()));
        } else {
            config.getEntries().stream().filter(e -> e.getPath().startsWith(path + ".")).forEach(e -> {
                String key = e.getPath().substring(path.length() + 1);
                int size = key.indexOf(".");
                if (size < 0) {
                    size = key.length();
                }
                keys.add(key.substring(0, size));
            });
        }
        return keys;
    }

    @Override
    public Set<IEntry> getEntries() {
        Set<IEntry> entries = new HashSet<>();
        config.getEntries().stream().filter(e -> e.getPath().startsWith(path + ".")).forEach(entries::add);
        return entries;
    }

    @Override
    public ISection getSection(String path) {
        return new Section(this.path + "." + path, config, plugin);
    }

    @Override
    public boolean contains(String path) {
        return getKeys(false).contains(path);
    }

    @Override
    public String getCurrentPath() {
        return path;
    }

    @Override
    public String getName() {
        return path.substring(path.lastIndexOf("."));
    }

    @Override
    public IConfig getConfig() {
        return config;
    }

    @Override
    public CoPlugin getPlugin() {
        return plugin;
    }

    @Override
    public IEntry getEntry(String path) {
        return config.getEntry(this.path + "." + path);
    }

    @Override
    public ISection getParent() {
        if (!path.contains(".")) {
            return null;
        }
        return new Section(path.substring(0, path.lastIndexOf(".")), config, plugin);
    }
}
