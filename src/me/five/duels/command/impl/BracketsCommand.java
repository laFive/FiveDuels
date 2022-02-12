package me.five.duels.command.impl;

import me.five.duels.FiveDuels;
import me.five.duels.command.BaseCommand;
import me.five.duels.util.MenuUtil;
import me.five.itrulyevents.core.menu.Menu;
import me.five.itrulyevents.core.menu.MenuButton;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class BracketsCommand extends BaseCommand {

    private FiveDuels plugin;

    public BracketsCommand(FiveDuels plugin) {
        super("/brackets", "duels.brackets", true);
        this.plugin = plugin;
    }


    @Override
    public boolean runCommand(CommandSender sender, String[] args) {

        if (args.length > 0 && args[0].equalsIgnoreCase("end")) {
            if (!plugin.getBracketsManager().isBracketsCreated()) return true;
            plugin.getBracketsManager().cancelBrackets();
            sender.sendMessage(ChatColor.GRAY + "Brackets event ended!");
            return true;
        }

        if (plugin.getBracketsManager().isBracketsCreated()) {
            sender.sendMessage(ChatColor.RED + "A brackets event is already running! End it with /brackets end first");
            return true;
        }

        MenuUtil.openPlayerLimitMenu((Player)sender);
        return true;
    }
}
