package com.coalesce.type;

import org.bukkit.entity.Player;

import java.util.function.Consumer;

public interface ServerEssentials extends PluginDependant {


	default void forEachOnline(Consumer<Player> block) {
		getPlugin().getServer().getOnlinePlayers().forEach(block);
	}


}
