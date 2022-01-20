package me.five.duels.arena;

import me.five.duels.FiveDuels;
import me.five.duels.util.RelativeLocation;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ArenaData {

    private String arenaName;
    private boolean allowBuilding;
    private RelativeLocation initialCenter;
    private RelativeLocation spawnLocation1;
    private RelativeLocation spawnLocation2;

    public ArenaData(String arenaName, RelativeLocation initialCenter) {
        this.arenaName = arenaName;
        this.initialCenter = initialCenter;
        this.allowBuilding = true;
    }

    public ArenaData(YamlConfiguration arenaConfig) {
        this.arenaName = arenaConfig.getString("map-name");
        this.allowBuilding = arenaConfig.getBoolean("AllowBuilding");
        this.initialCenter = new RelativeLocation(arenaConfig.getConfigurationSection("Center-Vector"));
        if (arenaConfig.getConfigurationSection("Spawn-Location-1") != null) this.spawnLocation1 = new RelativeLocation(arenaConfig.getConfigurationSection("Spawn-Location-1"));
        if (arenaConfig.getConfigurationSection("Spawn-Location-2") != null) this.spawnLocation2 = new RelativeLocation(arenaConfig.getConfigurationSection("Spawn-Location-2"));
    }

    public void saveToConfig(FiveDuels plugin) {

        File targetFile = new File(plugin.getDataFolder() + "/maps/" + arenaName + ".yml");
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set("map-name", arenaName);
        yaml.set("AllowBuilding", allowBuilding);
        initialCenter.saveToConfig(yaml.createSection("Center-Vector"));
        if (spawnLocation1 != null) spawnLocation1.saveToConfig(yaml.createSection("Spawn-Location-1"));
        if (spawnLocation2 != null) spawnLocation2.saveToConfig(yaml.createSection("Spawn-Location-2"));
        try {
            yaml.save(targetFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    public RelativeLocation getInitialCenter() {
        return initialCenter;
    }

    public void setInitialCenter(RelativeLocation initialCenter) {
        this.initialCenter = initialCenter;
    }

    public boolean isAllowBuilding() {
        return allowBuilding;
    }

    public String getArenaName() {
        return arenaName;
    }

    public void setArenaName(String arenaName) {
        this.arenaName = arenaName;
    }

    public RelativeLocation getSpawnLocation1() {
        return spawnLocation1;
    }

    public void setSpawnLocation1(RelativeLocation spawnLocation1) {
        this.spawnLocation1 = spawnLocation1;
    }

    public RelativeLocation getSpawnLocation2() {
        return spawnLocation2;
    }

    public void setSpawnLocation2(RelativeLocation spawnLocation2) {
        this.spawnLocation2 = spawnLocation2;
    }
}
