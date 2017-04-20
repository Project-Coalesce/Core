package com.coalesce.command;

import com.coalesce.plugin.CoPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandRegister extends Command implements PluginIdentifiableCommand {
	
	private final CoCommand command;
	private final CoPlugin plugin;
	
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
		for (CoCommand command : plugin.getCommands()){
			command.execute(new CommandContext(sender, Arrays.asList(args)));
			return true;
		}
		return false;
	}
	
	@Override
	public Plugin getPlugin() {
		return plugin;
	}
}
