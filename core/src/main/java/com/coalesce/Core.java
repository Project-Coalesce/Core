package com.coalesce;

import com.coalesce.plugin.CoPlugin;

import static org.bukkit.plugin.ServicePriority.Normal;

public class Core extends CoPlugin {
	
	private static Core instance;

	@Override
	public void onPluginEnable() {

		getServer().getServicesManager().register(Core.class, this, this, Normal);

		//TODO: Remove this
		updateCheck("Project-Coalesce", "Core", true); //This is an example, this will be changed back once we get the updater working
	}

    @Override
	public void onPluginDisable() {
		instance = null;
	}
	
	/**
	 * Grabs the instance of the core.
	 * Make sure you don't call this before nor after {@link #onPluginDisable()}.
	 *
	 * @return The core instance.
	 */
	public static Core getInstance() {
		return instance;
	}
	
	private <M> M checkCoreEnabled(M instance) {
		if (!isEnabled()) throw new IllegalStateException("Core plugin is not enabled");
		return instance;
	}
}
