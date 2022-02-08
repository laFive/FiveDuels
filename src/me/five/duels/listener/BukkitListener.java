package me.five.duels.listener;

import me.five.duels.FiveDuels;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public class BukkitListener implements Listener {

    private FiveDuels plugin;

    public BukkitListener(FiveDuels plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {

        plugin.getArenaManager().getLoadedArenas().forEach(arena -> arena.onEvent(e));

        plugin.getQueueManager().unQueuePlayer(e.getPlayer());

    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {

        if (e.getEntity() instanceof Player) {

            Player player = (Player) e.getEntity();
            if (player.getWorld().equals(plugin.getArenaManager().getArenaWorld())) {
                e.setCancelled(true);
            }

        }

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        plugin.getDataManager().registerPlayer(e.getPlayer());

    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        plugin.getArenaManager().getLoadedArenas().forEach(arena -> arena.onEvent(e));
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        plugin.getArenaManager().getLoadedArenas().forEach(arena -> arena.onEvent(e));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        plugin.getArenaManager().getLoadedArenas().forEach(arena -> arena.onEvent(e));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        plugin.getArenaManager().getLoadedArenas().forEach(arena -> arena.onEvent(e));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        plugin.getArenaManager().getLoadedArenas().forEach(arena -> arena.onEvent(e));
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {

        plugin.getArenaManager().getLoadedArenas().forEach(arena -> arena.onEvent(e));

        if (!e.getTo().getWorld().equals(Bukkit.getWorlds().get(0))) {
            plugin.getQueueManager().unQueuePlayer(e.getPlayer());
        }

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        plugin.getArenaManager().getLoadedArenas().forEach(arena -> arena.onEvent(e));
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        plugin.getArenaManager().getLoadedArenas().forEach(arena -> arena.onEvent(e));
    }

}
