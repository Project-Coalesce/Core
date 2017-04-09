package com.coalesce.command.registry;

/**
 * A set of command executors that can execute commands.
 */
public enum Executor {
    
    /**
     * Represents the console
     */
    CONSOLE,
    
    /**
     * Represents a player.
     */
    PLAYER,
    
    /**
     * Represents a block.
     */
    BLOCK,
    
    /**
     * Represents a block, player, or console.
     */
    ALL;
    
}
