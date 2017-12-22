package com.coalesce.chat;

public final class TextComponent {

    private final ClickAction clickAction;
    private final String clickActionValue;
    private final String hoverValue;
    private final String text;

    private TextComponent(Builder builder) {
        this.text = builder.text;
        this.hoverValue = builder.hoverValue;
        this.clickAction = builder.clickAction;
        this.clickActionValue = builder.clickActionValue;
    }

    public String getRawText() {
        return text;
    }

    public String getHoverValue() {
        return hoverValue;
    }

    public String getClickActionValue() {
        return clickActionValue;
    }

    public ClickAction getClickAction() {
        return clickAction;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"text\":\"" + text + "\"");
        if (clickAction != ClickAction.NONE) {
            sb.append(",\"clickEvent\":{\"action\":\"");
            sb.append(clickAction.getAction());
            sb.append("\",\"value\":\"" + clickActionValue + "\"}");
        }

        if (hoverValue != null && hoverValue != "") {
            sb.append(",\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"" + hoverValue + "\"}]}}");
        }

        sb.append("}");
        return sb.toString();
    }

    public static class Builder {

        private ClickAction clickAction = ClickAction.NONE;
        private String clickActionValue = "";
        private String hoverValue = "";
        private String text = "";

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder hover(String hoverValue) {
            this.hoverValue = hoverValue;
            return this;
        }

        public Builder clickActionValue(String clickActionValue) {
            this.clickActionValue = clickActionValue;
            return this;
        }

        public Builder clickAction(ClickAction clickAction) {
            this.clickAction = clickAction;
            return this;
        }

        public TextComponent build() {
            return new TextComponent(this);
        }

    }

    public enum ClickAction {
        OPEN_URL("open_url"),
        OPEN_FILE("open_file"),
        RUN_COMMAND("run_command"),
        SUGGEST_COMMAND("suggest_command"),
        CHANGE_PAGE("change_page"),
        NONE("none");

        private final String action;

        ClickAction(String action) {
            this.action = action;
        }

        public String getAction() {
            return action;
        }
    }

}
