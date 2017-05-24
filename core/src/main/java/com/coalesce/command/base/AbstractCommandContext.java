package com.coalesce.command.base;

import com.coalesce.plugin.CoPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractCommandContext {
	
	private CommandSender sender;
	private List<String> args;
	private CoPlugin plugin;
	
	protected AbstractCommandContext(CommandSender sender, String[] args, CoPlugin plugin){
		this.sender = sender;
		this.plugin = plugin;
		List<String> list = new ArrayList<>();
		Stream.of(args).forEach(arg -> list.add(arg));
		this.args = list;
	}
	
	/**
	 * The current plugin.
	 * @return The current plugin being used.
	 */
	public CoPlugin getPlugin() {
		return plugin;
	}
	
	/**
	 * Sends a formatted plguin message.
	 *
	 * <p>Ex. [PlguinName] MESSAGE FROM THE PARAMETER</p>
	 *
	 * @param message The message to send with the plugin name before it.
	 */
	public void pluginMessage(String message) {
		send(plugin.getFormatter().format(message));
	}
	
	/**
	 * Checks if the CommandSender is a player.
	 * @return True if the sender is a player, false otherwise.
	 */
	public boolean isPlayer(){
		return sender instanceof Player;
	}
	
	/**
	 * Changes the sender of this command to a player.
	 * @return Returns the player sending this command.
	 */
	public Player asPlayer(){
		return (Player)sender;
	}
	
	/**
	 * Checks if the CommandSender is the console.
	 * @return True if the sender is the Console, false otherwise.
	 */
	public boolean isConsole(){
		return sender instanceof ConsoleCommandSender;
	}
	
	/**
	 * Changes the sender of this command to the Console.
	 * @return Returns the console sending this command.
	 */
	public ConsoleCommandSender asConsole(){
		return (ConsoleCommandSender)sender;
	}
	
	/**
	 * Gets the CommandSender of this command.
	 * @return The command CommandSender.
	 */
	public CommandSender getSender() {
		return sender;
	}
	
	/**
	 * Gets a list of all the arguments in this command.
	 * @return List of arguments.
	 */
	public List<String> getArgs() {
		return args;
	}
	
	/**
	 * Checks if the command has any arguments.
	 * @return Returns true if the command has any arguments, false otherwise.
	 */
	public boolean hasArgs(){
		return !args.isEmpty();
	}
	
	/**
	 * Gets an argument at a specified index.
	 * @param index The index of the argument.
	 * @return The argument at the index provided.
	 */
	public String argAt(int index) {
		return args.get(index);
	}
	
	/**
	 * Joins all the arguments in the command from the start index to the finish index.
	 * @param start Index to start at.
	 * @param finish Index to finish at.
	 * @return One string for the args between the start and finish index.
	 */
	public String joinArgs(int start, int finish) {
		if (args.isEmpty()) return "";
		return String.join(" ", args.subList(start, finish));
	}
	
	/**
	 * Joins all the arguments after a given point.
	 * @param start Where to start joining the arguments.
	 * @return One string for all the arguments after start.
	 */
	public String joinArgs(int start) {
		return joinArgs(start, args.size());
	}
	
	/**
	 * Joins all the arguments into one string.
	 * @return One string for the command arguments.
	 */
	public String joinArgs() {
		return joinArgs(0);
	}
	
	/**
	 * Sends a message to the player/console.
	 * @param message The message to send.
	 */
	public void send(String message){
		sender.sendMessage(message);
	}
	
}
