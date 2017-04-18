package com.coalesce.type;

import com.coalesce.Core;
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
	default @NotNull CoPlugin getPlugin() {
		Core plugin = Core.getInstance();
		if (plugin == null) throw new IllegalStateException("Core instance is null, cannot be retrieved");

		return plugin;
	}

}
