package site.thomasts.TomVanish;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class TomVanish extends JavaPlugin {

    private VanishCommand vanishCommand;

    @Override
    public void onEnable() {
        int pluginId = 33746;
        Metrics metrics = new Metrics(this, pluginId);

        this.vanishCommand = new VanishCommand(this);

        if (getCommand("vanish") != null) {
            Objects.requireNonNull(getCommand("vanish")).setExecutor(vanishCommand);
        }

        if (getCommand("silentvanish") != null) {
            Objects.requireNonNull(getCommand("silentvanish")).setExecutor(vanishCommand);
        }

        getServer().getPluginManager().registerEvents(new JoinListener(this, vanishCommand), this);

        getServer().getPluginManager().registerEvents(new PingListener(vanishCommand), this);

        getLogger().info("TomVanish enabled successfully!");
    }

    @Override
    public void onDisable() {
        getLogger().info("TomVanish disabled!");
    }

    public VanishCommand getVanishCommand() {
        return vanishCommand;
    }
}