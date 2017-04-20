package com.coalesce.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.material.CocoaPlant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandRegister extends Command {
	
	private final CoCommand command;
	public static Set<CoCommand> commands;
	
	public CommandRegister(CoCommand command) {
		super(command.getName());
		this.command = command;
		this.description = command.getDescription();
		this.usageMessage = command.getUsage();
		List<String> aliases = new ArrayList<>();
		command.getAliases().forEach(alias -> aliases.add(alias));
		this.setAliases(aliases);
		commands.add(command);
	}
	
	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		for (CoCommand command : commands){
			if (command.matchesCommand(args[0])){
				command.execute(new CommandContext(sender, Arrays.asList(args)));
				return true;
			}
		}
		return false;
	}
}
