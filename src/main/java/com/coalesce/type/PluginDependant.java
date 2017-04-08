package com.coalesce.type;

import com.coalesce.plugin.CoPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an object that requires an instance of {@link CoPlugin}
 */
public interface PluginDependant {

	/**
	 * The plugin associated with this
	 *
	 * @return The plugin
	 */
	@NotNull CoPlugin getPlugin();

}
