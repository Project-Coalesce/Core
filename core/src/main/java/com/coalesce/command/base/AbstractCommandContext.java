package com.coalesce.command.base;

import com.coalesce.plugin.CoPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractCommandContext implements ICommandContext {
	
	private CommandSender sender;
	private List<String> args;
	private CoPlugin plugin;
	
	public AbstractCommandContext(CommandSender sender, String[] args, CoPlugin plugin){
		this.sender = sender;
		this.plugin = plugin;
		List<String> list = new ArrayList<>();
		Stream.of(args).forEach(arg -> list.add(arg));
		this.args = list;
	}
	
	@Override
	public CoPlugin getPlugin() {
		return plugin;
	}
	
	@Override
	public void pluginMessage(String message) {
		send(plugin.getFormatter().format(message));
	}
	
	@Override
	public boolean isPlayer(){
		return sender instanceof Player;
	}
	
	@Override
	public Player asPlayer(){
		return (Player)sender;
	}
	
	@Override
	public boolean isConsole(){
		return sender instanceof ConsoleCommandSender;
	}
	
	@Override
	public ConsoleCommandSender asConsole(){
		return (ConsoleCommandSender)sender;
	}
	
	@Override
	public CommandSender getSender() {
		return sender;
	}
	
	@Override
	public List<String> getArgs() {
		return args;
	}
	
	@Override
	public boolean hasArgs(){
		return !args.isEmpty();
	}
	
	@Override
	public String argAt(int index) {
		return args.get(index);
	}
	
	@Override
	public String joinArgs(int start, int finish) {
		if (args.isEmpty()) return "";
		return String.join(" ", args.subList(start, finish));
	}
	
	@Override
	public String joinArgs(int start) {
		return joinArgs(start, args.size());
	}
	
	@Override
	public String joinArgs() {
		return joinArgs(0);
	}

	@Override
	public void send(String message){
		sender.sendMessage(message);
	}
	
}
