package com.coalesce.type;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface ServerEssentials extends PluginDependant {


	default void forEachOnline(@NotNull Consumer<Player> block) {
		getPlugin().getServer().getOnlinePlayers().forEach(block);
	}


}
