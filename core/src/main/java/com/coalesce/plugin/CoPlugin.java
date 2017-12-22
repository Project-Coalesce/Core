package com.coalesce.plugin;

import com.coalesce.chat.CoFormatter;
import com.coalesce.command.CoCommand;
import com.coalesce.command.CommandRegister;
import com.coalesce.config.IConfig;
import com.coalesce.updater.UpdateCheck;
import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

import static org.bukkit.ChatColor.DARK_RED;

public abstract class CoPlugin extends JavaPlugin implements Listener {

    private final List<CoModule> modules = new LinkedList<>();
    private Collection<IConfig> configs = new ArrayList<>();
    private Set<CoCommand> commands = new HashSet<>();
    private ChatColor pluginColor = ChatColor.WHITE;
    private CoFormatter formatter;
    private String displayName;
    private CoLogger logger;

    @Override
    public final void onEnable() {

        this.displayName = pluginColor + getName();

        //Setup basic things
        logger = new CoLogger(this);
        formatter = new CoFormatter(this);

        //Try to call the onEnable
        try {
            onPluginEnable();
        }
        catch (Exception e) {
            logger.error(DARK_RED + "Failed to enable module " + getName());
            e.printStackTrace();
            return;
        }

        enableModules();
    }

    @Override
    public final void onDisable() {
        disableModules();
        try {
            onPluginDisable();
        }
        catch (Exception e) {
            logger.warn(DARK_RED + "Failed to disable module " + getName());
            e.printStackTrace();
        }
    }

    @Override
    public final void onLoad() {

        try {
            onPluginLoad();
        }
        catch (Exception e) {
            logger.error(DARK_RED + "Failed to load module " + getName());
            e.printStackTrace();
            return;
        }

    }

    public void onPluginEnable() throws Exception {
    }

    public void onPluginDisable() throws Exception {
    }

    public void onPluginLoad() throws Exception {
    }

    /**
     * The color of the plugin name in pluginMessages etc.
     *
     * @param color The color you want this plugin to show up as in plugin messages.
     */
    public void setPluginColor(ChatColor color) {
        this.pluginColor = color;
        this.displayName = pluginColor + getName() + ChatColor.RESET;
    }

    /**
     * Gets the displayname of the plugin.
     *
     * @return Returns a formatted displayName for this plugin.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Adds a module to the plugin.
     *
     * @param modules The module to add.
     */
    protected final void addModules(CoModule... modules) {
        Collections.addAll(this.modules, modules);
    }

    /**
     * Gives a list of all the modules in this plugin.
     *
     * @return A list of modules.
     */
    public final List<CoModule> getModules() {
        return ImmutableList.copyOf(modules);
    }

    /**
     * Gets a module.
     *
     * @param clazz The class that extends CoModule.
     * @param <M>   A module class.
     * @return The specified module.
     */
    public final <M extends CoModule> Optional<M> getModule(Class<M> clazz) {
        Iterator<M> iterator = getModules().stream().filter(coModule -> coModule.getClass().equals(clazz)).map(coModule -> ((M)coModule)).iterator();
        return Optional.ofNullable(iterator.hasNext() ? iterator.next() : null);
    }

    /**
     * Enables all the modules.
     */
    public final void enableModules() {
        getModules().forEach(CoModule::enable);
    }

    /**
     * Disables all the plugins modules.
     */
    public final void disableModules() {
        getModules().forEach(CoModule::disable);
    }

    /**
     * Registers one listener.
     *
     * @param listener The listener to register.
     */
    public final void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    /**
     * Registers an array of listeners.
     *
     * @param listeners The array of listeners to add.
     */
    public final void registerListeners(Listener... listeners) {
        Arrays.asList(listeners).forEach(this::registerListener);
    }

    /**
     * Unregisters a Listener from the server.
     *
     * @param listener The listener to unregister.
     */
    public final void unregisterListener(Listener listener) {
        HandlerList.unregisterAll(listener);
    }

    /**
     * Gets the Coalesce Logger.
     *
     * @return CoLogger
     */
    public final CoLogger getCoLogger() {
        return logger;
    }

    /**
     * Gets the Coalesce Formatter.
     *
     * @return CoFormatter
     */
    public final CoFormatter getFormatter() {
        return formatter;
    }

    /**
     * A collection of all the current configurations of a plugin.
     *
     * @return A configuration file list.
     */
    public final Collection<IConfig> getConfigurations() {
        return configs;
    }

    /**
     * Looks for a config of any type. (If the config being looked up does not exist, it defaults to creating it in yml)
     *
     * @param name The name of the config you're looking for.
     * @return The config.
     */
    public final IConfig getConfig(String name) {
        for (IConfig config : configs) {
            if (config.getName().equals(name)) {
                return config;
            }
        }
        return null;
    }

    /**
     * Add several commands to the plugin.
     *
     * @param commands The command to add.
     */
    public final void addCommand(CoCommand... commands) {
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
        for (CoCommand command : commands) {
            if (map.getCommand(command.getName()) == null) {
                this.commands.add(command);
                map.register(getDisplayName(), new CommandRegister(command, this));
            }
        }
    }

    /**
     * Adds a command to the plugin.
     *
     * @param command The command to add.
     */
    public final void addCommand(CoCommand command) {
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
        if (map.getCommand(command.getName()) == null) {
            this.commands.add(command);
            map.register(getDisplayName(), new CommandRegister(command, this));
        }
    }

    /**
     * Gets a set of all the commands in this plugin.
     *
     * @return A command set.
     */
    public final Set<CoCommand> getCommands() {
        return commands;
    }

    /**
     * Checks if an update exists for this plugin.
     *
     * @param repositoryOwner The user or organization that this repository is held in.
     * @param repositoryName  The name of the repository.
     * @param autoUpdate      Whether or not to download a new version if it's out.
     */
    public final void updateCheck(String repositoryOwner, String repositoryName, boolean autoUpdate) {
        new UpdateCheck(this, repositoryOwner, repositoryName, getFile(), autoUpdate);
    }
}
