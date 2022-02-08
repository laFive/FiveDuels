package me.five.duels.util;

import me.five.duels.FiveDuels;
import me.five.duels.api.DuelsAPI;
import me.five.itrulyevents.core.menu.Menu;
import me.five.itrulyevents.core.menu.MenuButton;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
import java.util.Arrays;
import java.util.function.Consumer;

public class DuelRequestMenu extends Menu {

    private Player target;
    private FiveDuels plugin;

    public DuelRequestMenu(FiveDuels plugin, Player target) {

        super(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Select Kit", 3);
        this.target = target;
        this.plugin = plugin;

        ItemStack emptySlotItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta emptyMeta = emptySlotItem.getItemMeta();
        emptyMeta.setDisplayName(ChatColor.BLACK.toString());
        emptySlotItem.setItemMeta(emptyMeta);

        for (int i = 0; i < 27; i++) {
            setMenuButton(i, new MenuButton(emptySlotItem));
        }

        MenuButton shieldButton = new MenuButton(new ItemStack(Material.SHIELD), ChatColor.GOLD.toString() + ChatColor.BOLD + "Shield Kit Duels " + ChatColor.DARK_GRAY + "(Click)", Arrays.asList("", ChatColor.GRAY + "Click to send request!"));
        shieldButton.setWhenClicked(new Consumer<Player>() {
            @Override
            public void accept(Player player) {
                DuelRequest dr = new DuelRequest(player, target, "Shield");
                plugin.getQueueManager().duelRequest(dr);
                player.closeInventory();
            }
        });
        setMenuButton(10, shieldButton);

        MenuButton swordKit = new MenuButton(new ItemStack(Material.IRON_SWORD), ChatColor.GOLD.toString() + ChatColor.BOLD + "Sword Kit Duels " + ChatColor.DARK_GRAY + "(Click)", Arrays.asList("", ChatColor.GRAY + "Click to send request!"));
        swordKit.setWhenClicked(new Consumer<Player>() {
            @Override
            public void accept(Player player) {
                DuelRequest dr = new DuelRequest(player, target, "Sword");
                plugin.getQueueManager().duelRequest(dr);
                player.closeInventory();
            }
        });
        setMenuButton(12, swordKit);

        MenuButton movementKit = new MenuButton(new ItemStack(Material.FEATHER), ChatColor.GOLD.toString() + ChatColor.BOLD + "Movement Kit Duels " + ChatColor.DARK_GRAY + "(Click)", Arrays.asList("", ChatColor.GRAY + "Click to send request!"));
        movementKit.setWhenClicked(new Consumer<Player>() {
            @Override
            public void accept(Player player) {
                DuelRequest dr = new DuelRequest(player, target, "Movement");
                plugin.getQueueManager().duelRequest(dr);
                player.closeInventory();
            }
        });
        setMenuButton(14, movementKit);

        MenuButton uhcKit = new MenuButton(new ItemStack(Material.GOLDEN_APPLE), ChatColor.GOLD.toString() + ChatColor.BOLD + "UHC Kit Duels " + ChatColor.DARK_GRAY + "(Click)", Arrays.asList("", ChatColor.GRAY + "Click to send request!"));
        uhcKit.setWhenClicked(new Consumer<Player>() {
            @Override
            public void accept(Player player) {
                DuelRequest dr = new DuelRequest(player, target, "UHC");
                plugin.getQueueManager().duelRequest(dr);
                player.closeInventory();
            }
        });
        setMenuButton(16, uhcKit);

    }


}
