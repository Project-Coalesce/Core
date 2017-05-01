package com.coalesce.command;

import com.coalesce.plugin.CoPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CoCommand {

	private CoPlugin plugin;
	private CommandExecutor commandExecutor;
	private String name;
	private Set<String> aliases;
	private String description;
	private String usage;
	private String permission;
	private Predicate<CommandSender> permissionCheck;
	private boolean requiresOperator = false;
	private int minArgs = -1;
	private int maxArgs = -1;
	private boolean playerOnly = false;
	private Set<CoCommand> children;

	public CoCommand(CoPlugin plugin, String name){
		this.plugin = plugin;
		this.name = name;

		//Defaults
		aliases = new HashSet<>();
		children = new HashSet<>();
	}
	
	/**
	 * The main execute method for a CoCommand.
	 * @param context The command context.
	 */
	public void execute(CommandContext context){

		//If there were no child commands that matched the context
		if (!checkChildren(context)){

			//Check for too many args
			if (context.getArgs().size() > maxArgs && maxArgs > -1){
				context.send(plugin.getFormatter().format(ChatColor.RED + "Incorrect usage! Too many arguments!"));
				return;
			}

			//Check for not enough args
			if (context.getArgs().size() < minArgs){
				context.send(plugin.getFormatter().format(ChatColor.RED + "Incorrect usage! Not enough arguments!"));
				return;
			}

			//Check if the sender has perms
			if (permission != null && !context.getSender().hasPermission(permission)){
				context.send(plugin.getFormatter().format(ChatColor.RED + "You do not have permission for this command!"));
				return;
			}
            if (requiresOperator && !context.getSender().isOp()) {
                context.send(plugin.getFormatter().format(ChatColor.RED + "You do not have permission for this command!"));
                return;
            }
            if (permissionCheck != null && !permissionCheck.test(context.getSender())) {
                context.send(plugin.getFormatter().format(ChatColor.RED + "You do not have permission for this command!"));
                return;
            }

			//Check if console is trying to use a player only command
			if (context.isConsole() && playerOnly){
				context.send(plugin.getFormatter().format(ChatColor.RED + "This command can only be used by players!"));
				return;
			}

			//Everything seems okay, so lets execute the command
			commandExecutor.execute(context);
		}
	}

	/**
	 * This method checks if any children match the command context.
	 * If one is found, the child is executed.
	 *
	 * @param context The command context
	 * @return True if a child was found, false otherwise
	 */
	private boolean checkChildren(CommandContext context){
		if (children.isEmpty()) return false;
		if (!context.hasArgs()) return false;
		for (CoCommand childCommand : children){
			//This is safe because we know that there is at least one argument
			if (childCommand.matchesCommand(context.argAt(0))){

				context.getArgs().remove(0);
				childCommand.execute(context);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the command Executor for this command.
	 * @return
	 */
	public CommandExecutor getCommandExecutor() {
		return commandExecutor;
	}
	
	/**
	 * Sets the executor of this command.
	 * @param commandExecutor
	 */
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
	
	/**
	 * Returns the plugin this command belongs too.
	 * @return The host plugin
	 */
	public CoPlugin getPlugin(){
		return plugin;
	}
	
	/**
	 * The name of this command
	 * @return The name of this command.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of this command.
	 * @param name The new name of this command.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * A set of aliases for this command.
	 * @return The alias set for this command.
	 */
	public Set<String> getAliases() {
		return aliases;
	}
	
	/**
	 * Sets the aliases for this command.
	 * @param aliases All the aliases for this command.
	 */
	public void setAliases(Set<String> aliases) {
		this.aliases = aliases;
	}
	
	/**
	 * Sets the aliases for this command.
	 * @param aliases All the aliases for this command.
	 */
	public void setAliases(String... aliases){
		this.aliases = Stream.of(aliases).map(String::toLowerCase).collect(Collectors.toSet());
	}
	
	/**
	 * Checks if this command has any aliases whatsoever.
	 * @return True if it has aliases, false otherwise.
	 */
	public boolean hasAliases(){
		return aliases.size() > 0;
	}
	
	/**
	 * Gets the description of this command.
	 * @return The command description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the description of this command.
	 * @param description The description of this command.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Gets the usage of this command.
	 * @return The command usage.
	 */
	public String getUsage() {
		return usage;
	}
	
	/**
	 * Sets the usage of this command.
	 * @param usage The command usage of this command.
	 */
	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getPermission() {
		return permission;
	}
	
	/**
	 * Sets the permission needed for this command
	 * @param permission The permission needed for this command.
	 */
	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	/**
	 * Checks if this command needs operator to be ran.
	 * @param requiresOperator True for operators only, false otherwise.
	 */
	public void setRequiresOperator(boolean requiresOperator) {
		this.requiresOperator = requiresOperator;
	}
	
	/**
	 * Sets the permission check of the command.
	 * @param permissionCheck The permission check.
	 */
	public void setPermissionCheck(Predicate<CommandSender> permissionCheck) {
		this.permissionCheck = permissionCheck;
	}
	
	/**
	 * Gets the minimum amount of allowed arguments for this command.
	 * @return The minimum amount of arguments.
	 */
	public int getMinArgs() {
		return minArgs;
	}
	
	/**
	 * Sets the minimum amount of arguments for this command before throwing an error.
	 * @param minArgs The minimum amount of arguments without throwing an error.
	 */
	public void setMinArgs(int minArgs) {
		this.minArgs = minArgs;
	}
	
	/**
	 * The maximum amount of arguments allowed in this command before throwing an error..
	 * @return The maximum amount of allowed arguments.
	 */
	public int getMaxArgs() {
		return maxArgs;
	}
	
	/**
	 * Sets the maximum amount of arguments for this command before sending an error.
	 * @param maxArgs The maximum amount of arguments allowed for this command. (Default is -1 for no limit)
	 */
	public void setMaxArgs(int maxArgs) {
		this.maxArgs = maxArgs;
	}
	
	/**
	 * Checks if this command is for players only.
	 * @return True if its for players only, false otherwise.
	 */
	public boolean isPlayerOnly() {
		return playerOnly;
	}
	
	/**
	 * Sets this command as player only if true.
	 * @param playerOnly If true, this is only for players, false otherwise.
	 */
	public void setPlayerOnly(boolean playerOnly){
		this.playerOnly = playerOnly;
	}
	
	/**
	 * Gets all the sub commands for this command
	 * @return A set of subCommands.
	 */
	public Set<CoCommand> getChildren() {
		return children;
	}
	
	/**
	 * Sets a list of possible sub commands for this command.
	 * @param children The subCommand set this command has.
	 */
	public void setChildren(Set<CoCommand> children) {
		this.children = children;
	}
}
