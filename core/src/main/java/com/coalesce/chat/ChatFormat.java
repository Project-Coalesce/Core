package com.coalesce.chat;

import org.bukkit.ChatColor;

public class ChatFormat {

    private final static int CENTER_PX = 154;

    public static String centererMessage(String str) {
        if (str == null || str.equals("")) return "";
        str = ChatColor.translateAlternateColorCodes('&', str);

        int messagePxSize = getWidth(str);

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        return sb.toString() + str;
    }

    public static int getWidth(String str) {
        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : str.toCharArray()) {
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
