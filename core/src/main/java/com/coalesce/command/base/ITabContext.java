package com.coalesce.command.base;

import com.coalesce.command.CoCommand;
import com.coalesce.plugin.CoPlugin;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Set;

public interface ITabContext {
	
	/**
	 * The CoCommand this TabCompletion comes from.
	 * @return CoCommand
	 */
	CoCommand getCommand();
	
	/**
	 * The CoPlugin this TabCompletion is held in.
	 * @return The current plugin.
	 */
	CoPlugin getPlugin();
	
	/**
	 * Get the command context from the current command.
	 * @return The command context of the current command.
	 */
	ICommandContext getContext();
	
	/**
	 * Gets the command sender of this command.
	 * @return The command sender.
	 */
	CommandSender getSender();
	
	/**
	 * Gets a Set of children from this command if any exist.
	 * @return The set of children commands, null if none exist.
	 */
	Set<CoCommand> getCommandChildren();
	
	/**
	 * Gets the length of the arguments.
	 * @return The argument length.
	 */
	int getLength();
	
	/**
	 * Quick way to check if the command argument size equals the correct length.
	 * @param length The length to check.
	 * @return True if the length equals the command argument size.
	 */
	boolean length(int length);
	
	/**
	 * Gets the previous argument.
	 * @return The previous argument.
	 */
	String getPrevious();
	
	/**
	 * Checks if the previous argument matches the specified string.
	 * @param previousArg The string to look for.
	 * @return True if the previous arg matches the given string, false otherwise.
	 */
	boolean previous(String previousArg);
	
	/**
	 * A list of completions
	 * @param completions The completions to add.
	 * @return A list of completions.
	 */
	void completion(String... completions);
	
	/**
	 * Have specific completions at a certain index.
	 * @param index The index for these completions to run.
	 * @param completions The completions to add.
	 * @return A list of completions if the index matches the command index, null otherwise.
	 */
	void completionAt(int index, String... completions);
	
	/**
	 * Have specific completions after a certain word.
	 * @param previousText The word these completions show up after.
	 * @param completions The completions to add.
	 * @return A list of completions.
	 */
	void completionAfter(String previousText, String... completions);
	
	/**
	 * Returns the current tab completion list.
	 * @return The tab completion list.
	 */
	List<String> currentPossibleCompletion();
	
}
