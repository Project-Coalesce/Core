package com.coalesce.command.registry;

import com.coalesce.command.CommandLoader;
import com.coalesce.command.ErrorMessage;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * This is where the commands registered through the Cmd Annotation are ran.
 */
public class CommandHost extends Command {
    
    private CommandInfo command;
    
    public CommandHost(CommandInfo commandInfo) {
        super(commandInfo.getName());
        this.command = commandInfo;
        this.description = commandInfo.getDesc();
        this.usageMessage = commandInfo.getUsage();
        this.setAliases(Arrays.asList(commandInfo.getAliases()));
    }
    
    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (checkLength(sender, args) || checkExecutor(sender) || !permissionCheck(sender, command.getPermissions())) {
            return true;
        }
        try {
            command.getMethod().invoke(command.getContained(), sender, alias, args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
            e.printStackTrace();
        }
        return true;
    }
    
    /**
     * This checks if the length of the command matches the command annotations' requirement.
     *
     * @param sender The sender of the command.
     * @param args The command arguments.
     * @return True of the command didn't meet the criteria.
     */
    private boolean checkLength(CommandSender sender, String[] args) {
        if (args.length > command.getMax() && command.getMax() > -1) {
            sender.sendMessage(ErrorMessage.TOO_MANY_ARGS.format());
            return true;
        }
        if (args.length < command.getMin() && command.getMin() > -1) {
            sender.sendMessage(ErrorMessage.NOT_ENOUGH_ARGS.format());
            return true;
        }
        return false;
    }
    
    /**
     * Checks what/who is executing the command.
     *
     * @param sender The sender executing the command.
     * @return Returns true if the command cannot be executed
     */
    private boolean checkExecutor(CommandSender sender) {
        List<Executor> executors = new ArrayList<>();
        for (Executor executor : command.getExecutors()) {
            executors.add(executor);
        }
        boolean isPlayer = executors.contains(Executor.PLAYER);
        boolean isConsole = executors.contains(Executor.CONSOLE);
        boolean isBlock = executors.contains(Executor.BLOCK);
        
        if (isPlayer && !isConsole && !isBlock) {
            if (!(sender instanceof Player)) {
                return true;
            }
        }
        
		if (!isPlayer && isConsole && !isBlock) { // Console only
            if (!(sender instanceof ConsoleCommandSender)) {
                return true;
            }
        }
    
		if (!isPlayer && !isConsole && isBlock) { // Block only
            if (!(sender instanceof BlockCommandSender)) {
                return true;
            }
        }
    
		if (isPlayer && isConsole && !isBlock) { // Player & console
            if (sender instanceof BlockCommandSender) {
                return true;
            }
        }
    
		if (isPlayer && !isConsole && isBlock) { // Player & block
            if (sender instanceof ConsoleCommandSender) {
                return true;
            }
        }
    
		if (!isPlayer && isConsole && isBlock) { // Console & block
            if (sender instanceof Player) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (command.hasCompleter()) {
            Method completions = CommandLoader.completionMethods.get(command.getName()); //Gets the method where the completion is held.
            Class<?> cls = CommandLoader.completionClasses.get(command.getName()); //Gets the class where this completion is held.
    
            try {
                return (List<String>) completions.invoke(cls.newInstance(), sender, alias, args);
            }
            catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    /**
     * Checks if the sender of the command has the correct permissions. (the permissions are provided in the @{@link Cmd} annotation)
     * 
     * @param sender The command sender who're being checked.
     * @param permissions The permissions which are being checked.
     * @return Whether or not the sender has all the specified permissions.
     */
    private boolean permissionCheck(CommandSender sender, String... permissions) {
	if (sender instanceof ConsoleCommandSender) { // Speed up a bit, due to console having all permissions, always.
	    return true;
	}
        return Stream.of(permissions).allMatch(sender::hasPermission);
    }
    
}
