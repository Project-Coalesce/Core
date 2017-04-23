package com.coalesce.chat;

import com.coalesce.plugin.CoPlugin;
import org.bukkit.ChatColor;

import static org.bukkit.ChatColor.*;

public final class CoFormatter {
    private String prefix;

    //Constants
    private final static int CENTER_PX = 154;

    public CoFormatter() {
        this.prefix = GRAY + "[" + AQUA + "CoalesceCore" + GRAY + "]" + RESET;
    }

    public CoFormatter(CoPlugin plugin) {
        this.prefix = GRAY + "[" + WHITE + plugin.getDisplayName() + GRAY + "]" + RESET;
    }

    public String format(String message) {
        return prefix + " " + message;
    }

    public String centerString(String message) {
        if (message == null || message.equals("")) return "";
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = getWidth(message);

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = FontInfo.getCharSize(' ');
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
            if (c == '\u00A7') {
                previousCode = true;
                continue;
            } else if (previousCode == true) {
                previousCode = false;
                if (c == 'l' || c == 'L') {
                    isBold = true;
                    continue;
                } else isBold = false;
            } else {
                messagePxSize += FontInfo.getCharSize(c, isBold);
            }
        }
        return messagePxSize;
    }

    /**
     * Alternate color codes in a string, if the chars variable is null then it will use a rainbow effect.
     * If string already contains color codes, they will be stripped.
     *
     * @param str   String to add color to.
     * @param chars Colors that will be alternated in the string, if null then its rainbow.
     * @return Changed String
     */
    public String rainbowifyString(String str, char... chars) {
        str = ChatColor.stripColor(str);
        if (chars == null || chars.length == 0) chars = new char[]{'c', '6', 'e', 'a', 'b', '3', 'd'};
        
        int index = 0;
        String returnValue = "";
        for (char c : str.toCharArray()) {
            returnValue += "&" + chars[index] + c;
            index++;
            if (index == chars.length) {
                index = 0;
            }
        }
        
        return ChatColor.translateAlternateColorCodes('&', returnValue);
    }
}
