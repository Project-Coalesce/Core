package com.coalesce.command.base;

import com.coalesce.type.CmdCompleter;
import com.coalesce.type.CmdExecutor;
import com.coalesce.type.Named;
import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.coalesce.util.Safety.apply;
import static com.coalesce.util.Safety.ifNull;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.RED;

public final class Command implements CmdExecutor, Named {

	private String name;
	private String[] desc;
	private Set<String> aliases;

	private String permission;
	private boolean onlyConsole, onlyPlayer;

	private CmdCompleter completer;
	private final CmdExecutor executor;


	private Command parent;
	private Set<Command> children = new HashSet<>();


	public Command(@NotNull CmdExecutor executor, @NotNull String name, @NotNull String... aliases) {
		this.name = name;
		this.executor = executor;
		this.aliases = Stream.of(aliases).map(String::toLowerCase).collect(Collectors.toSet());
	}


	@NotNull
	@Override
	public String getName() {
		return name;
	}

	public @NotNull Set<String> getAliases() {
		return ImmutableSet.copyOf(this.aliases);
	}

	public @Nullable String getPermission() {
		return this.permission;
	}


	public @NotNull CmdCompleter getCompleter() {
		return ifNull(completer, CmdCompleter::getDefault);
	}

	public @NotNull CmdExecutor getExecutor() {
		return executor;
	}


	public boolean isAlias(@NotNull String alias) {
		return this.aliases.contains(alias.toLowerCase());
	}

	public boolean isOnlyConsole() {
		return onlyConsole;
	}

	public boolean isOnlyPlayer() {
		return onlyPlayer;
	}

	public boolean hasChildren() {
		return !this.children.isEmpty();
	}


	public @Nullable Command getParent() {
		return parent;
	}

	public @NotNull Set<Command> getChildren() {
		return ImmutableSet.copyOf(ifNull(this.children, Collections::emptySet));
	}

	public @Nullable Command childByName(@NotNull String alias) {
		return this.children.stream().filter(command -> command.isAlias(alias)).findFirst().orElse(null);
	}


	public Command onlyConsole() {
		this.onlyConsole = true;
		return this;
	}

	public Command onlyPlayer() {
		this.onlyPlayer = true;
		return this;
	}

	public Command require(@NotNull String permission) {
		this.permission = permission;
		return this;
	}


	public Command with(@Nullable CmdCompleter completer) {
		this.completer = completer;
		return this;
	}

	public Command with(@NotNull Command... commands) {
		if (this.children == null) this.children = new HashSet<>();

		Collections.addAll(children, commands);
		children.forEach(command -> command.parent = this);

		return this;
	}


	@Override
	public void execute(CmdContext context) {
		if (!checkRequirements(context)) return;


		if (hasChildren() && !context.noArgs()) {
			String childAlias = context.argAt(0);

			Command childCmd = this.childByName(childAlias);
			if (childCmd != null) {
				context.getInput().remove(0);
				childCmd.execute(context);

				return;
			}
		}

		getExecutor().execute(context);
	}


	private boolean checkRequirements(CmdContext context) {
		if (isOnlyConsole() && context.isPlayer()) {
			context.send(RED + "This command can only be run by " + GOLD + "Console");
			return false;
		}

		if (isOnlyPlayer() && context.isConsole()) {
			context.send(RED + "This command can only be run by a " + GOLD + "Player");
			return false;
		}

		if (!ifNull(apply(getPermission(), context.getSender()::hasPermission), () -> true)) {
			context.send(RED + "This command requires the permission " + GOLD + getPermission());
			return false;
		}

		return true;
	}


	public static Command of(@NotNull CmdExecutor executor, @NotNull String name, @NotNull String... aliases) {
		return new Command(executor, name, aliases);
	}

}
