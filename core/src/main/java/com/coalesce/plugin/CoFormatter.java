package com.coalesce.plugin;

import static org.bukkit.ChatColor.*;

public class CoFormatter {

	private CoPlugin plugin;
	private String prefix;

	public CoFormatter(CoPlugin plugin){
		this.plugin = plugin;
		this.prefix = GRAY + "[" + plugin.getDisplayName() + GRAY + "]" + RESET;
	}

	public String format(String message){
		return prefix + " " + message;
	}

}
