package me.five.duels.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class BaseCommand implements CommandExecutor {

    private String usage;
    private String permission;
    private boolean denyConsole;

    public BaseCommand(String usage, String permission, boolean denyConsole) {
        this.usage = usage;
        this.permission = permission;
        this.denyConsole = denyConsole;
    }

    public BaseCommand(String usage, boolean denyConsole) {
        this.usage = usage;
        this.denyConsole = denyConsole;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player) && denyConsole) return true;
        if (permission != null && !sender.hasPermission(permission)) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }
        if (runCommand(sender, args)) return true;
        sender.sendMessage(ChatColor.RED + "Usage: " + usage);
        return true;
    }

    public abstract boolean runCommand(CommandSender sender, String[] args);

}
