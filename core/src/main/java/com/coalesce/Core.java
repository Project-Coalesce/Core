package com.coalesce;

import com.coalesce.plugin.CoPlugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

import static org.bukkit.plugin.ServicePriority.Normal;

public class Core extends CoPlugin {
	
	private static Core instance;
	private Field fileField;

	@Override
	public void onPluginEnable() {
	    try{
            fileField = JavaPlugin.class.getField("file");
            fileField.setAccessible(true);
        } catch (Exception ex) {
	        ex.printStackTrace();
        }

		getServer().getServicesManager().register(Core.class, this, this, Normal);
		updateCheck("Project-Coalesce", "Core", true);
	}

    public Field getFileField() {

	    return fileField;

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
