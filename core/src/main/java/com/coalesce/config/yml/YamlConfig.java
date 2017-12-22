package com.coalesce.config.yml;

import com.coalesce.config.ConfigFormat;
import com.coalesce.config.IConfig;
import com.coalesce.config.IEntry;
import com.coalesce.config.ISection;
import com.coalesce.config.common.Section;
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
import java.util.List;

public abstract class YamlConfig implements IConfig {

    private Collection<IEntry> entries = new ArrayList<>();
    private final ConfigFormat format;
    private YamlConfiguration config;
    private final CoPlugin plugin;
    private final File dir, file;
    private final String name;

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
        if (!plugin.getConfigurations().contains(this)) { //Registers this configuration
            plugin.getConfigurations().add(this);
        }
        getBase().getKeys(true).forEach(entry -> { //Adds entries to the entries collection.
            if (getEntry(entry) == null) {
                entries.add(new YamlEntry(this, entry, getBase().get(entry)));
            }
        });
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
    public boolean contains(String path, boolean exact) {
        if (exact) {
            return getEntry(path) == null;
        }
        for (IEntry entry : entries) {
            if (entry.getPath().startsWith(path)) {
                return true;
            }
        }
        return false;
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
    public void setEntry(String path, Object value) {
        IEntry entry = new YamlEntry(this, path, value);
        if (getEntry(path) == null) {
            setValue(entry.getPath(), entry.getValue());
            entries.add(entry);
            return;
        }
        entries.remove(entry);
        entry = getEntry(path).setValue(value);
        entries.add(entry);
    }

    @Override
    public void addEntry(String path, Object value) {
        IEntry entry;
        if (getBase().get(path) == null) {
            entry = new YamlEntry(this, path, value);
            setValue(entry.getPath(), entry.getValue());
        } else {
            entry = new YamlEntry(this, path, getBase().get(path));
        }
        entries.add(entry);
    }

    @Override
    public void removeEntry(IEntry entry) {
        entry.remove();
    }

    @Override
    public void removeEntry(String path) {
        getEntry(path).remove();
    }

    @Override
    public IConfig getConfig() {
        return this;
    }

    @Override
    public void clear() {
        entries.forEach(IEntry::remove);
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
        }
        catch (IOException e) {
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
    public CoPlugin getPlugin() {
        return plugin;
    }

    @Override
    public ISection getSection(String path) {
        return new Section(path, this, plugin);
    }

    @Override
    public List<String> getStringList(String path) {
        return (List<String>)getList(path);
    }

    /**
     * The base YAML file.
     *
     * @return YamlConfiguration from the Bukkit API
     */
    public YamlConfiguration getBase() {
        return config;
    }

    private void setValue(String path, Object value) {
        getBase().set(path, value);
        try {
            getBase().save(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
