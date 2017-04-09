package com.coalesce.type;

import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.logging.Logger;

import static org.bukkit.ChatColor.*;

/**
 * Represents an object that will log to console
 */
public interface Logging extends Named, PluginDependant {

    /**
     * Get the logger from this Plugin
     *
     * @return The plugin's logger
     */
    default @NotNull Logger getLogger() {
        return getPlugin().getLogger();
    }

    /**
     * Get the server's Console
     *
     * @return Bukkit's ConsoleCommandSender
     */
    default @NotNull ConsoleCommandSender getConsole() {
        return getPlugin().getServer().getConsoleSender();
    }


    default void info(Object message) {
        getConsole().sendMessage(LogLevel.INFO + " " + Objects.toString(message));
    }

    default void warn(Object message) {
        getConsole().sendMessage(LogLevel.WARN + " " + Objects.toString(message));
    }

    default void error(Object message) {
        getConsole().sendMessage(LogLevel.ERROR + " " + Objects.toString(message));
    }

    default void debug(Object message) {
        getConsole().sendMessage(LogLevel.DEBUG + " " + Objects.toString(message));
    }

    /**
     * Represents log levels for colored console output
     */
    enum LogLevel {

        INFO(WHITE + "Info"),
        DEBUG(AQUA + "Debug"),
        WARN(GOLD + "Warn"),
        ERROR(RED + "Error");

        @NotNull
        private final String prefix;

        LogLevel(@NotNull String prefix) {
            this.prefix = DARK_GRAY + "[" + prefix + DARK_GRAY + "]" + RESET;
        }

        /**
         * Get the prefix for this log level
         *
         * @return The prefix
         */
        public @NotNull String getPrefix() {
            return prefix;
        }

        @Override
        public String toString() {
            return getPrefix();
        }

    }

}
