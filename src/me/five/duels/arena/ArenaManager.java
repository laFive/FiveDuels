package me.five.duels.arena;

import me.five.duels.FiveDuels;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ArenaManager {

    private FiveDuels plugin;
    private World arenaWorld;
    private List<Arena> loadedArenas;
    private List<ArenaData> arenaData;


    public ArenaManager(FiveDuels plugin) {

        this.plugin = plugin;
        this.arenaData = new ArrayList<>();
        this.loadedArenas = new ArrayList<>();
        this.arenaWorld = Bukkit.getWorld("DuelsGames") == null ? Bukkit.getWorlds().get(0) : Bukkit.getWorld("DuelsGames");

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
            if (ad.getInitialCenter() == null || ad.getSpawnLocation1() == null || ad.getSpawnLocation2() == null) {
                if (ad.getInitialCenter() == null) {
                    plugin.getLogger().info("trol retge ");
                }
                if (ad.getSpawnLocation1() == null) {
                    plugin.getLogger().info("trol fweqfwq ");
                }
                if (ad.getSpawnLocation2() == null) {
                    plugin.getLogger().info("fefe retge ");
                }
                continue;
            }
            if (!new File(plugin.getDataFolder() + "/schematics/" + ad.getArenaName() + ".schem").exists()) {
                plugin.getLogger().info("hahahahahahahahah " + ad.getArenaName());
                plugin.getLogger().info(ad.getArenaName());
                continue;
            }
            for (int i = 0; i <= 50; i++) {
                loadedArenas.add(new Arena(ad, offset, plugin));
                offset += 250;
            }
        }
    }

    public World getArenaWorld() {
        return arenaWorld;
    }

    public void createArena(ArenaData ad) {
        arenaData.add(ad);
    }

    public ArenaData getArenaData(String name) {
        return arenaData.stream().filter(a -> a.getArenaName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}
