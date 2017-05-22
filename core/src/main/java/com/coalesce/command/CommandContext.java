package com.coalesce.command;

import com.coalesce.command.base.AbstractCommandContext;
import com.coalesce.plugin.CoPlugin;
import org.bukkit.command.CommandSender;

public final class CommandContext extends AbstractCommandContext {

	public CommandContext(CommandSender sender, String[] args, CoPlugin plugin){
		super(sender, args, plugin);
	}
}
