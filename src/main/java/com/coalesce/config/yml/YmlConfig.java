package com.coalesce.config.yml;

import com.coalesce.config.IConfig;
import com.coalesce.config.IEntry;
import com.coalesce.plugin.CoPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public final class YmlConfig implements IConfig {
	
	private Collection<IEntry> collection = new ArrayList<>();
	private YamlConfiguration config;
	private final File dir, file;
	private final String name;
	
	public YmlConfig(String name, CoPlugin plugin) {
		this.name = name;
		this.dir = new File("plugins" + File.separator + plugin.getName());
		this.file = new File(dir + name + ".yml");
		if (!dir.exists()) {
			dir.mkdir();
			try {
				file.createNewFile();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.config = YamlConfiguration.loadConfiguration(file);
	}
	
	@Override
	public IEntry getEntry(String path) {
		return null;
	}
	
	@Override
	public IEntry getEntryFromValue(Object value) {
		return null;
	}
	
	@Override
	public Collection<IEntry> getEntries() {
		return null;
	}
	
	@Override
	public void addEntry(IEntry entry) {
		
	}
	
	@Override
	public void removeEntry(IEntry entry) {
		
	}
	
	@Override
	public IConfig getConfig() {
		return null;
	}
	
	@Override
	public void clear() {
		
	}
	
	@Override
	public void backup() {
		
	}
	
	@Override
	public void delete() {
		
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public File getFile() {
		return file;
	}
}
