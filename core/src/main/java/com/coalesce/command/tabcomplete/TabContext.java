package com.coalesce.command.tabcomplete;

import com.coalesce.command.CoCommand;
import com.coalesce.command.CommandContext;
import com.coalesce.command.base.AbstractCommandContext;
import com.coalesce.command.base.AbstractTabContext;
import com.coalesce.command.base.ICommandContext;
import com.coalesce.plugin.CoPlugin;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class TabContext extends AbstractTabContext {

    /**
     * A new tab completer context.
     *
     * @param plugin         The plugin this completer belongs to.
     * @param command        The command this completer is linked to.
     * @param commandContext The command context the command is linked to.
     */
    public TabContext(CoPlugin plugin, CoCommand command, ICommandContext commandContext) {
        super(plugin, command, commandContext);
    }
}
