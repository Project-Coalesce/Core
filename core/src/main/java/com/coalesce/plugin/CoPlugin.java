package com.coalesce.plugin;

import com.coalesce.chat.CoFormatter;
import com.coalesce.command.CoCommand;
import com.coalesce.command.CommandRegister;
import com.coalesce.config.IConfig;
import com.coalesce.config.yml.YmlConfig;
import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;

import static org.bukkit.ChatColor.DARK_RED;

public abstract class CoPlugin extends JavaPlugin implements Listener {

	public String displayName;
	private final List<CoModule> modules = new LinkedList<>();
	private Collection<IConfig> configs = new ArrayList<>();
	private Set<CoCommand> commands = new HashSet<>();
	
	private CoLogger logger;
	private CoFormatter formatter;
	
	@Override
	public final void onEnable() {

		//Setup basic things
		logger = new CoLogger(this);
		formatter = new CoFormatter(this);

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

	@Override
    public final void onLoad() {

	    try {
            onPluginLoad();
        } catch (Exception e) {
            logger.error(DARK_RED + "Failed to load module " + getName());
            e.printStackTrace();
            return;
        }

    }

	public void onPluginEnable() throws Exception {}
	public void onPluginDisable() throws Exception {}
    public void onPluginLoad() throws Exception {}

	public String getDisplayName(){
		return displayName;
	}

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

	public final void registerListener(@NotNull Listener listener) {
		getServer().getPluginManager().registerEvents(listener, this);
	}

	public final void registerListeners(Listener... listeners) {
	    Arrays.asList(listeners).forEach(this::registerListener);
    }

	public final void unregisterListener(@NotNull Listener listener) {
		HandlerList.unregisterAll(listener);
	}

	public final CoLogger getCoLogger() {
		return logger;
	}

	public final CoFormatter getFormatter(){
		return formatter;
	}

	
	/**
	 * A collection of all the current configurations of a plugin.
	 * @return A configuration file list.
	 */
	public final Collection<IConfig> getConfigurations() {
		return configs;
	}
	
	/**
	 * Looks for a config of any type. (If the config being looked up does not exist, it defaults to creating it in yml)
	 * @param name The name of the config you're looking for.
	 * @return The config.
	 */
	public final IConfig getConfig(String name) {
		for (IConfig config : configs) {
			if (config.getName().equals(name)) {
				return config;
			}
		}
		return null;
	}
	
	public final void addCommand(CoCommand command) {
		CommandMap map = null;
		Field field;
		
		try {
			field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			field.setAccessible(true);
			map = (CommandMap)field.get(Bukkit.getServer());
		}
		catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
		commands.add(command);
		map.register(getDisplayName(), new CommandRegister(command, this));
	}
	
	public final Set<CoCommand> getCommands() {
		return commands;
	}
}
