package com.coalesce.type;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * A collection of convenience methods
 */
public interface ServerEssentials extends PluginDependant {

	/**
	 * Run an action for all online players
	 *
	 * @param block The action
	 */
	default void forEachOnline(@NotNull Consumer<Player> block) {
		getPlugin().getServer().getOnlinePlayers().forEach(block);
	}

}
