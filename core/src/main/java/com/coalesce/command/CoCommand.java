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

	public void execute(CommandContext context){

		//If there were no child commands that matched the context
		if (!checkChildren(context)){

			//Check for too many args
			if (context.getArgs().size() > maxArgs){
				context.send(plugin.getFormatter().format(ChatColor.RED + "Incorrect usage!"));
				return;
			}

			//Check for not enough args
			if (context.getArgs().size() < minArgs){
				context.send(plugin.getFormatter().format(ChatColor.RED + "Incorrect usage!"));
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

	public CoPlugin getPlugin(){
		return plugin;
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

	public void setRequiresOperator(boolean requiresOperator) { this.requiresOperator = requiresOperator; }

	public void setPermissionCheck(Predicate<CommandSender> permissionCheck) { this.permissionCheck = permissionCheck; }

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
