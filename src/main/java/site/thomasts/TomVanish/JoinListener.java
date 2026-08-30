package site.thomasts.TomVanish;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class JoinListener implements Listener {

    private final TomVanish plugin;
    private final VanishCommand vanishCommand;

    public JoinListener(TomVanish plugin, VanishCommand vanishCommand) {
        this.plugin = plugin;
        this.vanishCommand = vanishCommand;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player joiningPlayer = event.getPlayer();

        if (!joiningPlayer.hasPermission("vanish.see")) {
            for (UUID vanishedUUID : vanishCommand.getVanishedPlayers()) {
                Player vanishedPlayer = plugin.getServer().getPlayer(vanishedUUID);
                if (vanishedPlayer != null) {
                    joiningPlayer.hidePlayer(plugin, vanishedPlayer);
                }
            }
        }
    }
}