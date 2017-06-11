package com.coalesce.config.yml;

import com.coalesce.config.IConfig;
import com.coalesce.config.IEntry;
import com.coalesce.config.ISection;
import com.coalesce.plugin.CoPlugin;

import java.util.HashSet;
import java.util.Set;

public final class YamlSection implements ISection {
	
	private final String path;
	private final IConfig config;
	private final CoPlugin plugin;
	
	public YamlSection(String path, IConfig config, CoPlugin plugin) {
		this.path = path;
		this.config = config;
		this.plugin = plugin;
	}
	
	@Override
	public Set<String> getKeys(boolean deep) {
		Set<String> keys = new HashSet<>();
		if (deep) {
			config.getEntries().stream().filter(e -> e.getPath().startsWith(path + ".")).forEach(e -> keys.add(e.getPath()));
		}
		else config.getEntries().stream().filter(e -> e.getPath().startsWith(path+".")).forEach(e -> {
			String key = e.getPath().substring(path.length() + 1);
			int size = key.indexOf(".");
			if (size < 0) size = key.length();
			keys.add(key.substring(0, size));
		});
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
		return new YamlSection(path, config, plugin);
	}
	
	@Override
	public boolean contains(String path) {
		return config.getEntries().stream().filter(e -> e.getPath().startsWith(this.path + ".")).anyMatch(e -> e.getPath().equalsIgnoreCase(path));
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
}
