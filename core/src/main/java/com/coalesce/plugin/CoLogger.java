package com.coalesce.plugin;

import org.bukkit.command.ConsoleCommandSender;

import static org.bukkit.ChatColor.*;

public class CoLogger {

	private CoPlugin plugin;

	public CoLogger(CoPlugin plugin){
		this.plugin = plugin;
	}

	/**
	 * Logs to console with logging level as INFO.
	 *
	 * @param message The message to log.
	 */
	public void info(String message){
		LogLevel.INFO.log(getConsole(), plugin, message);
	}

	/**
	 * Logs to console with logging level as WARN.
	 *
	 * @param message The message to log.
	 */
	public void warn(String message){
		LogLevel.WARN.log(getConsole(), plugin, message);
	}

	/**
	 * Logs to console with logging level as ERROR.
	 *
	 * @param message The message to log.
	 */
	public void error(String message){
		LogLevel.ERROR.log(getConsole(), plugin, message);
	}

	/**
	 * Logs to console with logging level as DEBUG.
	 *
	 * @param message The message to log.
	 */
	public void debug(String message){
		LogLevel.DEBUG.log(getConsole(), plugin, message);
	}

	private ConsoleCommandSender getConsole(){
		return plugin.getServer().getConsoleSender();
	}

	enum LogLevel {

		INFO(WHITE + "Info"),
		DEBUG(BLUE + "Debug"),
		WARN(YELLOW + "Warn"),
		ERROR(RED + "Error");

		private final String prefix;

		LogLevel(String prefix){
			this.prefix = GRAY + "[" + AQUA + "%s " + prefix + GRAY + "]" + RESET;
		}

		public String getPrefix(){
			return prefix;
		}

		public void log(ConsoleCommandSender console, CoPlugin plugin, String message){
			console.sendMessage(String.format(prefix, plugin.getDisplayName()) + ' ' + message);
		}
	}
}
