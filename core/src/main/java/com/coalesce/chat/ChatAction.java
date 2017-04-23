package com.coalesce.chat;

public enum ChatAction {
    OPEN_URL("open_url"),
    OPEN_FILE("open_file"),
    RUN_COMMAND("run_command"),
    SUGGEST_COMMAND("suggest_command"),
    CHANGE_PAGE("change_page"),
    NONE("none");
    
    private String action;
    
    ChatAction(String action) {
        this.action = action;
    }
    
    public String getAction() { return action; }
}
