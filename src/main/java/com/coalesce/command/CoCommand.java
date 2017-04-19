package com.coalesce.command;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CoCommand {

	private CommandExecutor commandExecutor;
	private String name;
	private Set<String> aliases;
	private String description;
	private String usage;
	private String permission;
	private int minArgs = -1;
	private int maxArgs = -1;
	private boolean playerOnly;
	private Set<CoCommand> children;

	public CoCommand(String name){
		this.name = name;

		//Defaults
		aliases = new HashSet<>();
		children = new HashSet<>();
	}

	public void execute(CommandContext context){

		//If there were no child commands that matched the context
		if (!checkChildren(context)){

		}

	}

	private boolean checkChildren(CommandContext context){
		if (children.isEmpty()) return false;
		if (!context.hasArgs()) return false;
		for (CoCommand childCommand : children){
			//This is safe because we know that there is at least one argument
			if (childCommand.matchesCommand(context.argAt(0))){

				childCommand.execute(context);
				return true;
			}
		}
		return false;
	}

	public CommandExecutor getCommandExecutor() {
		return commandExecutor;
	}

	public void setCommandExecutor(CommandExecutor commandExecutor) {
		this.commandExecutor = commandExecutor;
	}

	/**
	 * Checks if the String matches this commands name, or aliases
	 *
	 * @param input The string to compare it to
	 * @return if the string matches
	 */
	public boolean matchesCommand(String input){
		input = input.toLowerCase();
		return (input.equals(name) || aliases.contains(input));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getAliases() {
		return aliases;
	}

	public void setAliases(Set<String> aliases) {
		this.aliases = aliases;
	}

	public void setAliases(String... aliases){
		this.aliases = Stream.of(aliases).map(String::toLowerCase).collect(Collectors.toSet());
	}

	public boolean hasAliases(){
		return aliases.size() > 0;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public int getMinArgs() {
		return minArgs;
	}

	public void setMinArgs(int minArgs) {
		this.minArgs = minArgs;
	}

	public int getMaxArgs() {
		return maxArgs;
	}

	public void setMaxArgs(int maxArgs) {
		this.maxArgs = maxArgs;
	}

	public boolean isPlayerOnly() {
		return playerOnly;
	}

	public void setPlayerOnly(boolean playerOnly){
		this.playerOnly = playerOnly;
	}

	public Set<CoCommand> getChildren() {
		return children;
	}

	public void setChildren(Set<CoCommand> children) {
		this.children = children;
	}
}
