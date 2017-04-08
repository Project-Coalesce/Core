package com.coalesce.command.registry;

/**
 * Created by Noah on 4/8/2017.
 */
public
@interface Complete {
    
    /**
     * The name of the command this completer completes.
     */
    String value();
}
