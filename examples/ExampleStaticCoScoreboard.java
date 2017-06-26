package examples;

import com.coalesce.plugin.CoPlugin;
import com.coalesce.scoreboard.StaticScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ExampleStaticCoScoreboard implements Listener {

    final StaticScoreboard scoreboard;

    public ExampleStaticCoScoreboard(CoPlugin coPlugin) {
        // Create scoreboard
        scoreboard = new StaticScoreboard.Builder().build();

        // Add all players that are currently on the server
        Bukkit.getOnlinePlayers().forEach(scoreboard::send);

        // Create a bukkit runnable to update scoreboards
        new BukkitRunnable() {
            @Override
            public void run() {
                // Clear all the entries
                scoreboard.clearEntries();

                // Re-add the entries
                scoreboard.addEntry("§bIP:");
                scoreboard.addEntry("§7server.craft.com");
                scoreboard.addEntry("");
                scoreboard.addEntry("§bWebsite:");
                scoreboard.addEntry("§7www.craft.com");
                scoreboard.addEntry("");
                scoreboard.addEntry("§bPlayers Online:");
                scoreboard.addEntry("§7" + Bukkit.getOnlinePlayers().size());
                scoreboard.addEntry("§m§e------------------");

                // Update the scoreboard
                scoreboard.update();
            }
        }.runTaskTimer(coPlugin, 0L, 3L); // Set timer
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        scoreboard.send(event.getPlayer());
    }
}
