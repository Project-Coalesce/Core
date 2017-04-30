package com.coalesce.scoreboard;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class CoScoreboard {
    private Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    @Getter private String title;
    private Map<String, Integer> scores;
    private List<Team> teams;

    /**
     * Create scoreboard builder.
     *
     * @param title Title of scoreboard, shown at top of board.
     */
    public CoScoreboard(String title) {
        this.title = title;
        this.scores = Maps.newLinkedHashMap();
        this.teams = Lists.newArrayList();
    }

    public void blankLine() {
        this.add(" ");
    }

    /**
     * Add a line to the scoreboard.
     *
     * @param text String of line
     */
    public void add(String text) {
        this.add(text, null);
    }

    /**
     * Add a line to the scoreboard with specific score.
     *
     * @param text  String of line
     * @param score Score of line
     */
    public void add(String text, Integer score) {
        Preconditions.checkArgument((boolean) (text.length() < 48),
                (Object) "text cannot be over 48 characters in length");
        text = this.fixDuplicates(text);
        this.scores.put(text, score);
    }

    private String fixDuplicates(String text) {
        while (this.scores.containsKey(text)) {
            text = text + "\u00a7r";
        }
        if (text.length() > 48) {
            text = text.substring(0, 47);
        }
        return text;
    }

    private SimpleEntry<Team, String> createTeam(String text) {
        String result = "";
        if (text.length() <= 16) {
            return new AbstractMap.SimpleEntry<Team, String>(null, text);
        }
        Team team = this.scoreboard.registerNewTeam("text-" + this.scoreboard.getTeams().size());
        Iterator<String> iterator = Splitter.fixedLength((int) 16).split((CharSequence) text).iterator();
        team.setPrefix((String) iterator.next());
        result = (String) iterator.next();
        if (text.length() > 32) {
            team.setSuffix((String) iterator.next());
        }
        this.teams.add(team);
        return new AbstractMap.SimpleEntry<Team, String>(team, result);
    }

    /**
     * Builds the scoreboard into a scoreboard object allowing it to be sent to players.
     *
     * @see CoScoreboard#getScoreboard()
     */
    public void build() {
        Objective obj = this.scoreboard
                .registerNewObjective(this.title.length() > 16 ? this.title.substring(0, 15) : this.title, "dummy");
        obj.setDisplayName(this.title);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        int index = this.scores.size();
        for (Map.Entry<String, Integer> text : this.scores.entrySet()) {
            Map.Entry<Team, String> team = this.createTeam(text.getKey());
            Integer score = text.getValue() != null ? text.getValue() : index;
            String player = (String) team.getValue();
            if (team.getKey() != null) {
                team.getKey().addEntry(player);
            }
            obj.getScore(player).setScore(score.intValue());
            --index;
        }
    }

    /**
     * Clear the scoreboard by removing all lines and setting the title to null.
     */
    public void reset() {
        this.title = null;
        this.scores.clear();
        for (Team t : this.teams) {
            t.unregister();
        }
        this.teams.clear();
    }

    /**
     * Get Bukkit scoreboard
     *
     * @return scoreboard object
     * @see org.bukkit.scoreboard.Scoreboard
     */
    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    /**
     * Sends scoreboard to players, allowing them to see it on the side of their screen.
     *
     * @param players List of players to send to
     */
    public /* varargs */ void send(Player... players) {
        for (Player p : players) {
            p.setScoreboard(this.scoreboard);
        }
    }
}
