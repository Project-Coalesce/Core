package com.coalesce;

import com.coalesce.plugin.CoPlugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;

import static org.bukkit.plugin.ServicePriority.Normal;

public class Core extends CoPlugin {
	
	private static Core instance;
	private Method fileMethod;

	@Override
	public void onPluginEnable() {
        try{
            fileMethod = JavaPlugin.class.getDeclaredMethod("getFile");
            fileMethod.setAccessible(true);
        } catch (Exception ex){
            ex.printStackTrace();
        }

		getServer().getServicesManager().register(Core.class, this, this, Normal);
		updateCheck("sk89q", "CommandBook", true); //This is an example, this will be changed back once we get the updater working
	}

    public Method getFileMethod() {
        return fileMethod;
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
