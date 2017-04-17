package com.coalesce.config;

public interface IEntry {
	
	/**
	 * Gets the path of this entry.
	 *
	 * @return The path in the config.
	 */
	String getPath();
	
	/**
	 * Gets the value of this entry.
	 *
	 * @return The value of this entry.
	 */
	Object getValue();
	
	/**
	 * Sets the path of this entry.
	 *
	 * @param newpath
	 *            The new path this entry will have.
	 */
	IEntry setPath(String newpath);
	
	/**
	 * Sets the value of this entry.
	 *
	 * @param value
	 *            The new value of this entry.
	 */
	IEntry setValue(Object value);
	
	/**
	 * Gets the database this entry is held in.
	 *
	 * @return The entry's database.
	 */
	IConfig getDatabase();
	
	/**
	 * Removes the entry.
	 */
	void remove();
	
}
