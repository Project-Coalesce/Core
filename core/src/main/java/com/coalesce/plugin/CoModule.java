package com.coalesce.plugin;

import org.bukkit.event.Listener;

import static org.bukkit.ChatColor.DARK_RED;

/**
 * Base class for sub "plugins", allows for modular servers
 */
public abstract class CoModule implements Listener {

    private final CoPlugin plugin;

    private final String name;
    private boolean isEnabled;

    /**
     * Create a new module
     *
     * @param plugin The plugin that's creating this module
     * @param name   The name of this module
     */
    public CoModule(CoPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    /**
     * Get the {@link CoPlugin} that created this module
     *
     * @return The {@link CoPlugin}
     */
    public CoPlugin getPlugin() {
        return plugin;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public String getName() {
        return name;
    }

    public void enable() {
        if (isEnabled) {
            throw new IllegalStateException("Module " + getName() + " is already enabled");
        }

        try {
            onEnable();
            getPlugin().registerListener(this);
        }
        catch (Exception e) {
            plugin.getCoLogger().error(DARK_RED + "Failed to enable module " + getName());
            e.printStackTrace();
            return;
        }

        isEnabled = true;
    }

    public void disable() {
        if (!isEnabled) {
            throw new IllegalStateException("Module " + getName() + " isn't enabled");
        }

        isEnabled = false;
        try {
            onDisable();
            getPlugin().unregisterListener(this);
        }
        catch (Exception e) {
            plugin.getCoLogger().warn(DARK_RED + "Failed to disable module " + getName());
            e.printStackTrace();
        }
    }


    /**
     * Called when this module is successfully enabled
     */
    protected abstract void onEnable() throws Exception;

    /**
     * Called when this module is successfully disabled
     */
    protected abstract void onDisable() throws Exception;

}
