package com.coalesce.command.tabcomplete;

import com.coalesce.command.base.AbstractTabContext;

public interface TabExecutor {
	
	/**
	 * The tab completer method
	 * @param context The tab complete context
	 */
	void complete(AbstractTabContext context);
	
}
