package com.coalesce.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class PlayerScoreboard implements CoScoreboard<Function<Player, String>>{

    private final Scoreboard scoreboard;
    private Function<Player, String> title;
    private final Map<Function<Player, String>, Integer> entries;

    //Constants
    private static final int MAX_ENTRIES = 16;

    private PlayerScoreboard(Builder builder){

        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        title = builder.title;
        entries = builder.entries;
    }

    @Override
    public void send(Player player){

        player.setScoreboard(scoreboard);
    }

    @Override
    public void send(Collection<Player> players) {
        players.forEach(this::send);
    }

    @Override
    public void setTitle(Function<Player, String> titleEntry) {
        this.title = titleEntry;
    }

    @Override
    public void addEntry(Function<Player, String> entry, int score){

        entries.put(entry, score);
    }

    public void addEntry(Function<Player, String> entry){

        addEntry(entry,MAX_ENTRIES - entries.size());
    }

    public void addEntries(Collection<Function<Player, String>> entries){

        entries.forEach(this::addEntry);
    }

    @Override
    public void removeEntry(Function<Player, String> entry) {
        entries.remove(entry);
    }

    @Override
    public void clearEntries(){
        entries.clear();
    }

    public static class Builder {

        private Function<Player, String> title;
        private Map<Function<Player, String>, Integer> entries;

        public Builder(Function<Player, String> title){
            this.title = title;

            entries = new HashMap<>(MAX_ENTRIES);
        }

        public Builder(String title){
            this.title = (player -> title);

            entries = new HashMap<>(MAX_ENTRIES);
        }

        public Builder addEntry(Function<Player, String> entry){

            entries.put(entry, MAX_ENTRIES - entries.size());
            return this;
        }

        public Builder addEntries(Function<Player, String>... entries){

            Stream.of(entries).forEach(this::addEntry);

            return this;
        }

        public Builder addEntry(Function<Player, String> entry, int score){

            entries.put(entry, score);
            return this;
        }

        public PlayerScoreboard build(){
            return new PlayerScoreboard(this);
        }
    }

}
