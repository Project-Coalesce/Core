package com.coalesce.chat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TextComponent {

    private String text = "";
    private ChatAction clickAction = ChatAction.NONE;
    private String clickActionValue = "";
    private String hoverValue = "";

    public TextComponent(String string) {
        setText(string);
    }

    public void setClickEvent(ChatAction action, String value) {
        clickAction = action;
        clickActionValue = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String str) {
        text = str;
    }

    public String getHover() {
        return hoverValue;
    }

    public void setHover(String str) {
        hoverValue = str;
    }

    public String build() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"text\":\"" + text + "\"");
        if (clickAction != ChatAction.NONE) {
            sb.append(",\"clickEvent\":{\"action\":\"");
            switch (clickAction) {
                case OPEN_URL:
                    sb.append("open_url");
                    break;
                case OPEN_FILE:
                    sb.append("open_file");
                    break;
                case CHANGE_PAGE:
                    sb.append("change_page");
                    break;
                case RUN_COMMAND:
                    sb.append("run_command");
                    break;
                case SUGGEST_COMMAND:
                    sb.append("suggest_command");
                    break;
            }
            sb.append("\",\"value\":\"" + clickActionValue + "\"}");
        }

        if (hoverValue != null && hoverValue != "") {
            sb.append(",\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\""
                    + hoverValue + "\"}]}}");
        }

        sb.append("}");
        return sb.toString();
    }

    public void sendToPlayer(Player player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + build());
    }
}
