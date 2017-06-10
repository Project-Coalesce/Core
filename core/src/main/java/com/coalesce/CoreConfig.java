package com.coalesce;

import com.coalesce.config.yml.YamlConfig;
import com.coalesce.plugin.CoPlugin;

public final class CoreConfig extends YamlConfig {
	
	protected CoreConfig(CoPlugin plugin) {
		super("config", plugin);
        addEntry("log-download-process", true);
	}
	
	/**
	 * Checks whether to log download process for plugin updates.
	 * @return True if download processes are logged, false otherwise.
	 */
	public boolean logDLProcess() {
		return getBoolean("log-download-process");
	}
	
}
