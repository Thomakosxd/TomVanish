package site.thomasts.TomVanish;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VanishCommand implements CommandExecutor {

    private final TomVanish plugin;
    private final Set<UUID> vanishedPlayers = new HashSet<>();
    private final MiniMessage mm = MiniMessage.miniMessage();

    public VanishCommand(TomVanish plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        boolean isSilent = command.getName().equalsIgnoreCase("silentvanish");

        if (args.length > 0) {
            if (!sender.hasPermission("vanish.others")) {
                sender.sendMessage(mm.deserialize("<red>You don't have permission to vanish other players.</red>"));
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(mm.deserialize("<red>Player not found.</red>"));
                return true;
            }

            toggleVanish(target, isSilent);

            boolean targetIsVanished = isVanished(target);
            String status = targetIsVanished ? "<green>vanished</green>" : "<yellow>unvanished</yellow>";
            String silentText = isSilent ? " <gray>(silently)</gray>" : "";

            sender.sendMessage(mm.deserialize("<gray>You have " + status + " <white>" + target.getName() + "</white>" + silentText + ".</gray>"));
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(mm.deserialize("<red>Only players can execute this command on themselves!</red>"));
            return true;
        }

        if (!player.hasPermission("vanish.use")) {
            player.sendMessage(mm.deserialize("<red>You don't have permission to use vanish.</red>"));
            return true;
        }
         if (isSilent && !player.hasPermission("vanish.silent")) {
             player.sendMessage(mm.deserialize("<red>You don't have permission to use silent vanish.</red>"));
             return true;
         }

        toggleVanish(player, isSilent);
        return true;
    }

    public void toggleVanish(Player player, boolean silent) {
        UUID uuid = player.getUniqueId();

        if (vanishedPlayers.contains(uuid)) {
            vanishedPlayers.remove(uuid);

            for (Player target : Bukkit.getOnlinePlayers()) {
                target.showPlayer(plugin, player);
            }

            player.sendMessage(mm.deserialize("<green>You are now visible!</green>"));

            if (!silent) {
                Component fakeJoin = mm.deserialize("<yellow>" + player.getName() + " joined the game</yellow>");
                for (Player target : Bukkit.getOnlinePlayers()) {
                    if (!target.hasPermission("vanish.see") && !target.equals(player)) {
                        target.sendMessage(fakeJoin);
                    }
                }
            }

        } else {
            vanishedPlayers.add(uuid);

            for (Player target : Bukkit.getOnlinePlayers()) {
                if (!target.hasPermission("vanish.see") && !target.equals(player)) {
                    target.hidePlayer(plugin, player);
                }
            }

            player.sendMessage(mm.deserialize("<yellow>You are now vanished!</yellow>"));

            if (!silent) {
                Component fakeQuit = mm.deserialize("<yellow>" + player.getName() + " left the game</yellow>");
                for (Player target : Bukkit.getOnlinePlayers()) {
                    if (!target.hasPermission("vanish.see") && !target.equals(player)) {
                        target.sendMessage(fakeQuit);
                    }
                }
            }
        }
    }

    public void toggleVanish(Player player) {
        toggleVanish(player, false);
    }

    public boolean isVanished(Player player) {
        return vanishedPlayers.contains(player.getUniqueId());
    }

    public Set<UUID> getVanishedPlayers() {
        return vanishedPlayers;
    }
}