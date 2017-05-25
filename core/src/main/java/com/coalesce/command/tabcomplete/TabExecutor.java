package com.coalesce.command.tabcomplete;

import com.coalesce.command.base.ITabContext;

public interface TabExecutor {
	
	/**
	 * The tab completer method
	 * @param context The tab complete context
	 */
	void complete(ITabContext context);
	
}
