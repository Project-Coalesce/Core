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
		LogLevel.INFO.log(getConsole(), message);
	}

	default void warn(Object message) {
		LogLevel.WARN.log(getConsole(), message);
	}

	default void error(Object message) {
		LogLevel.ERROR.log(getConsole(), message);
	}

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

		public @NotNull String getLogPrefix() {
			return logPrefix;
		}

		protected void log(ConsoleCommandSender console, Object message) {
			console.sendMessage(this + " " + Objects.toString(message));
		}

		@Override
		public String toString() {
			return getLogPrefix();
		}

	}

}
