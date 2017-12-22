package com.coalesce.config;

import java.util.List;

public interface IEntry {

    /**
     * Gets a value from a config entry.
     *
     * @return A string from the specified path.
     */
    String getString();

    /**
     * Gets a value from a config entry.
     *
     * @return A double from the specified path.
     */
    double getDouble();

    /**
     * Gets a value from a config entry.
     *
     * @return An integer from the specified path.
     */
    int getInt();

    /**
     * Gets a value from a config entry.
     *
     * @return A long from the specified path.
     */
    long getLong();

    /**
     * Gets a value from a config entry.
     *
     * @return A boolean from the specified path.
     */
    boolean getBoolean();

    /**
     * Gets a value from a config entry.
     *
     * @return A list from the specified path.
     */
    List<?> getList();

    /**
     * Gets a string list from an entry.
     *
     * @return The String list.
     */
    List<String> getStringList();

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
     * @param newpath The new path this entry will have.
     */
    IEntry setPath(String newpath);

    /**
     * Sets the value of this entry.
     *
     * @param value The new value of this entry.
     */
    IEntry setValue(Object value);

    /**
     * Gets the database this entry is held in.
     *
     * @return The entry's database.
     */
    IConfig getConfig();

    /**
     * Gets the name of this entry.
     *
     * @return The entry name.
     */
    String getName();

    /**
     * Allows quick removal and moving of an Entry to another configuration file.
     *
     * @param config The new configuration file.
     * @return The new entry.
     */
    IEntry setConfig(IConfig config);

    /**
     * Removes the entry.
     */
    void remove();

}
