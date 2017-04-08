package com.coalesce.command;

import com.coalesce.Core;
import com.coalesce.command.registry.CommandHost;
import com.coalesce.command.registry.CommandInfo;
import com.coalesce.command.registry.CommandRegister;
import com.coalesce.command.registry.Complete;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Contains the methods to load a command class onto the server.
 */
public
class CommandLoader {
    
    /*
     * The string in both of these hashmaps is the name of the command so it can
     * refer back to where it needs to be executed from.
    */
    public static HashMap<String, Method> completionMethods = new HashMap<>();
    public static HashMap<String, Class<?>> completionClasses = new HashMap<>();
    
    /**
     * Gets the bukkit CommandMap
     * @return Returns the CommandMap
     */
    private static CommandMap getMap() {
        CommandMap map = null;
        Field field;
        try {
            field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            map = (CommandMap)field.get(Bukkit.getServer());
        }
        catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return map;
    }
    
    /**
     * This method registers a class that contains commands.
     * @param cls The class that contains the commands.
     */
    public static void addCommand(Class<?> cls) {
        try {
            new CommandRegister(cls.newInstance());
        }
        catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        for (CommandInfo command : CommandRegister.commands.values()) {
            getMap().register(Core.getInstance().getName(), new CommandHost(command));
        }
    }
    
    /**
     * Regsiters a command tab completion.
     * @param cls The class that contains the tab completion method.
     */
    public static void addCompletion(Class<?> cls) {
        for (Method method : cls.getMethods()) {
            if (method.isAnnotationPresent(Complete.class)) {
                String command =  method.getAnnotation(Complete.class).value();
                completionClasses.put(command, cls);
                completionMethods.put(command, method);
            }
        }
    }
}
