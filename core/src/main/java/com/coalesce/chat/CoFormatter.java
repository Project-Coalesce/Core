package com.coalesce.chat;

import com.coalesce.plugin.CoPlugin;
import org.bukkit.ChatColor;

import static org.bukkit.ChatColor.*;

public final class CoFormatter {

	private CoPlugin plugin;
	private String prefix;

	//Constants
	private final static int CENTER_PX = 154;

	public CoFormatter(CoPlugin plugin){
		this.plugin = plugin;
		this.prefix = GRAY + "[" + plugin.getDisplayName() + GRAY + "]" + RESET;
	}

	public String format(String message){
		return prefix + " " + message;
	}

	public String centerString(String message){
		if (message == null || message.equals("")) return "";
		message = ChatColor.translateAlternateColorCodes('&', message);

		int messagePxSize = getWidth(message);

		int halvedMessageSize = messagePxSize / 2;
		int toCompensate = CENTER_PX - halvedMessageSize;
		int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
		int compensated = 0;
		StringBuilder sb = new StringBuilder();
		while (compensated < toCompensate) {
			sb.append(" ");
			compensated += spaceLength;
		}
		return sb.toString() + message;
	}

	public int getWidth(String message) {
		int messagePxSize = 0;
		boolean previousCode = false;
		boolean isBold = false;

		for (char c : message.toCharArray()) {
			if (c == '�') {
				previousCode = true;
				continue;
			} else if (previousCode == true) {
				previousCode = false;
				if (c == 'l' || c == 'L') {
					isBold = true;
					continue;
				} else isBold = false;
			} else {
				DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
				messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
				messagePxSize++;
			}
		}
		return messagePxSize;
	}

}
