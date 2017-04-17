package com.coalesce.plugin;

import com.google.common.collect.ImmutableList;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static org.bukkit.ChatColor.DARK_RED;

public abstract class CoPlugin extends JavaPlugin implements Listener {

	private final List<CoModule> modules = new LinkedList<>();
	private CoLogger logger;

	@Override
	public final void onEnable() {

		//Setup basic things
		logger = new CoLogger(this);

		//Try to call the onEnable
		try {
			onPluginEnable();
		} catch (Exception e) {
			logger.error(DARK_RED + "Failed to enable module " + getName());
			e.printStackTrace();
			return;
		}

		enableModules();
	}

	@Override
	public final void onDisable() {
		disableModules();
		try {
			onPluginDisable();
		} catch (Exception e) {
			logger.warn(DARK_RED + "Failed to disable module " + getName());
			e.printStackTrace();
		}
	}


	public abstract void onPluginEnable() throws Exception;

	public abstract void onPluginDisable() throws Exception;


	protected final void addModules(CoModule... modules) {
		Collections.addAll(this.modules, modules);
	}


	public final @NotNull List<CoModule> getModules() {
		return ImmutableList.copyOf(modules);
	}

	public final <M extends CoModule> @NotNull Optional<M> getModule(Class<M> clazz) {
		Iterator<M> iterator = getModules().stream().filter(coModule -> coModule.getClass().equals(clazz)).map(coModule -> ((M) coModule)).iterator();
		return Optional.ofNullable(iterator.hasNext() ? iterator.next() : null);
	}


	public final void enableModules() {
		getModules().forEach(CoModule::enable);
	}

	public final void disableModules() {
		getModules().forEach(CoModule::disable);
	}

	public final void register(@NotNull Listener listener) {
		getServer().getPluginManager().registerEvents(listener, this);
	}

	public final void unregister(@NotNull Listener listener) {
		HandlerList.unregisterAll(listener);
	}

	public final CoLogger getCoLogger(){
		return logger;
	}

}
