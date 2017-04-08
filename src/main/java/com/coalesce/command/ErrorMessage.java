package com.coalesce.command;

import org.bukkit.ChatColor;

/**
 * A set of basic error messages for commands. These can be changed to your liking in the future, but for now, this will do.
 */
public enum ErrorMessage {
    
    
    NO_PERMISSION("&cError: &4You don't have permission to do that."),
    CONSOLE_ONLY("&cError: &4Command can only be executed by the console."),
    PLAYER_ONLY("&cError: &4Command can only be executed by a player."),
    BLOCK_ONLY("&cError: &4Command can only be executed by a command block."),
    PLAYER_CONSOLE_ONLY("&cError: &4Command is for player and console use only."),
    BLOCK_CONSOLE_ONLY("&cError: &4Command is for block and console use only."),
    PLAYER_BLOCK_ONLY("&cError: &4Command is for player and block use only."),
    TOO_MANY_ARGS("&cError: &4Too many arguments provided."),
    NOT_ENOUGH_ARGS("&cError: &4Not enough arguments provided.");
    
    String error;
    
    ErrorMessage(String error) {
        this.error = error;
    }
    
    public String getError() {
        return error;
    }
    
    public String format() {
        return ChatColor.translateAlternateColorCodes('&', error);
    }
    
}
