package com.coalesce.config.json;

import com.coalesce.config.ConfigFormat;
import com.coalesce.config.IConfig;
import com.coalesce.config.IEntry;
import com.coalesce.plugin.CoPlugin;
import com.google.common.io.Files;
import lombok.Getter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class JsonConfig implements IConfig {

	private Collection<IEntry> entries;
	private final File dir, file;
	private final String name;
	private final ConfigFormat format;
	private final CoPlugin plugin;
	@Getter
    private JSONObject json;

	protected JsonConfig(String name, CoPlugin plugin) {
		this.name = name;
		this.format = ConfigFormat.JSON;
		this.plugin = plugin;
		
		//Creating the correct directory and generating the file.
		if (!name.contains(File.separator)) {
			this.dir = plugin.getDataFolder();
			this.file = new File(dir.getAbsolutePath() + File.separator + name + ".json");
		} else {
			int last = name.lastIndexOf(File.separator);
			String fileName = name.substring(last + 1);
			String path = name.substring(0, last);
			this.dir = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + path);
			this.file = new File(dir + File.separator + fileName + ".json");
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
        try{
            FileReader reader = new FileReader(file);
            json = (JSONObject) new JSONParser().parse(reader);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
	    json.forEach((k, v) -> entries.add(new JsonEntry(this, (String) k, v)));
		return entries;
	}
	
	@Override
	public void setEntry(String path, Object value) {
		IEntry entry = new JsonEntry(this, path, value);
		if (getEntry(path) == null) {
			json.put(entry.getPath(), entry.getValue());
			entries.add(entry);
			return;
		}
		entries.remove(entry);
		entry = entry.setValue(value);
		entries.add(entry);
	}
	
	@Override
	public void addEntry(String path, Object value) { //This needs to be updated to match Yaml's functionality.
		IEntry entry;
		if (!json.containsKey(path)) {
			entry = new JsonEntry(this, path, value);
			json.put(entry.getPath(), entry.getValue());
		}
		else entry = new JsonEntry(this, path, json.get(path));
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
	    json.clear();
	}
	
	@Override
	public void backup() {
		DateFormat format = new SimpleDateFormat("yyyy.dd.MM-hh.mm.ss");
		File file = new File(dir + File.separator + "backups");
		File bckp = new File(file + File.separator + name + format.format(new Date()) + ".json");
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

}
