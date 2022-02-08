package me.five.duels.command.impl;

import me.five.duels.FiveDuels;
import me.five.duels.command.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveQueueCommand extends BaseCommand {

    private FiveDuels plugin;

    public LeaveQueueCommand(FiveDuels plugin) {
        super("/leavequeue", true);
        this.plugin = plugin;
    }


    @Override
    public boolean runCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        plugin.getQueueManager().unQueuePlayer(player);
        return true;
    }
}
