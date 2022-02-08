package me.five.duels.command.impl;

import me.five.duels.FiveDuels;
import me.five.duels.arena.ArenaData;
import me.five.duels.command.BaseCommand;
import me.five.duels.kit.Kit;
import me.five.duels.util.VectorLocation;
import org.bukkit.Bukkit;
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

        if (args[0].equalsIgnoreCase("create")) {

            if (!sender.hasPermission("duels.create")) {
                printPermissionMessage(sender);
                return true;
            }

            if (args.length < 2) {
                printUsage(sender);
                return true;
            }

            String name = args[1];
            if (plugin.getArenaManager().getArenaData(name) != null) {
                sender.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "A map with that name already exists!");
                return true;
            }
            ArenaData ad = new ArenaData(name, new VectorLocation(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
            ad.saveToConfig(plugin);
            plugin.getArenaManager().createArena(ad);
            sender.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "Map created! Ensure you set your spawn/center locations for the map to function correctly!");
            return true;

        }

        if (args[0].equalsIgnoreCase("joinshield")) {

            plugin.getQueueManager().queuePlayer(player, "Shield");
            return true;

        }

        if (args[0].equalsIgnoreCase("setcenter")) {

            if (!sender.hasPermission("duels.setcenter")) {
                printPermissionMessage(sender);
                return true;
            }

            if (args.length < 2) {
                printUsage(sender);
                return true;
            }

            String name = args[1];
            ArenaData arenaData = plugin.getArenaManager().getArenaData(name);
            if (arenaData == null) {
                sender.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + " Unable to find a map with that name!");
                return true;
            }
            arenaData.setInitialCenter(new VectorLocation(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
            arenaData.saveToConfig(plugin);
            sender.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "Center vector set!");
            return true;

        }

        if (args[0].equalsIgnoreCase("setspawn")) {

            if (!sender.hasPermission("duels.setspawn")) {
                printPermissionMessage(sender);
                return true;
            }

            if (args.length < 3) {
                printUsage(sender);
                return true;
            }

            String name = args[1];
            String locationNumber = args[2];
            int loc;
            try {
                loc = Integer.parseInt(locationNumber);
            } catch (NumberFormatException ex) {
                printUsage(sender);
                return true;
            }
            ArenaData arenaData = plugin.getArenaManager().getArenaData(name);
            if (arenaData == null) {
                sender.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + " Unable to find a map with that name!");
                return true;
            }
            if (loc == 1)
                arenaData.setSpawnLocation1(new VectorLocation(player.getLocation().getX() - arenaData.getInitialCenter().getRelativeX(), player.getLocation().getY() - arenaData.getInitialCenter().getRelativeY(), player.getLocation().getZ() - arenaData.getInitialCenter().getRelativeZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
            if (loc == 2)
                arenaData.setSpawnLocation2(new VectorLocation(player.getLocation().getX() - arenaData.getInitialCenter().getRelativeX(), player.getLocation().getY() - arenaData.getInitialCenter().getRelativeY(), player.getLocation().getZ() - arenaData.getInitialCenter().getRelativeZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
            if (loc != 1 && loc != 2) {
                sender.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "Spawn location number must be 1 or 2!");
                return true;
            }
            arenaData.saveToConfig(plugin);
            sender.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "Spawn location set!");
            return true;

        }

        return true;
    }

    public void printUsage(CommandSender sender) {


        sender.sendMessage("");
        sender.sendMessage(ChatColor.GOLD + "/spectate <player> " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Spectate a player in a duel");
        sender.sendMessage(ChatColor.GOLD + "/leavequeue " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Leave your current duels queue");
        sender.sendMessage(ChatColor.GOLD + "/duel <player> " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Send a duel request to a player");
        sender.sendMessage(ChatColor.GOLD + "/stats <player> " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "View duels stats of a player");
        sender.sendMessage("");

    }

    public void printPermissionMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
    }

}
