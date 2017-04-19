package com.coalesce.plugin;

import com.coalesce.config.Config;

import java.io.File;

public class CoConfig extends Config {

	private static final CoConfig DEFAULT = new CoConfig("DEFAULT", 16);

	private String serverName;
	private int maxPlayers;

	protected CoConfig(String serverName, int maxPlayers) {
		this.serverName = serverName;
		this.maxPlayers = maxPlayers;
	}


	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}


	public static CoConfig load(CoPlugin plugin) {
		return load(DEFAULT, new File(plugin.getDataFolder(), "config.toml"));
	}

}
