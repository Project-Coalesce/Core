package com.coalesce.chat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class TextComponent{
    private ChatAction clickAction = ChatAction.NONE;
    private String clickActionValue = "";
    private String hoverValue = "";
    private String text = "";
    private CoFormatter formatter = new CoFormatter();

    public TextComponent(String string) {
        withText(string);
    }

    public TextComponent() {}

    public TextComponent withClickEvent(ChatAction action, String value) {
        clickAction = action;
        clickActionValue = value;

        return this;
    }

    public TextComponent withText(String text) {
        this.text = text;

        return this;
    }

    public TextComponent withHover(String str) {
        hoverValue = str;

        return this;
    }

    public TextComponent center() {
        text = formatter.centerString(text);

        return this;
    }

    public TextComponent rainbow(char... chars) {
        text = formatter.rainbowifyString(text, chars);

        return this;
    }

    public TextComponent withSending(Player player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + build());

        return this;
    }

    //TODO Use some random json library, please
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

    public String getText() {
        return text;
    }

    public String getHover() {
        return hoverValue;
    }
}
