package me.five.duels.util;

import me.five.duels.FiveDuels;
import me.five.itrulyevents.core.menu.Menu;
import me.five.itrulyevents.core.menu.MenuButton;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class MenuUtil {

    public static void openPlayerLimitMenu(Player player, FiveDuels plugin) {
        Menu playerLimitMenu = new Menu(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Select player limit", 4);
        for (int i = 0; i < 36; i++) {
            playerLimitMenu.setMenuButton(i, new MenuButton(new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE), ChatColor.BLUE.toString()));
        }
        MenuButton eightButton = new MenuButton(new ItemStack(Material.BOOK), ChatColor.DARK_AQUA + "8 Players " + ChatColor.GRAY + "(Click)");
        eightButton.setWhenClicked(new Consumer<Player>() {
            @Override
            public void accept(Player player) {
                openBuildingMenu(player, 8, plugin);
                return;
            }
        });
        MenuButton sixteenButton = new MenuButton(new ItemStack(Material.BOOK), ChatColor.DARK_AQUA + "16 Players " + ChatColor.GRAY + "(Click)");
        sixteenButton.setWhenClicked(new Consumer<Player>() {
            @Override
            public void accept(Player player) {
                openBuildingMenu(player, 16, plugin);
                return;
            }
        });
        MenuButton thirtyTwoButton = new MenuButton(new ItemStack(Material.BOOK), ChatColor.DARK_AQUA + "32 Players " + ChatColor.GRAY + "(Click)");
        thirtyTwoButton.setWhenClicked(new Consumer<Player>() {
            @Override
            public void accept(Player player) {
                openBuildingMenu(player, 32, plugin);
                return;
            }
        });
        MenuButton noLimitButton = new MenuButton(new ItemStack(Material.BOOK), ChatColor.DARK_AQUA + "No Player Limit " + ChatColor.GRAY + "(Click)");
        noLimitButton.setWhenClicked(new Consumer<Player>() {
            @Override
            public void accept(Player player) {
                openBuildingMenu(player, Integer.MAX_VALUE, plugin);
                return;
            }
        });
        MenuButton cancelButton = new MenuButton(new ItemStack(Material.BARRIER), ChatColor.RED + "Cancel " + ChatColor.GRAY + "(Click)");
        cancelButton.setWhenClicked(new Consumer<Player>() {
            @Override
            public void accept(Player player) {
                player.closeInventory();
                return;
            }
        });
        playerLimitMenu.setMenuButton(10, eightButton);
        playerLimitMenu.setMenuButton(12, sixteenButton);
        playerLimitMenu.setMenuButton(14, thirtyTwoButton);
        playerLimitMenu.setMenuButton(16, noLimitButton);
        playerLimitMenu.setMenuButton(31, cancelButton);
        playerLimitMenu.openMenu(player);
    }

    public static void openBuildingMenu(Player player, int playerLimit, FiveDuels plugin) {

        Menu allowBuilding = new Menu(ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "Allow Building", 4);
        for (int i = 0; i < 36; i++) {
            allowBuilding.setMenuButton(i, new MenuButton(new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE), ChatColor.BLUE.toString()));
        }
        MenuButton denyButton = new MenuButton(new ItemStack(Material.RED_WOOL), ChatColor.DARK_AQUA + "No Building " + ChatColor.GRAY + "(Click)");
        denyButton.setWhenClicked(new Consumer<Player>() {
            @Override
            public void accept(Player player) {
                plugin.getBracketsManager().createBrackets(player, playerLimit, false);
                return;
            }
        });
        MenuButton allowButton = new MenuButton(new ItemStack(Material.LIME_WOOL), ChatColor.DARK_AQUA + "Allow Building " + ChatColor.GRAY + "(Click)");
        allowButton.setWhenClicked(new Consumer<Player>() {
            @Override
            public void accept(Player player) {
                plugin.getBracketsManager().createBrackets(player, playerLimit, true);
                return;
            }
        });
        MenuButton cancelButton = new MenuButton(new ItemStack(Material.BARRIER), ChatColor.RED + "Cancel " + ChatColor.GRAY + "(Click)");
        cancelButton.setWhenClicked(new Consumer<Player>() {
            @Override
            public void accept(Player player) {
                player.closeInventory();
                return;
            }
        });
        allowBuilding.setMenuButton(11, denyButton);
        allowBuilding.setMenuButton(15, allowButton);
        allowBuilding.setMenuButton(31, cancelButton);
        allowBuilding.openMenu(player);

    }

}
