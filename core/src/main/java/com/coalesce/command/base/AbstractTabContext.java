package com.coalesce.command.base;

import com.coalesce.command.CoCommand;
import com.coalesce.plugin.CoPlugin;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public abstract class AbstractTabContext implements ITabContext {
	
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
	
	@Override
	public CoCommand getCommand() {
		return command;
	}
	
	@Override
	public CoPlugin getPlugin() {
		return plugin;
	}
	
	@Override
	public ICommandContext getContext() {
		return commandContext;
	}
	
	@Override
	public CommandSender getSender() {
		return commandContext.getSender();
	}
	
	@Override
	public Set<CoCommand> getCommandChildren() {
		return command.getChildren();
	}
	
	@Override
	public int getLength() {
		return commandContext.getArgs().size() - 1;
	}
	
	@Override
	public boolean length(int length) {
		return getLength() == length;
	}
	
	@Override
	public String getPrevious() {
		return commandContext.getArgs().get(getLength());
	}
	
	@Override
	public boolean previous(String previousArg) {
		return getPrevious().matches(previousArg);
	}
	
	@Override
	public void completion(String... completions) {
		possible.clear();
		Stream.of(completions).forEach(c -> possible.add(c));
		return;
	}
	
	@Override
	public void completionAt(int index, String... completions) {
		if (length(index)) {
			possible.clear();
			Stream.of(completions).forEach(c -> possible.add(c));
		}
		return;
	}
	
	@Override
	public void completionAfter(String previousText, String... completions) {
		if (previous(previousText)) {
			possible.clear();
			Stream.of(completions).forEach(c -> possible.add(c));
		}
		return;
	}

	@Override
	public List<String> currentPossibleCompletion() {
		return possible;
	}
	
}
