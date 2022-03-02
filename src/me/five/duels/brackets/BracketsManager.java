package me.five.duels.brackets;

import me.five.duels.FiveDuels;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BracketsManager {

    private Brackets brackets;
    private FiveDuels fiveDuels;

    public BracketsManager(FiveDuels plugin) {
        this.fiveDuels = plugin;
    }

    public boolean isBracketsCreated() {
        return brackets != null;
    }

    public void createBrackets(Player host, int playerLimit, boolean build) {
        brackets = new Brackets(playerLimit, host, build, fiveDuels);
        Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-------------------------------------------");
        Bukkit.broadcastMessage(ChatColor.DARK_AQUA + host.getName() + ChatColor.GRAY + " is hosting a duels brackets event!");
        Bukkit.broadcastMessage(ChatColor.GRAY + "Click " + ChatColor.DARK_AQUA + " here " + ChatColor.GRAY + "to join!");
        Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-------------------------------------------");
    }

    public void createBrackets(Player player) {

    }

    public void cancelBrackets() {
        brackets = null;
    }

}
