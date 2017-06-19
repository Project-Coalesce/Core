package examples;

import com.coalesce.plugin.CoPlugin;
import com.coalesce.scoreboard.PlayerScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ExamplePlayerCoScoreboard implements Listener {

    private final CoPlugin coPlugin;

    public ExamplePlayerCoScoreboard(CoPlugin coPlugin) {
        this.coPlugin = coPlugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                final PlayerScoreboard scoreboard = new PlayerScoreboard.Builder()
                        .title(ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH + "--" +
                                ChatColor.YELLOW + " Server Craft " +
                                ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH + "--")
                        .addEntries(player -> ChatColor.AQUA + "IP:",
                                player -> ChatColor.GRAY + "server.craft.com",
                                player -> "", player -> "",
                                player -> ChatColor.AQUA + "Website:",
                                player -> ChatColor.GRAY + "www.craft.com",
                                player -> "",
                                player -> ChatColor.AQUA + "Players Online:",
                                player -> ChatColor.GRAY + "" + Bukkit.getOnlinePlayers().size(),
                                player -> "",
                                player -> ChatColor.AQUA + "World Time:",
                                player -> ChatColor.GRAY + "" + event.getPlayer().getWorld().getTime(),
                                player -> ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH + "------------------")
                        .build();
                scoreboard.send(event.getPlayer());
            }
        }.runTaskTimer(coPlugin, 0L, 3L);
    }
}
