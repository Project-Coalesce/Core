package com.coalesce.command;

import com.coalesce.plugin.CoPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandRegistry implements Listener {

	private CoPlugin plugin;
	private Set<CoCommand> commands;

	public CommandRegistry(CoPlugin plugin){
		this.plugin = plugin;

		plugin.register(this);

		commands = new HashSet<>();
	}

	public void registerCommand(CoCommand command){

		if (commands.contains(command)) return;

		commands.add(command);
	}

	@EventHandler
	private void onPlayerCommand(PlayerCommandPreprocessEvent event){
		if (handleCommand(event.getPlayer(), event.getMessage().replace("/", "").split(" "))){
			event.setCancelled(true);
		}
	}

	@EventHandler
	private void onConsoleCommand(ServerCommandEvent event){
		if (handleCommand(event.getSender(), event.getCommand().replace("/", "").split(" "))){
			event.setCancelled(true);
		}
	}

	private boolean handleCommand(CommandSender sender, String[] args){

		for (CoCommand command : commands){
			if (command.matchesCommand(args[0])){

				command.execute(new CommandContext(sender, Stream.of(Arrays.copyOfRange(args, 1, args.length - 1)).collect(Collectors.toList())));
				return true;
			}
		}

		return false;
	}
}
