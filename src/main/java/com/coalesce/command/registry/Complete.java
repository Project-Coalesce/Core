package com.coalesce.command.registry;

/**
 * This declares a method a tab completion.
 */
public @interface Complete {
    
    /**
     * The name of the command this completer completes.
     */
    String value();
}
