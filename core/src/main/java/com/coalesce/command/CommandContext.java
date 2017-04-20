package com.coalesce.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public final class CommandContext {

	private CommandSender sender;
	private List<String> args;

	public CommandContext(CommandSender sender, List<String> args){
		this.sender = sender;
		this.args = args;
	}

	public boolean isPlayer(){
		return sender instanceof Player;
	}

	public Player asPlayer(){
		return (Player)sender;
	}

	public boolean isConsole(){
		return sender instanceof ConsoleCommandSender;
	}

	public ConsoleCommandSender asConsole(){
		return (ConsoleCommandSender)sender;
	}

	public CommandSender getSender() {
		return sender;
	}

	public List<String> getArgs() {
		return args;
	}

	public boolean hasArgs(){
		return !args.isEmpty();
	}

	public String argAt(int index) {
		return args.get(index);
	}

	public String joinArgs(int start, int finish) {
		if (args.isEmpty()) return "";
		return String.join(" ", args.subList(start, finish));
	}

	public String joinArgs(int start) {
		return joinArgs(start, args.size());
	}

	public String joinArgs() {
		return joinArgs(0);
	}

	public void send(String message){
		sender.sendMessage(message);
	}
}
