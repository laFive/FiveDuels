package me.five.duels.command.impl;

import me.five.duels.FiveDuels;
import me.five.duels.command.BaseCommand;
import me.five.duels.kit.Kit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelsCommand extends BaseCommand {

    private FiveDuels plugin;

    public DuelsCommand(FiveDuels plugin) {
        super("/duels", true);
        this.plugin = plugin;
    }

    @Override
    public boolean runCommand(CommandSender sender, String[] args) {

        Player player = (Player) sender;
        if (args.length < 1) {
            printUsage(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("setkit")) {

            if (!sender.hasPermission("duels.setkit")) {
                printPermissionMessage(sender);
                return true;
            }

            if (args.length < 2) {
                printUsage(sender);
                return true;
            }

            String name = args[1];
            Kit kit = new Kit(player);
            plugin.getKitManager().addKit(name, kit);
            sender.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "Kit " + name + " set!");
            return true;

        }

        if (args[0].equalsIgnoreCase("getkit")) {

            if (!sender.hasPermission("duels.getkit")) {
                printPermissionMessage(sender);
                return true;
            }

            if (args.length < 2) {
                printUsage(sender);
                return true;
            }

            String name = args[1];
            Kit kit = plugin.getKitManager().getKit(name);
            if (kit == null) {
                sender.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "Kit not found!");
                return true;
            }
            kit.applyToPlayer(player);
            sender.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "Kit " + name + " applied!");
            return true;

        }

        return true;
    }

    public void printUsage(CommandSender sender) {



    }

    public void printPermissionMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
    }

}
