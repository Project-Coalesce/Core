package com.coalesce.command;

import com.coalesce.plugin.CoPlugin;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CommandBuilder {

	private CoCommand command;

	public CommandBuilder(CoPlugin plugin, String name){
		command = new CoCommand(plugin, name);
	}

	public CommandBuilder executor(CommandExecutor executor){
		command.setCommandExecutor(executor);
		return this;
	}

	public CommandBuilder aliases(String... aliases){
		command.setAliases(aliases);
		return this;
	}

	public CommandBuilder aliases(Set<String> aliases){
		command.setAliases(aliases);
		return this;
	}

	public CommandBuilder description(String description){
		command.setDescription(description);
		return this;
	}

	public CommandBuilder usage(String usage){
		command.setUsage(usage);
		return this;
	}

	public CommandBuilder permission(String permission){
		command.setPermission(permission);
		return this;
	}

	public CommandBuilder minArgs(int minArgs){
		command.setMinArgs(minArgs);
		return this;
	}

	public CommandBuilder maxArgs(int maxArgs){
		command.setMaxArgs(maxArgs);
		return this;
	}

	public CommandBuilder playerOnly(){
		command.setPlayerOnly(true);
		return this;
	}

	public CommandBuilder children(CoCommand... subCommands){
		command.setChildren(Stream.of(subCommands).collect(Collectors.toSet()));
		return this;
	}

	public CommandBuilder children(Set<CoCommand> subCommands){
		command.setChildren(subCommands);
		return this;
	}

	public CoCommand build(){
		return command;
	}
}
