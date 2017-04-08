package com.coalesce.type;

import com.coalesce.plugin.CoPlugin;

/**
 * Represents an object that requires an instance of {@link CoPlugin}
 */
public interface PluginDependant {

	/**
	 * The plugin associated with this
	 *
	 * @return The plugin
	 */
	CoPlugin getPlugin();

}
