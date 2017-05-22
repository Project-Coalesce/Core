package com.coalesce.command.base;

import com.coalesce.command.CoCommand;
import com.coalesce.command.CommandContext;
import com.coalesce.plugin.CoPlugin;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public abstract class AbstractTabContext {
	
	private final CoPlugin plugin;
	private final CoCommand command;
	private final AbstractCommandContext commandContext;
	private List<String> possible = new ArrayList<>();
	
	/**
	 * A new tab completer context.
	 * @param plugin The plugin this completer belongs to.
	 * @param command The command this completer is linked to.
	 * @param commandContext The command context the command is linked to.
	 */
	public AbstractTabContext(CoPlugin plugin, CoCommand command, AbstractCommandContext commandContext) {
		this.plugin = plugin;
		this.command = command;
		this.commandContext = commandContext;
	}
	
	/**
	 * The CoCommand this TabCompletion comes from.
	 * @return CoCommand
	 */
	public CoCommand getCommand() {
		return command;
	}
	
	/**
	 * The CoPlugin this TabCompletion is held in.
	 * @return The current plugin.
	 */
	public CoPlugin getPlugin() {
		return plugin;
	}
	
	/**
	 * Get the command context from the current command.
	 * @return The command context of the current command.
	 */
	public AbstractCommandContext getContext() {
		return commandContext;
	}
	
	/**
	 * Gets the command sender of this command.
	 * @return The command sender.
	 */
	public
	CommandSender getSender() {
		return commandContext.getSender();
	}
	
	/**
	 * Gets a Set of children from this command if any exist.
	 * @return The set of children commands, null if none exist.
	 */
	public
	Set<CoCommand> getCommandChildren() {
		return command.getChildren();
	}
	
	/**
	 * Gets the length of the arguments.
	 * @return The argument length.
	 */
	public int getLength() {
		return commandContext.getArgs().size() - 1;
	}
	
	/**
	 * Quick way to check if the command argument size equals the correct length.
	 * @param length The length to check.
	 * @return True if the length equals the command argument size.
	 */
	public boolean length(int length) {
		return getLength() == length;
	}
	
	/**
	 * Gets the previous argument.
	 * @return The previous argument.
	 */
	public String getPrevious() {
		return commandContext.getArgs().get(getLength());
	}
	
	/**
	 * Checks if the previous argument matches the specified string.
	 * @param previousArg The string to look for.
	 * @return True if the previous arg matches the given string, false otherwise.
	 */
	public boolean previous(String previousArg) {
		return getPrevious().matches(previousArg);
	}
	
	/**
	 * A list of completions
	 * @param completions The completions to add.
	 * @return A list of completions.
	 */
	public void completion(String... completions) {
		possible.clear();
		Stream.of(completions).forEach(c -> possible.add(c));
		return;
	}
	
	/**
	 * Have specific completions at a certain index.
	 * @param index The index for these completions to run.
	 * @param completions The completions to add.
	 * @return A list of completions if the index matches the command index, null otherwise.
	 */
	public void completionAt(int index, String... completions) {
		if (length(index)) {
			possible.clear();
			Stream.of(completions).forEach(c -> possible.add(c));
		}
		return;
	}
	
	/**
	 * Have specific completions after a certain word.
	 * @param previousText The word these completions show up after.
	 * @param completions The completions to add.
	 * @return A list of completions.
	 */
	public void completionAfter(String previousText, String... completions) {
		if (previous(previousText)) {
			possible.clear();
			Stream.of(completions).forEach(c -> possible.add(c));
		}
		return;
	}
	
	/**
	 * Returns the current tab completion list.
	 * @return The tab completion list.
	 */
	public List<String> currentPossibleCompletion() {
		return possible;
	}
	
}
