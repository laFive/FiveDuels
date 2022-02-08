package me.five.duels.arena;

import me.five.duels.FiveDuels;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class ArenaManager {

    private Random random;
    private FiveDuels plugin;
    private World arenaWorld;
    private List<Arena> loadedArenas;
    private List<ArenaData> arenaData;


    public ArenaManager(FiveDuels plugin) {

        this.plugin = plugin;
        this.random = new Random();
        this.arenaData = new ArrayList<>();
        this.loadedArenas = new ArrayList<>();
        this.arenaWorld = Bukkit.getWorld("DuelsGames");

    }

    public void loadArenas() {
        File arenaDirectory = new File(plugin.getDataFolder() + "/maps/");
        if (!arenaDirectory.exists()) arenaDirectory.mkdirs();
        for (File arenaFile : arenaDirectory.listFiles()) {
            if (!arenaFile.isFile() || !arenaFile.getName().endsWith(".yml")) continue;
            YamlConfiguration arenaDataConfig = YamlConfiguration.loadConfiguration(arenaFile);
            arenaData.add(new ArenaData(arenaDataConfig));
        }

        int offset = 500;
        for (ArenaData ad : arenaData) {
            if (ad.getInitialCenter() == null || ad.getSpawnLocation1() == null || ad.getSpawnLocation2() == null) continue;
            if (!new File(plugin.getDataFolder() + "/schematics/" + ad.getArenaName() + ".schem").exists()) {
                plugin.getLogger().info(ad.getArenaName());
                continue;
            }
            for (int i = 0; i <= 25; i++) {
                loadedArenas.add(new Arena(ad, offset, plugin));
                offset += 250;
            }
        }
    }

    public Arena getRandomArena() {

        List<Arena> validArenas = loadedArenas.stream().filter(arena -> !arena.isDuelCreated()).toList();
        if (validArenas.isEmpty()) return null;
        return validArenas.get(random.nextInt(validArenas.size()));

    }

    public boolean isInArena(Player player) {

        return loadedArenas.stream().anyMatch(arena -> arena.isDuelCreated() && arena.getPlayers().contains(player));

    }

    public World getArenaWorld() {
        return arenaWorld;
    }

    public List<Arena> getLoadedArenas() {
        return loadedArenas;
    }

    public void createArena(ArenaData ad) {
        arenaData.add(ad);
    }

    public ArenaData getArenaData(String name) {
        return arenaData.stream().filter(a -> a.getArenaName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}
