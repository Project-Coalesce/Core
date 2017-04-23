package com.coalesce.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public abstract class NMSCore {
    private static NMSCore instance = null;

    public static NMSCore get() {
        return instance;
    }

    public static NMSCore get(@Nullable String anything) { // Just in case kotlin :p
        return get();
    }

    public static void set(NMSCore instance) {
        if (NMSCore.instance != null) {
            throw new IllegalStateException("instance is already set.");
        }
        NMSCore.instance = instance; // how 2 static wit proxi
    }

    public abstract boolean sendActionBar(Player player, String message);

    public abstract boolean sendTabTitles(Player player, String title, String footer);

    public abstract boolean sendJson(Player player, String json);

    public abstract double getPing(Player player);
}
