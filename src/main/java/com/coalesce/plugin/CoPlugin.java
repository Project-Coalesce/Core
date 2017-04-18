package com.coalesce.plugin;

import com.coalesce.type.Logging;
import com.coalesce.type.ServerEssentials;
import com.coalesce.type.Switch;
import com.google.common.collect.ImmutableList;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static org.bukkit.ChatColor.DARK_RED;

public abstract class CoPlugin extends JavaPlugin implements Logging, ServerEssentials, Listener {

	private final List<CoModule> modules = new LinkedList<>();

	private boolean doEnable = true;
	private CoConfig coConfig;


	@Override
	public final void onLoad() {
		coConfig = CoConfig.load(this);
		doEnable = onPreEnable();
	}

	@Override
	public final void onEnable() {
		if (!doEnable) return;

		try {
			onPluginEnable();
		} catch (Exception e) {
			error(DARK_RED + "Failed to enable module " + getName());
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
			warn(DARK_RED + "Failed to disable module " + getName());
			e.printStackTrace();
		}
	}

	@Override
	public @NotNull CoPlugin getPlugin() {
		return this;
	}

	public boolean onPreEnable() {
		return true;
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
		getModules().forEach(Switch::enable);
	}

	public final void disableModules() {
		getModules().forEach(Switch::disable);
	}


	public final void register(@NotNull Listener listener) {
		getServer().getPluginManager().registerEvents(listener, this);
	}

	public final void unregister(@NotNull Listener listener) {
		HandlerList.unregisterAll(listener);
	}


}
