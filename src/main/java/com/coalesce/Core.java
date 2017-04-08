package com.coalesce;

import com.coalesce.player.PlayerModule;
import com.coalesce.plugin.CoPlugin;

public final class Core extends CoPlugin {

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


	public PlayerModule getPlayerModule() {
		return playerModule;
	}


	public static Core getInstance() {
		return instance;
	}

}
