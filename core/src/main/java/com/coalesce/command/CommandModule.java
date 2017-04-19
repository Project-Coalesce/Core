package com.coalesce.command;

import com.coalesce.command.base.CmdContext;
import com.coalesce.command.base.Command;
import com.coalesce.plugin.CoModule;
import com.coalesce.plugin.CoPlugin;
import com.coalesce.util.Safety;
import com.google.common.collect.Lists;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CommandModule extends CoModule {

	private final Map<String, Command> commandMap = new HashMap<>();


	public CommandModule(@NotNull CoPlugin plugin) {
		super(plugin, "Sxtanna Commands");
	}

	@Override
	protected void onEnable() throws Exception {}

	@Override
	protected void onDisable() throws Exception {
		commandMap.clear();
	}


	public void add(Command... commands) {
		Stream.of(commands).forEach(cmd -> {
			commandMap.put(cmd.getName().toLowerCase(), cmd);
			cmd.getAliases().forEach(alias -> commandMap.put(alias.toLowerCase(), cmd));
		});
	}


	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPCommand(PlayerCommandPreprocessEvent e) {
		if (handleCommand(e.getPlayer(), e.getMessage())) e.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onSCommand(ServerCommandEvent e) {
		if (handleCommand(e.getSender(), e.getCommand())) e.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onTabComplete(TabCompleteEvent e) {
		if (!e.getBuffer().startsWith("/") && e.getSender() instanceof Player) return;

		List<String> completes = handleTabComplete(e.getSender(), e.getBuffer());
		if (completes == null) {
			e.setCompletions(getPlugin().getServer().getOnlinePlayers().stream()
										.map(Player::getName)
										.collect(Collectors.toList()));
		} else {
			if (completes.isEmpty()) e.setCancelled(true);
			else {
				e.setCompletions(completes);
			}
		}
	}


	private boolean handleCommand(CommandSender sender, String message) {
		CmdContext context = createContext(sender, message);

		Command command = commandMap.get(context.getAlias());
		if (command == null) return false;

		command.execute(context);
		return true;
	}

	private List<String> handleTabComplete(CommandSender sender, String message) {
		CmdContext context = createContext(sender, message);

		return Safety.apply(commandMap.get(context.getAlias()), command -> command.getCompleter().execute(context));
	}

	private CmdContext createContext(CommandSender sender, String message) {
		List<String> input = Lists.newArrayList(message.split(" "));

		String alias = input.get(0).toLowerCase();
		if (alias.startsWith("/")) alias = alias.substring(1);

		return new CmdContext(sender, alias, input.subList(1, input.size()));
	}

}
