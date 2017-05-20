package com.coalesce.config.yml;

import com.coalesce.config.ConfigFormat;
import com.coalesce.config.IConfig;
import com.coalesce.config.IEntry;
import com.coalesce.plugin.CoPlugin;
import com.google.common.io.Files;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public abstract class YamlConfig implements IConfig {
	
	private Collection<IEntry> entries = new ArrayList<>();
	private YamlConfiguration config;
	private final File dir, file;
	private final String name;
	private final ConfigFormat format;
	private final CoPlugin plugin;
	
	protected YamlConfig(String name, CoPlugin plugin) {
		this.name = name;
		this.plugin = plugin;
		this.format = ConfigFormat.YAML;
		
		//Creating the correct directory and generating the file.
		if (!name.contains(File.separator)) {
			this.dir = plugin.getDataFolder();
			this.file = new File(dir.getAbsolutePath() + File.separator + name + ".yml");
		} else {
			int last = name.lastIndexOf(File.separator);
			String fileName = name.substring(last + 1);
			String path = name.substring(0, last);
			this.dir = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + path);
			this.file = new File(dir + File.separator + fileName + ".yml");
		}
		if (!dir.exists()) {
			dir.mkdirs();
		}
		try {
			file.createNewFile();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		//Loading the configuration.
		this.config = YamlConfiguration.loadConfiguration(file);
		if (!plugin.getConfigurations().contains(this)) {
			plugin.getConfigurations().add(this);
		}
	}
	
	@Override
	public IEntry getEntry(String path) {
		for (IEntry entry : entries) {
			if (entry.getPath().matches(path)) {
				return entry;
			}
		}
		return null;
	}
	
	@Override
	public Collection<IEntry> getEntryFromValue(Object value) {
		Collection<IEntry> found = new ArrayList<>();
		entries.forEach(entry -> {
			if (entry.getValue().equals(value)) {
				found.add(entry);
			}
		});
		return found;
	}
	
	@Override
	public Collection<IEntry> getEntries() {
		getBase().getKeys(true).forEach(entry -> {
			if (getEntry(entry) == null) {
				entries.add(new YamlEntry(this, entry, getBase().get(entry)));
			}
		});
		return entries;
	}
	
	@Override
	public void addReplace(String path, Object value) {
		IEntry entry = new YamlEntry(this, path, value);
		setValue(entry.getPath(), entry.getValue());
		entries.add(entry);
	}
	
	@Override
	public void addEntry(String path, Object value) {
		IEntry entry;
		if (getBase().get(path) == null) {
			entry = new YamlEntry(this, path, value);
			setValue(entry.getPath(), entry.getValue());
		}
		else entry = new YamlEntry(this, path, getBase().get(path));
		entries.add(entry);
	}
	
	@Override
	public void removeEntry(IEntry entry) {
		entry.remove();
	}
	
	@Override
	public IConfig getConfig() {
		return this;
	}
	
	@Override
	public void clear() {
		entries.forEach(e -> e.remove());
	}
	
	@Override
	public void backup() {
		DateFormat format = new SimpleDateFormat("yyyy.dd.MM-hh.mm.ss");
		File file = new File(dir + File.separator + "backups");
		File bckp = new File(file + File.separator + name + format.format(new Date()) + ".yml");
		if (!dir.exists() || bckp.exists() || !this.file.exists()) {
			return;
		}
		if (!file.exists()) {
			file.mkdir();
		}
		try {
			Files.copy(this.file, bckp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void delete() {
		file.delete();
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public File getFile() {
		return file;
	}
	
	@Override
	public ConfigFormat getFormat() {
		return format;
	}
	
	@Override
	public <E extends CoPlugin> E getPlugin() {
		return (E) plugin;
	}
	
	/**
	 * The base YAML file.
	 * @return YamlConfiguration from the Bukkit API
	 */
	public YamlConfiguration getBase() {
		return config;
	}
	
	private void setValue(String path, Object value) {
		getBase().set(path, value);
		try {
			getBase().save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
