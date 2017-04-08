package com.coalesce.player;

import com.coalesce.plugin.CoModule;
import com.coalesce.plugin.CoPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Handles players and their user accounts
 */
public final class PlayerModule extends CoModule {

	public PlayerModule(CoPlugin plugin) {
		super(plugin, "Players");
	}


	@Override
	protected void onEnable() throws Exception {
		forEachOnline(this::loadUser);
	}

	@Override
	protected void onDisable() throws Exception {
		forEachOnline(this::unloadUser);
	}


	private void loadUser(final Player player) {
		// TODO: 4/8/2017 Load the user...
	}

	private void unloadUser(final Player player) {
		// TODO: 4/8/2017 Unload the user...
	}


	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent e) {
		e.setJoinMessage("");

		loadUser(e.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent e) {
		e.setQuitMessage("");

		unloadUser(e.getPlayer());
	}

}
