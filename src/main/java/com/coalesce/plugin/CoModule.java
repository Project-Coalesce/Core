package com.coalesce.plugin;

import com.coalesce.type.Logging;
import com.coalesce.type.ServerEssentials;
import com.coalesce.type.Switch;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.ChatColor.DARK_RED;

/**
 * Base class for sub "plugins", allows for modular servers
 */
public abstract class CoModule extends JavaPlugin {

	private final CoPlugin plugin;

	private final String name;
	private boolean isEnabled;

	/**
	 * Create a new module
	 *
	 * @param plugin The plugin that's creating this module
	 * @param name The name of this module
	 */
	public CoModule(@NotNull CoPlugin plugin, @NotNull String name) {
		this.plugin = plugin;
		this.name = name;
	}

	/**
	 * Get the {@link CoPlugin} that created this module
	 *
	 * @return The {@link CoPlugin}
	 */
	@Override
	public @NotNull CoPlugin getPlugin() {
		return plugin;
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

	@Override
	public @NotNull String getName() {
		return name;
	}

	@Override
	public void enable() {
		if (isEnabled) throw new IllegalStateException("Module " + getName() + " is already enabled");

		try {
			onEnable();
			getPlugin().register(this);
		}
		catch (Exception e) {
			error(DARK_RED + "Failed to enable module " + getName());
			e.printStackTrace();
			return;
		}

		isEnabled = true;
	}

	@Override
	public void disable() {
		if (!isEnabled) throw new IllegalStateException("Module " + getName() + " isn't enabled");

		isEnabled = false;
		try {
			onDisable();
			getPlugin().unregister(this);
		}
		catch (Exception e) {
			warn(DARK_RED + "Failed to disable module " + getName());
			e.printStackTrace();
		}
	}


	/**
	 * Called when this module is successfully enabled
	 */
	protected abstract void onEnable() throws Exception;

	/**
	 * Called when this module is successfully disabled
	 */
	protected abstract void onDisable() throws Exception;

}
