package com.coalesce.command.tabcomplete;

public interface TabExecutor {

    /**
     * The tab completer method
     *
     * @param context The tab complete context
     */
    void complete(TabContext context);

}
