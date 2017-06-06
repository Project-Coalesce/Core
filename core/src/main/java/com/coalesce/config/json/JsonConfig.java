package com.coalesce.config.json;

import com.coalesce.config.ConfigFormat;
import com.coalesce.config.IConfig;
import com.coalesce.config.IEntry;
import com.coalesce.plugin.CoPlugin;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class JsonConfig implements IConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private Collection<IEntry> entries = new ArrayList<>();
	private final ConfigFormat format;
	private final CoPlugin plugin;
	private final File dir, file;
	private final String name;
	@Getter
	private JSONObject json;
	
	protected JsonConfig(String name, CoPlugin plugin) {
		this.name = name;
		this.format = ConfigFormat.JSON;
		this.plugin = plugin;

		dir = plugin.getDataFolder();
		file = new File(dir, name.replaceAll("/", String.valueOf(File.separatorChar)) + ".json");

		if (file.exists()) {
            try{
                FileReader reader = new FileReader(file);
                json = (JSONObject) new JSONParser().parse(reader);
                reader.close();
            } catch (Exception e) {
                plugin.getCoLogger().error("An error occured while attempting to read configuration file at " + file.getAbsolutePath() + ":");
                e.printStackTrace();
            }
        } else {
            json = new JSONObject();
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
	public String getString(String path) {
		return getEntry(path).getString();
	}
	
	@Override
	public double getDouble(String path) {
		return getEntry(path).getDouble();
	}
	
	@Override
	public int getInt(String path) {
		return getEntry(path).getInt();
	}
	
	@Override
	public long getLong(String path) {
		return getEntry(path).getLong();
	}
	
	@Override
	public boolean getBoolean(String path) {
		return getEntry(path).getBoolean();
	}
	
	@Override
	public List<?> getList(String path) {
		return getEntry(path).getList();
	}
	
	@Override
	public Object getValue(String path) {
		return getEntry(path).getValue();
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
        save();
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
        save();
	}

	@Override
	public void removeEntry(IEntry entry) {
		entry.remove();
        save();
	}
	
	@Override
	public void removeEntry(String path) {
		getEntry(path).remove();
        save();
	}
	
	@Override
	public IConfig getConfig() {
		return this;
	}
	
	@Override
	public void clear() {
		entries.forEach(e -> e.remove());
		json.clear();
		save();
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

	private void save() {
        try {
            synchronized (file) {
                if (file.exists()) file.delete();
                if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
                file.createNewFile();

                FileWriter writer = new FileWriter(file);

                try {
                    writer.write(GSON.toJson(json));
                } catch (Exception e) {
                    plugin.getCoLogger().error("An error occured while attempting to write saved data for configuration file at " + file.getAbsolutePath() + ":");
                    e.printStackTrace();
                }

                writer.close();
            }
        } catch (IOException e) {
            plugin.getCoLogger().error("An error occured while attempting to save configuration file at " + file.getAbsolutePath() + ":");
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
