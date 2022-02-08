package me.five.duels.command.impl;

import me.five.duels.FiveDuels;
import me.five.duels.command.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectateCommand extends BaseCommand {

    private FiveDuels plugin;

    public SpectateCommand(FiveDuels plugin) {
        super("/spectate <player>", true);
        this.plugin = plugin;
    }


    @Override
    public boolean runCommand(CommandSender sender, String[] args) {

        Player player = (Player) sender;

        if (args.length < 1) return false;
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return true;
        }
        if (target == player) {
            sender.sendMessage(ChatColor.RED + "You can't spectate yourself you noob!");
            return true;
        }

        boolean done = plugin.getArenaManager().getLoadedArenas().stream().anyMatch(arena -> arena.isDuelCreated() && arena.spectate(player, target));
        if (!done) sender.sendMessage(ChatColor.RED + "That player is not in a duel!");
        return true;
    }
}
