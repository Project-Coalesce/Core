package com.coalesce;

import com.coalesce.player.PlayerModule;
import com.coalesce.plugin.CoPlugin;
import org.jetbrains.annotations.Nullable;

public final class Core extends CoPlugin {
    
    //The core instance.
	private static Core instance;


	private PlayerModule playerModule;


	@Override
	public boolean onPreEnable() {
		instance = this;
		return super.onPreEnable();
	}

	@Override
	public void onPluginEnable() {
		addModules(playerModule = new PlayerModule(this));
	}

	@Override
	public void onPluginDisable() {
		instance = null;
		playerModule = null;
	}


	public @Nullable PlayerModule getPlayerModule() {
		return playerModule;
	}
    
    
    /**
     * Grabs the instance of the core.
     * @return The core instance.
     */
    public static @Nullable Core getInstance() {
		return instance;
	}

}
