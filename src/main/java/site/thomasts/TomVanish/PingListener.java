package site.thomasts.TomVanish;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Iterator;
import java.util.List;

public class PingListener implements Listener {

    private final VanishCommand vanishCommand;

    public PingListener(VanishCommand vanishCommand) {
        this.vanishCommand = vanishCommand;
    }

    @EventHandler
    public void onServerListPing(PaperServerListPingEvent event) {
        List<PaperServerListPingEvent.ListedPlayerInfo> listedPlayers = event.getListedPlayers();

        int hiddenCount = 0;
        Iterator<PaperServerListPingEvent.ListedPlayerInfo> iterator = listedPlayers.iterator();

        while (iterator.hasNext()) {
            PaperServerListPingEvent.ListedPlayerInfo info = iterator.next();


            if (vanishCommand.getVanishedPlayers().contains(info.id())) {
                iterator.remove();
                hiddenCount++;
            }
        }

        event.setNumPlayers(event.getNumPlayers() - hiddenCount);
    }
}