package me.five.duels.command.impl;

import me.five.duels.FiveDuels;
import me.five.duels.command.BaseCommand;
import me.five.duels.util.DuelRequestMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelCommand extends BaseCommand {

    private FiveDuels plugin;

    public DuelCommand(FiveDuels plugin) {
        super("/duel <player>", true);
        this.plugin = plugin;
    }


    @Override
    public boolean runCommand(CommandSender sender, String[] args) {

        if (args.length < 1) return false;

        Player player = (Player) sender;
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return true;
        }

        if (target.equals(player)) {

            player.sendMessage(ChatColor.RED + "HA noob you can't duel yourself!");
            return true;

        }

        if (plugin.getArenaManager().isInArena(player)) {
            sender.sendMessage(ChatColor.RED + "You are already in a duel!");
            return true;
        }

        if (plugin.getQueueManager().checkDuelRequest(player, target)) return true;

        DuelRequestMenu menu = new DuelRequestMenu(plugin, target);
        menu.openMenu(player);
        return true;
    }
}
