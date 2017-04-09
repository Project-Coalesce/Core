package com.coalesce;

import com.coalesce.user.UserModule;
import com.coalesce.plugin.CoPlugin;
import org.jetbrains.annotations.Nullable;

import static org.bukkit.plugin.ServicePriority.Normal;

public final class Core extends CoPlugin {
    
    //The core instance.
	private static Core instance;


	private UserModule userModule;


	@Override
	public boolean onPreEnable() {
		instance = this;
		return super.onPreEnable();
	}

	@Override
	public void onPluginEnable() {
		getServer().getServicesManager().register(Core.class, this, this, Normal);

		addModules(userModule = new UserModule(this));
	}

	@Override
	public void onPluginDisable() {
		instance = null;
		userModule = null;
	}


	public @Nullable UserModule getUserModule() {
		return userModule;
	}
    
    
    /**
     * Grabs the instance of the core.
     * @return The core instance.
     */
    public static @Nullable Core getInstance() {
		return instance;
	}

}
