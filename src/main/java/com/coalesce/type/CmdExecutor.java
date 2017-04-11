package com.coalesce.type;

import com.coalesce.command.base.CmdContext;

@FunctionalInterface
public interface CmdExecutor {

	void execute(CmdContext context);

}
