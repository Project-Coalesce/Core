package com.coalesce.config.yml;

import com.coalesce.config.IConfig;
import com.coalesce.config.IEntry;
import com.coalesce.config.ISection;
import com.coalesce.plugin.CoPlugin;

import java.util.HashSet;
import java.util.Set;

public final class YamlSection implements ISection {
	
	private final String path;
	private final CoPlugin plugin;
	private final YamlConfig config;
	
	public YamlSection(String path, IConfig config, CoPlugin plugin) {
		this.path = path;
		this.config = (YamlConfig)config;
		this.plugin = plugin;
	}
	
	@Override
	public Set<String> getKeys(boolean deep) {
		return config.getBase().getConfigurationSection(path).getKeys(deep);
	}
	
	@Override
	public Set<IEntry> getEntries(boolean deep) {
		Set<IEntry> entries = new HashSet<>();
		getKeys(deep).forEach(key -> entries.add(config.getEntry(key)));
		return entries;
	}
	
	@Override
	public ISection getSection(String path) {
		return new YamlSection(path, config, plugin);
	}
	
	@Override
	public boolean contains(String path) {
		return config.getBase().contains(path);
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
}
