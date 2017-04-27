package com.coalesce.command;

public interface CommandExecutor {
	
	/**
	 * The command execution method.
	 * @param commandMethod The method reference to a command.
	 */
	void execute(CommandContext commandMethod);

}
