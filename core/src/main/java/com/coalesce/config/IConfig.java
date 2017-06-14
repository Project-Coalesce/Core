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
	 * Gets a List of Strings from a path in this config.
	 * @param path The path to get the strings from.
	 * @return A list from the specified path.
	 */
	List<String> getStringList(String path);
	
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
	 * @param path The path in the config.
	 * @param value The value to set this entry to.
	 */
	void addEntry(String path, Object value);
	
	/**
	 * Adds a new entry to the current config. If the
	 * config already has a value at the path location
	 * it will be updated with the new value supplied
	 * from this method.
	 *
	 * @param path The path in the config.
	 * @param value The value to set the path to.
	 */
	void setEntry(String path, Object value);
	
	/**
	 * Removes an entry from the config via the Entry Object.
	 * @param entry The entry to remove.
	 */
	void removeEntry(IEntry entry);
	
	/**
	 * Removes an entry from the config via the entry path.
	 * @param path The path to this entry.
	 */
	void removeEntry(String path);
	
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
	 * Gets a section of a configuration from a path.
	 * @param path The path to start getting the section from.
	 * @return A Section of the config.
	 */
	ISection getSection(String path);
	
	/**
	 * Checks whether the configuration contains a path.
	 * @param path The path to look for.
	 * @param exact True to look for the exact path, false to find a path that starts with the path provided.
	 *
	 *           <p>
	 *              Ex. contains("groups", false);<p>
	 *
	 *              ==Configuration== <br>
	 *              groups.test<br>
	 *              groups.test.another-test<p>
	 *
	 *              ==Results== <br>
	 *              contains would return true because one or more of those paths contains the groups section.
	 *           </p>
	 * @return
	 */
	boolean contains(String path, boolean exact);
	
	/**
	 * Returns the plugin this configuration is for.
	 * @return The host plugin.
	 */
	<E extends CoPlugin> E getPlugin();
}
