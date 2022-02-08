package me.five.duels.util;

import me.five.duels.FiveDuels;
import org.bukkit.Bukkit;

public class ArenaTickTask implements Runnable {

    private FiveDuels plugin;

    public ArenaTickTask(FiveDuels plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.getArenaManager().getLoadedArenas().forEach(arena -> arena.tick());
    }

    public void schedule() {
        Bukkit.getScheduler().runTaskTimer(plugin, this, 1L, 1L);
    }

}
