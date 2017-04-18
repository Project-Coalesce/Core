package com.coalesce.command;

import java.util.function.Consumer;

interface CommandExecutor {

	void execute(Consumer<CommandContext> commandMethod);

}
