package com.coalesce.type;

import com.coalesce.command.base.CmdContext;

import java.util.List;

@FunctionalInterface
public interface CmdCompleter {

	CmdCompleter DEFAULT = (context) -> null;

	static CmdCompleter getDefault() {
		return DEFAULT;
	}


	List<String> execute(CmdContext context);

}
