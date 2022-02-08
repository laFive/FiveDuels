package me.five.duels.api;

import me.five.duels.FiveDuels;
import me.five.duels.data.DataManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DuelsAPI {

    private static FiveDuels plugin;

    public static void setPlugin(FiveDuels plugin) {
        DuelsAPI.plugin = plugin;
    }

    public static DataManager getDataManager() {
        return plugin.getDataManager();
    }

    public static void addToQueue(Player player, String kit) {

        plugin.getQueueManager().queuePlayer(player, kit);
        player.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "You are now in the queue for " + kit + " duels");
        player.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "Leave the queue by typing /leavequeue");
        player.closeInventory();

    }

    public static int getQueueSize(String queue) {

        return plugin.getQueueManager().getQueueCount(queue);

    }

}
