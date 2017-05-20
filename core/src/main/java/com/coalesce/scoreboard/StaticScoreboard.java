package com.coalesce.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class StaticScoreboard implements CoScoreboard<String>{

    private final Scoreboard scoreboard;
    private Objective scoreboardObjective;
    private String title;
    private final Map<String, Integer> entries;

    private StaticScoreboard(Builder builder){

        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        title = builder.title;
        entries = builder.entries;

        update();
    }

    public void update(){

        if (scoreboardObjective != null){
            scoreboardObjective.unregister();
        }

        //Update the objective
        scoreboardObjective = scoreboard.registerNewObjective(title, "dummy");
        scoreboardObjective.setDisplayName(this.title);
        scoreboardObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

        //Sets the data to the scoreboard
        entries.forEach((entry, score) -> {

            scoreboardObjective.getScore(entry).setScore(score);
        });
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
    public void setTitle(String titleEntry) {
        this.title = titleEntry;
    }

    @Override
    public void addEntry(String message, int score){

        while (entries.containsKey(message)){
            message = message + ChatColor.RESET;
        }

        entries.put(message, score);
    }

    public void addEntry(String message){

        addEntry(message, MAX_ENTRIES - entries.size());
    }

    public void addEntries(Collection<String> messages){

        messages.forEach(this::addEntry);
    }

    @Override
    public void removeEntry(String entry) {
        entries.remove(entry);
    }

    @Override
    public void clearEntries(){
        entries.clear();
    }

    public static class Builder {

        private String title;
        private Map<String, Integer> entries;

        public Builder(){

            this.title = "";
            entries = new HashMap<>(MAX_ENTRIES);
        }

        public Builder title(String title){

            this.title = title;

            return this;
        }

        public Builder addEntry(String message){

            while (entries.containsKey(message)){
                message = message + ChatColor.RESET;
            }

            entries.put(message, MAX_ENTRIES - entries.size());

            return this;
        }

        public Builder addEntries(String... messages){

            Stream.of(messages).forEach(this::addEntry);

            return this;
        }

        public Builder addEntry(String message, int score){

            while (entries.containsKey(message)){
                message = message + ChatColor.RESET;
            }

            entries.put(message, score);

            return this;
        }

        public StaticScoreboard build(){
            return new StaticScoreboard(this);
        }
    }

}
