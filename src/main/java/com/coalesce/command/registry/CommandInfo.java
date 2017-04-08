package com.coalesce.command.registry;

import java.lang.reflect.Method;

/**
 * This generates the CommandInfo provided by the @Cmd annotation.
 */
public class CommandInfo {
    
    private Cmd command;
    private String name;
    private String desc;
    private String usage;
    private String[] aliases = null;
    private String[] permissions = null;
    private int min;
    private int max;
    private Executor[] executor;
    private Method method;
    private Object contained;
    private boolean completer;
    
    /**
     * Generates the command information for the BaseCommand to register.
     *
     * @param command The command the information is coming from.
     * @param mthds The method that contains the command.
     * @param contained The object the method is contained in.
     */
    public CommandInfo(Cmd command, Method mthds, Object contained) {
        this.setMethod(mthds);
        this.command = command;
        this.name = command.name();
        this.desc = command.desc();
        this.usage = command.usage();
        this.aliases = command.aliases();
        this.permissions = command.permissions();
        this.min = command.min();
        this.max = command.max();
        this.executor = command.executor();
        this.contained = contained;
        this.setCompletable(command.completer());
    }
    
    /**
     * Gets the object the command is contained in.
     *
     * @return Gets the class this command is contained in.
     */
    public Object getContained() {
        return contained;
    }
    
    /**
     * Sets the object the command is contained in.
     *
     * @param contained
     */
    public void setContained(Object contained) {
        this.contained = contained;
    }
    
    /**
     * Get the name of the command.
     *
     * @return The command name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the description of the command.
     *
     * @return The command description.
     */
    public String getDesc() {
        return desc;
    }
    
    /**
     * Get the usage message of the command.
     *
     * @return The command usage.
     */
    public String getUsage() {
        return usage;
    }
    
    /**
     * Get the allowed executors of the command.
     *
     * @return The allowed executors.
     */
    public Executor[] getExecutors() {
        return executor;
    }
    
    /**
     * Get the aliases of the command.
     *
     * @return The command aliases.
     */
    public String[] getAliases() {
        return aliases;
    }
    
    /**
     * Get the permissions to perform this command.
     *
     * @return The command permissions.
     */
    public String[] getPermissions() {
        return permissions;
    }
    
    /**
     * Get the minimum amount of arguments allowed in this command.
     *
     * @return The minimum amount of arguments
     */
    public int getMin() {
        return min;
    }
    
    /**
     * Get the maximum amount of arguments allowed in this command.
     *
     * @return The maximum amount of arguments
     */
    public int getMax() {
        return max;
    }
    
    /**
     * Gets the command information.
     *
     * @return The command information.
     */
    public Cmd getCommand() {
        return command;
    }
    
    /**
     * Sets the name of a command.
     *
     * @param name The new name of the command.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Sets the description of the command.
     *
     * @param desc The new description of the command.
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    /**
     * Sets the command usage.
     *
     * @param usage The new command usage.
     */
    public void setUsage(String usage) {
        this.usage = usage;
    }
    
    /**
     * Sets the allowed command executors.
     *
     * @param executor The executors allowed to perform this command.
     */
    public void setExecutor(Executor... executor) {
        this.executor = executor;
    }
    
    /**
     * Sets the aliases of the command.
     *
     * @param aliases The command aliases.
     */
    public void setAliases(String... aliases) {
        this.aliases = aliases;
    }
    
    /**
     * Sets the permissions of the command.
     *
     * @param permissions The command permissions.
     */
    public void setPermissions(String... permissions) {
        this.permissions = permissions;
    }
    
    /**
     * Sets the minimum amount of arguments allowed in a command.
     *
     * @param min The minimum allowed amount of arguments.
     */
    public void setMin(int min) {
        this.min = min;
    }
    
    /**
     * Sets the maximum amount of arguments allowed in a command.
     *
     * @param max The maximum allowed amount of arguments.
     */
    public void setMax(int max) {
        this.max = max;
    }
    
    /**
     * Sets the command information.
     *
     * @param command The command information.
     */
    public void setCommand(Cmd command) {
        this.command = command;
    }
    
    /**
     * Gets the method the command is contained in.
     *
     * @return The command method.
     */
    public Method getMethod() {
        return method;
    }
    
    /**
     * Sets the method the command is in.
     *
     * @param method The command method
     */
    public void setMethod(Method method) {
        this.method = method;
    }
    
    /**
     * Checks if the command has a tab completion method.
     */
    public boolean hasCompleter() {
        return completer;
    }
    
    /**
     * Forces it to have a tab completion method. (It may not exist)
     *
     * @param completer True gives it a tab completer, false does the opposite.
     */
    public void setCompletable(boolean completer) {
        this.completer = completer;
    }
    
}
