package me.five.duels.command.impl;

import me.five.duels.FiveDuels;
import me.five.duels.command.BaseCommand;
import me.five.duels.util.StatsMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand extends BaseCommand {

    private FiveDuels plugin;

    public StatsCommand(FiveDuels plugin) {
        super("/stats [player]", true);
        this.plugin = plugin;
    }


    @Override
    public boolean runCommand(CommandSender sender, String[] args) {

        Player player = (Player) sender;
        if (args.length < 1) {

            StatsMenu sm = new StatsMenu(plugin, player);
            sm.openMenu(player);
            return true;

        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return true;
        }

        StatsMenu sm = new StatsMenu(plugin, target);
        sm.openMenu(player);
        return true;

    }
}
