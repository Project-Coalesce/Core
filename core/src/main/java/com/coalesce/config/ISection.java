package com.coalesce.config;

import java.util.Set;

public interface ISection {
	
	/**
	 * Gets a set of keys that is in this section.
	 * @param deep Whether to get the keys of the keys (and so on) in this section.
	 *
	 * @return A Set of Keys.
	 */
	Set<String> getKeys(boolean deep);
	
	/**
	 * Gets all the entries that exist in this configuration section.
	 * @param deep Whether to get all the entries in this section.
	 *
	 * @return
	 */
	Set<IEntry> getEntries(boolean deep);
	
	/**
	 * Gets a section of a configuration.
	 * @param path The path to start getting the section from.
	 *
	 * @return A section of the configuration.
	 */
	ISection getSection(String path);
	
	/**
	 * Check whether a section contains a path.
	 * @param path The path to look for
	 * @return True if the path exists, false otherwise.
	 */
	boolean contains(String path);
	
	/**
	 * Gets the current path of the configuration section.
	 * @return The current path
	 */
	String getCurrentPath();
	
	/**
	 * The name of the section.
	 * @return The name of the section.
	 */
	String getName();
	
	/**
	 * Gets the base configuration of this section.
	 * @return The current configuration
	 */
	IConfig getConfig();

}
