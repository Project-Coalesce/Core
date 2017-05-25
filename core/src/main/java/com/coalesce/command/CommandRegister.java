package com.coalesce.command;

import com.coalesce.command.base.AbstractCommandContext;
import com.coalesce.command.base.AbstractTabContext;
import com.coalesce.command.tabcomplete.TabContext;
import com.coalesce.plugin.CoPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public final class CommandRegister extends Command implements PluginIdentifiableCommand {
	
	private final CoCommand command;
	private final CoPlugin plugin;
	
	/**
	 * Registers a command for the given plugin.
	 * @param command The CoCommand to register.
	 * @param plugin The plugin to register the command for.
	 */
	public CommandRegister(CoCommand command, CoPlugin plugin) {
		super(command.getName());
		this.plugin = plugin;
		this.command = command;
		this.description = command.getDescription();
		this.usageMessage = command.getUsage();
		List<String> aliases = new ArrayList<>();
		command.getAliases().forEach(alias -> aliases.add(alias));
		this.setAliases(aliases);
	}
	
	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (command.matchesCommand(commandLabel)) {
			try {
				command.execute(command.getCommandContext().getDeclaredConstructor(CommandSender.class, String[].class, CoPlugin.class).newInstance(sender, args, plugin));
			}
			catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
	
	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		try {
			
			//The Default or custom tab context
			Constructor<? extends AbstractTabContext> tabContext = command.getTabContext().getDeclaredConstructor(CoPlugin.class, CoCommand.class, AbstractCommandContext.class);
			System.out.println(tabContext.getName());
			//The default or custom command context
			Constructor<? extends AbstractCommandContext> commandContext = command.getCommandContext().getDeclaredConstructor(CommandSender.class, String[].class, CoPlugin.class);
			System.out.println(commandContext.getName());
			
			return command.completer(tabContext.newInstance(plugin, command, commandContext.newInstance(sender, args, plugin)));
		}
		catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Plugin getPlugin() {
		return plugin;
	}
}
