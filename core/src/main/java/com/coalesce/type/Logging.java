package com.coalesce.type;

import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.lang.StringBuilder;
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

	/**
	 * Logs to console with logging level as INFO.
	 *
	 * @param message The message to log.
	 */
	default void info(Object message) {
		LogLevel.INFO.log(getConsole(), message);
	}

	/**
	 * Logs to console with logging level as WARN.
	 *
	 * @param message The message to log.
	 */
	default void warn(Object message) {
		LogLevel.WARN.log(getConsole(), message);
	}

	/**
	 * Logs to console with logging level as ERROR.
	 *
	 * @param message The message to log.
	 */
	default void error(Object message) {
		LogLevel.ERROR.log(getConsole(), message);
	}

	/**
	 * Logs to console with logging level as DEBUG.
	 * This generally shouldn't be used in production plugins without a toggle.
	 *
	 * @param message The message to log.
	 */
	default void debug(Object message) {
		LogLevel.DEBUG.log(getConsole(), message);
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
		private final String logPrefix;

		LogLevel(@NotNull String logPrefix) {
			this.logPrefix = DARK_GRAY + "[" + logPrefix + DARK_GRAY + "]" + RESET;
		}

		/**
		 * Gets the prefix for the logging level.
		 * 
		 * @return The logging level enumeration's prefix.
		 */
		public @NotNull String getLogPrefix() {
			return logPrefix;
		}

		/**
		 * Logs the specified object(s) to the specified console sender.
		 *
		 * @param console The console sender to log to.
		 * @param message The object(s) to log.
		 */
		protected void log(ConsoleCommandSender console, Object... message) {
			switch (message.length) {
				case 0:
					return;
				case 1:
					console.sendMessage(logPrefix + ' ' + Objects.toString(message[0]));
					break;
				default:
					StringBuilder builder = new StringBuilder();
					for (Object obj : message) {
						builder.append(Objects.toString(obj));
					}
					console.sendMessage(logPrefix + ' ' + builder.toString());
					break;
			}
		}

		/**
		 * Gets the prefix for the logging level.
		 * 
		 * @return The logging level enumeration's prefix.
		 * @see #getLogPrefix()
		 */
		@Override
		public String toString() {
			return getLogPrefix();
		}
	}
}
