package com.coalesce.type;

import org.bukkit.command.ConsoleCommandSender;

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
	default Logger getLogger() {
		return getPlugin().getLogger();
	}

	/**
	 * Get the server's Console
	 *
	 * @return Bukkit's ConsoleCommandSender
	 */
	default ConsoleCommandSender getConsole() {
		return getPlugin().getServer().getConsoleSender();
	}


	default void info(Object message) {
		getConsole().sendMessage(LogLevel.INFO + " " +  message.toString());
	}

	default void warn(Object message) {
		getConsole().sendMessage(LogLevel.WARN + " " +  message.toString());
	}

	default void error(Object message) {
		getConsole().sendMessage(LogLevel.ERROR + " " +  message.toString());
	}


	/**
	 * Represents log levels for colored console output
	 */
	enum LogLevel {

		INFO(WHITE + "Info"),
		WARN(GOLD + "Warn"),
		ERROR(RED + "Error");

		private final String prefix;

		LogLevel(String prefix) {
			this.prefix = DARK_GRAY + "[" + prefix + DARK_GRAY + "]" + RESET;
		}

		/**
		 * Get the prefix for this log level
		 *
		 * @return The prefix
		 */
		public String getPrefix() {
			return prefix;
		}

		@Override
		public String toString() {
			return getPrefix();
		}

	}

}
