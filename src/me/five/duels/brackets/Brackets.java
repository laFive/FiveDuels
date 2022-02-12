package me.five.duels.brackets;

import me.five.duels.FiveDuels;
import me.five.itrulyevents.core.ITrulyCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Brackets {

    private boolean started;
    private boolean commenced;
    private Player host;
    private int maxPlayers;
    private List<Player> participants;
    private List<Player> spectators;
    private FiveDuels fiveDuels;
    private boolean build;

    public Brackets(int maxPlayers, Player host, boolean building, FiveDuels plugin) {
        this.fiveDuels = plugin;
        this.maxPlayers = maxPlayers;
        this.host = host;
        this.build = building;
    }

    public void forceEnd() {
        Stream.concat(participants.stream(), spectators.stream()).collect(Collectors.toList()).forEach(p -> {
            p.setGameMode(GameMode.SURVIVAL);
            p.teleport(ITrulyCore.getInstance().getPluginConfig().getSpawnLocation());
            p.setAllowFlight(false);
            p.setFlying(false);
            p.closeInventory();
            p.sendMessage(ChatColor.DARK_AQUA + "[!] " + ChatColor.GRAY + "The brackets event was forcefully ended!");
            Bukkit.getOnlinePlayers().forEach(op -> {
                op.showPlayer(fiveDuels, p);
            });
        });
        fiveDuels.getArenaManager().getLoadedArenas().forEach(a -> a.bracketsEnd());
    }

    public boolean isBuild() {
        return build;
    }
}
