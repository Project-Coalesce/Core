package com.coalesce.command;

import com.coalesce.command.base.AbstractCommandContext;
import com.coalesce.command.base.AbstractTabContext;
import com.coalesce.command.base.ICommandContext;
import com.coalesce.command.tabcomplete.TabExecutor;
import com.coalesce.plugin.CoPlugin;
import org.bukkit.command.CommandSender;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CommandBuilder {

    private CoCommand command;

    /**
     * Creates a new command Builder
     *
     * @param plugin The host plugin
     * @param name   The name of the command.
     */
    public CommandBuilder(CoPlugin plugin, String name) {
        command = new CoCommand(plugin, name);
    }

    /**
     * The method the command needs to run.
     *
     * @param executor The method of this command. Should be this::method_name
     */
    public CommandBuilder executor(CommandExecutor executor) {
        command.setCommandExecutor(executor);
        return this;
    }

    /**
     * The method the command needs to create a tab completion.
     *
     * @param executor The method of this command's tab completer
     */
    public CommandBuilder completer(TabExecutor executor) {
        command.setTabExecutor(executor);
        return this;
    }

    /**
     * The list of aliases for this command.
     *
     * @param aliases List of aliases.
     */
    public CommandBuilder aliases(String... aliases) {
        command.setAliases(aliases);
        return this;
    }

    /**
     * The list of aliases for this command.
     *
     * @param aliases List of aliases.
     */
    public CommandBuilder aliases(Set<String> aliases) {
        command.setAliases(aliases);
        return this;
    }

    /**
     * The command description.
     *
     * @param description The command description.
     */
    public CommandBuilder description(String description) {
        command.setDescription(description);
        return this;
    }

    /**
     * The command usage.
     *
     * @param usage The command usage.
     */
    public CommandBuilder usage(String usage) {
        command.setUsage(usage);
        return this;
    }

    /**
     * The needed permissions for this command.
     *
     * @param permission The required permission node.
     */
    public CommandBuilder permission(String permission) {
        command.setPermission(permission);
        return this;
    }

    /**
     * Forces the sender of this command to be an operator.
     */
    public CommandBuilder requireOp() {
        command.setRequiresOperator(true);
        return this;
    }

    /**
     * Sets the require operator variable
     *
     * @param requires True = require operator, false does opposite.
     */
    public CommandBuilder requiresOp(boolean requires) {
        command.setRequiresOperator(requires);
        return this;
    }

    /**
     * Checks if the sender has permissions.
     */
    public CommandBuilder permissionCheck(Predicate<CommandSender> predicate) {
        command.setPermissionCheck(predicate);
        return this;
    }

    /**
     * Minimum amount of arguments allowed in a command without throwing an error.
     *
     * @param minArgs The min amount of args without error.
     */
    public CommandBuilder minArgs(int minArgs) {
        command.setMinArgs(minArgs);
        return this;
    }

    /**
     * Sets the maximum amount of arguments allowed in a command without throwing an error.
     *
     * @param maxArgs The max amount of args without error.
     */
    public CommandBuilder maxArgs(int maxArgs) {
        command.setMaxArgs(maxArgs);
        return this;
    }

    /**
     * Makes this command for players only.
     */
    public CommandBuilder playerOnly() {
        command.setPlayerOnly(true);
        return this;
    }

    /**
     * A list of CoCommands that also act as subcommands.
     *
     * @param subCommands The list of CoCommands.
     */
    public CommandBuilder children(CoCommand... subCommands) {
        command.setChildren(Stream.of(subCommands).collect(Collectors.toSet()));
        return this;
    }

    /**
     * A set of CoCommands that also act as subcommands.
     *
     * @param subCommands The set of CoCommands.
     */
    public CommandBuilder children(Set<CoCommand> subCommands) {
        command.setChildren(subCommands);
        return this;
    }

    /**
     * This builds the CoCommand.
     *
     * @return A new CoCommand.
     */
    public CoCommand build() {
        return command;
    }
}
