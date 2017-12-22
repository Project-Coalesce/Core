package com.coalesce.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class PlayerScoreboard implements CoScoreboard<Function<Player, String>> {

    private Function<Player, String> title;
    private final Map<Function<Player, String>, Integer> entries;

    private PlayerScoreboard(Builder builder) {

        title = builder.title;
        entries = builder.entries;
    }

    @Override
    public void send(Player player) {

        Scoreboard scoreboard = generateScoreboard(player);
        player.setScoreboard(scoreboard);
    }

    @Override
    public void send(Collection<Player> players) {
        players.forEach(this::send);
    }

    private Scoreboard generateScoreboard(Player player) {

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        String titleString = title.apply(player);

        Objective scoreboardObjective = scoreboard.registerNewObjective(titleString, "dummy");
        scoreboardObjective.setDisplayName(titleString);
        scoreboardObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

        entries.forEach((playerStringFunction, score) -> {

            String entryString = playerStringFunction.apply(player);
            scoreboardObjective.getScore(entryString).setScore(score);

        });

        return scoreboard;
    }

    @Override
    public void setTitle(Function<Player, String> titleEntry) {
        this.title = titleEntry;
    }

    @Override
    public void addEntry(Function<Player, String> entry, int score) {

        entries.put(entry, score);
    }

    public void addEntry(Function<Player, String> entry) {

        addEntry(entry, MAX_ENTRIES - entries.size());
    }

    public void addEntries(Collection<Function<Player, String>> entries) {

        entries.forEach(this::addEntry);
    }

    @Override
    public void removeEntry(Function<Player, String> entry) {
        entries.remove(entry);
    }

    @Override
    public void clearEntries() {
        entries.clear();
    }

    public static class Builder {

        private Function<Player, String> title;
        private Map<Function<Player, String>, Integer> entries;

        public Builder() {
            this.title = (player -> "");

            entries = new HashMap<>(MAX_ENTRIES);
        }

        public Builder title(Function<Player, String> title) {
            this.title = title;

            return this;
        }

        public Builder title(String title) {
            this.title = (player -> title);

            return this;
        }

        public Builder addEntry(Function<Player, String> entry) {

            entries.put(entry, MAX_ENTRIES - entries.size());
            return this;
        }

        public Builder addEntries(Function<Player, String>... entries) {

            Stream.of(entries).forEach(this::addEntry);

            return this;
        }

        public Builder addEntry(Function<Player, String> entry, int score) {

            entries.put(entry, score);
            return this;
        }

        public PlayerScoreboard build() {
            return new PlayerScoreboard(this);
        }
    }

}
