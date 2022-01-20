package me.five.duels.arena;

import me.five.duels.FiveDuels;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ArenaManager {

    private FiveDuels plugin;
    private List<ArenaData> arenaData;

    public ArenaManager(FiveDuels plugin) {

        this.plugin = plugin;
        this.arenaData = new ArrayList<>();
        File arenaDirectory = new File(plugin.getDataFolder() + "/arenas/");
        if (!arenaDirectory.exists()) arenaDirectory.mkdirs();
        for (File arenaFile : arenaDirectory.listFiles()) {
            if (!arenaFile.isFile() || !arenaFile.getName().endsWith(".yml")) continue;
            YamlConfiguration arenaDataConfig = YamlConfiguration.loadConfiguration(arenaFile);
            arenaData.add(new ArenaData(arenaDataConfig));
        }

    }

}
