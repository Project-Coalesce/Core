package com.coalesce.command.base;

import com.coalesce.command.resolve.Resolver;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CmdContext {

	private final CommandSender sender;

	private final String alias;
	private final List<String> input;


	public CmdContext(CommandSender sender, String alias, List<String> input) {
		this.sender = sender;
		this.alias = alias;
		this.input = input;
	}


	public @NotNull CommandSender getSender() {
		return sender;
	}

	public @NotNull String getAlias() {
		return alias;
	}

	public @NotNull List<String> getInput() {
		return input;
	}


	public void send(Object... message) {
		getSender().sendMessage(String.join("\n", Stream.of(message).map(Objects::toString).collect(Collectors.toList())));
	}


	public boolean isPlayer() {
		return sender instanceof Player;
	}

	public boolean isConsole() {
		return sender instanceof ConsoleCommandSender;
	}

	public Player asPlayer() {
		return ((Player) sender);
	}

	public ConsoleCommandSender asConsole() {
		return ((ConsoleCommandSender) sender);
	}


	public boolean usedAlias(@NotNull String alias) {
		return getAlias().equalsIgnoreCase(alias);
	}


	/**
	 * Check if there is no input
	 *
	 * @return True if there is no input
	 */
	public boolean noArgs() {
		return input.isEmpty();
	}

	/**
	 * Check if there is no input, and if so, send a message
	 *
	 * @param message The message to send
	 * @return True if there is no input
	 *
	 * @see #noArgs()
	 */
	public boolean noArgs(Object... message) {
		boolean result = noArgs();
		if (result) send(message);

		return result;
	}


	/**
	 * Check if there is at least this much input
	 *
	 * @param count The amount of input
	 * @return True if there is
	 */
	public boolean hasArgs(int count) {
		return input.size() >= count;
	}

	/**
	 * Check if there is at least this much input, and if not, send a message
	 *
	 * @param count The amount of input
	 * @param message The message to send if not
	 * @return True if there is
	 *
	 * @see #hasArgs(int)
	 */
	public boolean hasArgs(int count, Object... message) {
		boolean result = hasArgs(count);
		if (!result) send(message);

		return result;
	}


	public String argAt(int index) {
		return getInput().get(index);
	}

	/**
	 * Read an argument using {@link Resolver}
	 *
	 * @param clazz The object class
	 * @param index The index
	 * @param <O> The object type
	 *
	 * @return The read argument or null
	 */
	public <O> @Nullable O read(Class<O> clazz, int index) {
		return Resolver.read(clazz, this, index);
	}

	/**
	 * Read an argument using {@link Resolver}, if null send a message
	 *
	 * @param clazz The object class
	 * @param index The index
	 * @param <O> The object type
	 *
	 * @return The read argument or null
	 */
	public <O> @Nullable O read(Class<O> clazz, int index, Object... message) {
		O object = read(clazz, index);
		if (object == null) send(message);

		return object;
	}


	public String joinArgs(int start, int finish) {
		if (getInput().isEmpty()) return "";
		return String.join(" ", getInput().subList(start, finish));
	}

	public String joinArgs(int start) {
		return joinArgs(start, getInput().size());
	}

	public String joinArgs() {
		return joinArgs(0);
	}

}
