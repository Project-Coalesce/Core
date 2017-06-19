package examples;

import com.coalesce.scoreboard.StaticScoreboard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ExampleStaticCoScoreboard implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        StaticScoreboard scoreboard = new StaticScoreboard.Builder()
                .title("§m§e--§e Server Craft §m§e--")
                .addEntries("§bIP:",
                        "§7server.craft.com",
                        "",
                        "§bWebsite:",
                        "§7www.craft.com",
                        "",
                        "§m§e------------------")
                .send(event.getPlayer());
    }
}
