package me.five.duels.util;

import me.five.duels.FiveDuels;
import me.five.itrulyevents.core.menu.Menu;
import me.five.itrulyevents.core.menu.MenuButton;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.function.Consumer;

public class StatsMenu extends Menu {

    private Player target;
    private FiveDuels plugin;

    public StatsMenu(FiveDuels plugin, Player target) {

        super(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Stats: " + target.getName(), 3);
        this.target = target;
        this.plugin = plugin;

        plugin.getDataManager().registerPlayer(target);
        int games = plugin.getDataManager().getGames(target);
        int wins = plugin.getDataManager().getWins(target);

        ItemStack emptySlotItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta emptyMeta = emptySlotItem.getItemMeta();
        emptyMeta.setDisplayName(ChatColor.BLACK.toString());
        emptySlotItem.setItemMeta(emptyMeta);

        for (int i = 0; i < 27; i++) {
            setMenuButton(i, new MenuButton(emptySlotItem));
        }

        MenuButton statsButton = new MenuButton(new ItemStack(Material.PAPER), ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Duels Stats", Arrays.asList("", ChatColor.GRAY + "Games: " + ChatColor.DARK_AQUA + games, ChatColor.GRAY + "Wins: " + ChatColor.DARK_AQUA + wins));
        statsButton.setWhenClicked(new Consumer<Player>() {
            @Override
            public void accept(Player player) {

            }
        });
        setMenuButton(13, statsButton);

    }


}
