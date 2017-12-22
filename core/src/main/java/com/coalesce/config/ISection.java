package com.coalesce.config;

import com.coalesce.plugin.CoPlugin;

import java.util.Set;

public interface ISection {

    /**
     * Gets a set of keys that is in this section.
     *
     * @param deep Whether to get the keys of the keys (and so on) in this section.
     * @return A Set of Keys.
     */
    Set<String> getKeys(boolean deep);

    /**
     * Gets all the entries that exist in this configuration section.
     *
     * @return A set of entries in this section.
     */
    Set<IEntry> getEntries();

    /**
     * Gets a section from within this section.
     *
     * @param path The path from the start of this section. <p>Note: dont use the string provided originally to get this current section</p>
     * @return A section of the configuration.
     */
    ISection getSection(String path);

    /**
     * Check whether a section contains a path.
     *
     * @param path The path to look for
     * @return True if the path exists, false otherwise.
     */
    boolean contains(String path);

    /**
     * Gets the current path of the configuration section.
     *
     * @return The current path
     */
    String getCurrentPath();

    /**
     * The name of the section.
     *
     * @return The name of the section.
     */
    String getName();

    /**
     * Gets the base configuration of this section.
     *
     * @return The current configuration
     */
    IConfig getConfig();

    /**
     * Gets the host plugin.
     *
     * @return The host plugin.
     */
    CoPlugin getPlugin();

    /**
     * Gets an entry from a section
     *
     * @param path The path to the entry. <p>Note: dont use the string provided originally to get this entry</p> //TODO: elaborate more and create getEntry Implementations in Json and yml config.
     */
    IEntry getEntry(String path);

    /**
     * Gets the parent section to the current section.
     *
     * @return THe previous section, returns null if no previous section exists.
     */
    ISection getParent();


}
