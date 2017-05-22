package com.coalesce.command;

import com.coalesce.command.base.AbstractCommandContext;

public interface CommandExecutor {
	
	/**
	 * The command execution method.
	 * @param commandMethod The method reference to a command.
	 */
	void execute(AbstractCommandContext commandMethod);

}
