package com.coalesce.command.base;

import com.coalesce.plugin.CoPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public interface ICommandContext<E> {
	
	/**
	 * The current plugin.
	 * @return The current plugin being used.
	 */
	CoPlugin getPlugin();
	
	/**
	 * Sends a formatted plguin message.
	 *
	 * <p>Ex. [PlguinName] MESSAGE FROM THE PARAMETER</p>
	 *
	 * @param message The message to send with the plugin name before it.
	 */
	void pluginMessage(String message);
	
	/**
	 * Checks if the CommandSender is a player.
	 * @return True if the sender is a player, false otherwise.
	 */
	boolean isPlayer();
	
	/**
	 * Changes the sender of this command to a player.
	 * @return Returns the player sending this command.
	 */
	Player asPlayer();
	
	/**
	 * Checks if the CommandSender is the console.
	 * @return True if the sender is the Console, false otherwise.
	 */
	boolean isConsole();
	
	/**
	 * Changes the sender of this command to the Console.
	 * @return Returns the console sending this command.
	 */
	ConsoleCommandSender asConsole();
	
	/**
	 * Gets the CommandSender of this command.
	 * @return The command CommandSender.
	 */
	CommandSender getSender();
	
	/**
	 * Gets a list of all the arguments in this command.
	 * @return List of arguments.
	 */
	List<String> getArgs();
	
	/**
	 * Checks if the command has any arguments.
	 * @return Returns true if the command has any arguments, false otherwise.
	 */
	boolean hasArgs();
	
	/**
	 * Gets an argument at a specified index.
	 * @param index The index of the argument.
	 * @return The argument at the index provided.
	 */
	String argAt(int index);
	
	/**
	 * Joins all the arguments in the command from the start index to the finish index.
	 * @param start Index to start at.
	 * @param finish Index to finish at.
	 * @return One string for the args between the start and finish index.
	 */
	String joinArgs(int start, int finish);
	
	/**
	 * Joins all the arguments after a given point.
	 * @param start Where to start joining the arguments.
	 * @return One string for all the arguments after start.
	 */
	String joinArgs(int start);
	
	/**
	 * Joins all the arguments into one string.
	 * @return One string for the command arguments.
	 */
	String joinArgs();
	
	/**
	 * Sends a message to the player/console.
	 * @param message The message to send.
	 */
	void send(String message);
	
}
