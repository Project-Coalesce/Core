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

	private final File dir, file;
	private final String name;
	private final ConfigFormat format;
	private final CoPlugin plugin;
	@Getter
    private JSONObject json;

	protected JsonConfig(String name, CoPlugin plugin) {
		this.name = name;
		this.dir = plugin.getDataFolder();
		this.file = new File(dir + File.separator + name + ".json");
		this.format = ConfigFormat.JSON;
		this.plugin = plugin;

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

		if (!plugin.getConfigurations().contains(this)) {
			plugin.getConfigurations().add(this);
		}

        try{
            FileReader reader = new FileReader(file);
            json = (JSONObject) new JSONParser().parse(reader);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	@Override
	public IEntry getEntry(String path) {

	    if(!json.containsKey(path)) return null;
	    return new JsonEntry(this, path, json.get(path));

	}
	
	@Override
	public IEntry getEntryFromValue(Object value) {
//TODO This

	    return null;
	}
	
	@Override
	public Collection<IEntry> getEntries() {

	    List<IEntry> entries = new ArrayList<>();
	    json.forEach((k, v) -> entries.add(new JsonEntry(this, (String) k, v)));

		return entries;
	}
	
	@Override
	public void addEntry(IEntry entry) {

		json.put(entry.getPath(), entry.getValue());

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
