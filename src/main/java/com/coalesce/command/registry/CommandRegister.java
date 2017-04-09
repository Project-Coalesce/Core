package com.coalesce.command.registry;

import java.lang.reflect.Method;
import java.util.HashMap;


/**
 * This puts the command info and command in a hashmap for later use.
 */
public class CommandRegister {
    
    private Object object;
    private CommandInfo commandInfo;
    public static HashMap<String, CommandInfo> commands = new HashMap<>();
    
    public CommandRegister(Object object) {
        this.object = object;
        for (Method mthds : object.getClass().getMethods()) {
            if (mthds.isAnnotationPresent(Cmd.class)) {
                this.commandInfo = new CommandInfo(mthds.getAnnotation(Cmd.class), mthds, object);
                commands.put(commandInfo.getName(), commandInfo);
            }
        }
    }
    
    /**
     * Gets the command object that was created.
     *
     * @return Gets the Object that stores the command.
     */
    public Object getCommand() {
        return object;
    }
    
    /**
     * Creates a new command Object.
     */
    public CommandRegister setCommand(Object object) {
        new CommandRegister(object);
        return this;
    }
    
    /**
     * Gets the command information
     *
     * @return The command information.
     */
    public CommandInfo getCommandInfo() {
        return commandInfo;
    }
    
    /**
     * Sets the command information.
     *
     * @param commandInfo Sets the new command information.
     */
    public CommandRegister setCommandInfo(CommandInfo commandInfo) {
        this.commandInfo = commandInfo;
        return this;
    }
    
}
