package com.coalesce.config;

import com.coalesce.plugin.CoPlugin;

import java.io.File;
import java.util.Collection;
import java.util.List;

public interface IConfig {
	/**
	 * Gets an entry from a config.
	 * @param path The path in the config.
	 * @return The entry that is at that path.
	 */
	IEntry getEntry(String path);
	
	/**
	 * Gets a value from a config entry.
	 * @param path The path in the configuration.
	 * @return A string from the specified path.
	 */
	String getString(String path);
	
	/**
	 * Gets a value from a config entry.
	 * @param path The path in the configuration.
	 * @return A double from the specified path.
	 */
	double getDouble(String path);
	
	/**
	 * Gets a value from a config entry.
	 * @param path The path in the configuration.
	 * @return An integer from the specified path.
	 */
	int getInt(String path);
	
	/**
	 * Gets a value from a config entry.
	 * @param path The path in the configuration.
	 * @return A long from the specified path.
	 */
	long getLong(String path);
	
	/**
	 * Gets a value from a config entry.
	 * @param path The path in the configuration.
	 * @return A boolean from the specified path.
	 */
	boolean getBoolean(String path);
	
	/**
	 * Gets a value from a config entry.
	 * @param path The path in the configuration.
	 * @return A list from the specified path.
	 */
	List<?> getList(String path);
	
	/**
	 * Gets a value from a config entry.
	 * @param path The path in the configuration.
	 * @return An object from the specified path.
	 */
	Object getValue(String path);
	
	/**
	 * Gets an entry based on its value instead of path.
	 *
	 * @param value The value you are looking for.
	 * @return The entry that was found with this value.
	 *
	 * NOTE: This will likely change into an array of entries.
	 */
	Collection<IEntry> getEntryFromValue(Object value);
	
	/**
	 * Gets all the entries in a config.
	 * @return A collection of entries in a config.
	 */
	Collection<IEntry> getEntries();
	
	/**
	 * Adds a new entry to the current config.
	 * @param entry The entry being added.
	 */
	void addEntry(IEntry entry);
	
	/**
	 * Removes an entry from the config.
	 * @param entry The entry to remove.
	 */
	void removeEntry(IEntry entry);
	
	/**
	 * Returns this config.
	 * @return This current config.
	 */
	IConfig getConfig();
	
	/**
	 * Clears all the entries in this configuration.
	 */
	void clear();
	
	/**
	 * Backs up this configuration.
	 */
	void backup();
	
	/**
	 * Deletes the configuration file.
	 */
	void delete();
	
	/**
	 * The name of the config
	 * @return The name of the current config.
	 */
	String getName();
	
	/**
	 * Gets the config file.
	 * @return The file of this config.
	 */
	File getFile();
	
	/**
	 * Identifies the format of this configuration.
	 * @return YAML or GSON
	 */
	ConfigFormat getFormat();
	
	/**
	 * Returns the plugin this configuration is for.
	 * @return The host plugin.
	 */
	<E extends CoPlugin> E getPlugin();
}
