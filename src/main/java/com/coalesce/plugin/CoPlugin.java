package com.coalesce.plugin;

import com.coalesce.type.Logging;
import com.coalesce.type.ServerEssentials;
import com.coalesce.type.Switch;
import com.google.common.collect.ImmutableList;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public abstract class CoPlugin extends JavaPlugin implements Logging, ServerEssentials, Listener {

	private final List<CoModule> modules = new LinkedList<>();

	private boolean doEnable = true;


	@Override
	public final void onLoad() {
		doEnable = onPreEnable();
	}

	@Override
	public final void onEnable() {
		if (!doEnable) return;

		onPluginEnable();
		enableModules();

	}

	@Override
	public final void onDisable() {
		disableModules();
		onPluginDisable();
	}

	@Override
	public final CoPlugin getPlugin() {
		return this;
	}


	public boolean onPreEnable() {
		return true;
	}

	public abstract void onPluginEnable();

	public abstract void onPluginDisable();


	protected final void addModules(CoModule... modules) {
		Collections.addAll(this.modules, modules);
	}

	public final List<CoModule> getModules() {
		return ImmutableList.copyOf(modules);
	}

	public final <M extends CoModule> Optional<M> getModule(Class<M> clazz) {
		return getModules().stream().filter(coModule -> coModule.getClass().equals(clazz)).map(coModule -> ((M) coModule)).findAny();
	}


	public final void enableModules() {
		getModules().forEach(Switch::enable);
	}

	public final void disableModules() {
		getModules().forEach(Switch::disable);
	}


	public final void register(Listener listener) {
		getServer().getPluginManager().registerEvents(listener, this);
	}

	public final void unregister(Listener listener) {
		HandlerList.unregisterAll(listener);
	}


}
