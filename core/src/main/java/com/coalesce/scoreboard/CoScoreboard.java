package com.coalesce.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collection;

/**
 * Provides a general contract for scoreboard display interfaces consisting of String data mapped to Integer scores
 *
 * @param <T> The type of data to be displayed in the Scoreboard.
 */
public interface CoScoreboard<T> {

    /**
     * Sends the {@link Scoreboard} representation of the CoScoreboard to the player
     *
     * @param player The player to send the {@link Scoreboard} to.
     */
    void send(Player player);

    /**
     * Sends the {@link Scoreboard} representation of the CoScoreboard to the provided Collection of players
     *
     * @param players The players to send the {@link Scoreboard} to.
     */
    void send(Collection<Player> players);

    /**
     * Sets the title to the provided element
     *
     * @param titleEntry The entry to set the title to
     */
    void setTitle(T titleEntry);

    /**
     * Adds an entry to the CoScoreboard
     *
     * @param entry The entry to add
     * @param score The score of the entry
     */
    void addEntry(T entry, int score);

    /**
     * Removes an entry from the CoScoreboard.
     * Nothing is done if the entry does not exist.
     *
     * @param entry The entry to remove
     */
    void removeEntry(T entry);

    /**
     * Clears all entries from the CoScoreboard
     */
    void clearEntries();

}
