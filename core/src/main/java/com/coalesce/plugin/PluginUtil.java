package com.coalesce.plugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Event;
import org.bukkit.plugin.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

public class PluginUtil {

	/**
	 * Loads and enables a plugin.
	 *
	 * @param file The plugin file
	 * @return Whether the plugin was successfully loaded
	 */
	public static Plugin load(File file) {

		Plugin target = null;

		if (!file.getName().endsWith(".jar")){
			return null;
		}

		try {
			target = Bukkit.getPluginManager().loadPlugin(file);

		} catch (InvalidDescriptionException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidPluginException e) {
			e.printStackTrace();
			return null;
		}

		target.onLoad();
		Bukkit.getPluginManager().enablePlugin(target);

		return target;
	}

	/**
	 * Unload a plugin.
	 *
	 * @param plugin the plugin to unload
	 * @return the message to send to the user.
	 */
	public static void unload(Plugin plugin) throws NoSuchFieldException, IllegalAccessException, IOException {

		String name = plugin.getName();

		PluginManager pluginManager = Bukkit.getPluginManager();

		SimpleCommandMap commandMap = null;

		List<Plugin> plugins = null;

		Map<String, Plugin> names = null;
		Map<String, Command> commands = null;
		Map<Event, SortedSet<RegisteredListener>> listeners = null;

		boolean reloadlisteners = true;

		if (pluginManager != null) {

			pluginManager.disablePlugin(plugin);

			Field pluginsField = Bukkit.getPluginManager().getClass().getDeclaredField("plugins");
			pluginsField.setAccessible(true);
			plugins = (List<Plugin>) pluginsField.get(pluginManager);

			Field lookupNamesField = Bukkit.getPluginManager().getClass().getDeclaredField("lookupNames");
			lookupNamesField.setAccessible(true);
			names = (Map<String, Plugin>) lookupNamesField.get(pluginManager);

			try {
				Field listenersField = Bukkit.getPluginManager().getClass().getDeclaredField("listeners");
				listenersField.setAccessible(true);
				listeners = (Map<Event, SortedSet<RegisteredListener>>) listenersField.get(pluginManager);
			} catch (Exception e) {
				reloadlisteners = false;
			}

			Field commandMapField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
			commandMapField.setAccessible(true);
			commandMap = (SimpleCommandMap) commandMapField.get(pluginManager);

			Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
			knownCommandsField.setAccessible(true);
			commands = (Map<String, Command>) knownCommandsField.get(commandMap);

		}

		pluginManager.disablePlugin(plugin);

		if (plugins != null && plugins.contains(plugin))
			plugins.remove(plugin);

		if (names != null && names.containsKey(name))
			names.remove(name);

		if (listeners != null && reloadlisteners) {
			for (SortedSet<RegisteredListener> set : listeners.values()) {
				for (Iterator<RegisteredListener> it = set.iterator(); it.hasNext(); ) {
					RegisteredListener value = it.next();
					if (value.getPlugin() == plugin) {
						it.remove();
					}
				}
			}
		}

		if (commandMap != null) {
			for (Iterator<Map.Entry<String, Command>> it = commands.entrySet().iterator(); it.hasNext(); ) {
				Map.Entry<String, Command> entry = it.next();
				if (entry.getValue() instanceof PluginCommand) {
					PluginCommand c = (PluginCommand) entry.getValue();
					if (c.getPlugin() == plugin) {
						c.unregister(commandMap);
						it.remove();
					}
				}
			}
		}

		// Attempt to close the classloader to unlock any handles on the plugin's jar file.
		ClassLoader cl = plugin.getClass().getClassLoader();

		if (cl instanceof URLClassLoader) {


			Field pluginField = cl.getClass().getDeclaredField("plugin");
			pluginField.setAccessible(true);
			pluginField.set(cl, null);

			Field pluginInitField = cl.getClass().getDeclaredField("pluginInit");
			pluginInitField.setAccessible(true);
			pluginInitField.set(cl, null);


			((URLClassLoader) cl).close();

		}

		// Will not work on processes started with the -XX:+DisableExplicitGC flag, but lets try it anyway.
		// This tries to get around the issue where Windows refuses to unlock jar files that were previously loaded into the JVM.
		System.gc();
	}

}
