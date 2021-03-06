package me.five.duels.arena;

import me.five.duels.FiveDuels;
import me.five.duels.util.VectorLocation;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ArenaData {

    private String arenaName;
    private boolean allowBuilding;
    private VectorLocation initialCenter;
    private VectorLocation spawnLocation1;
    private VectorLocation spawnLocation2;

    public ArenaData(String arenaName, VectorLocation initialCenter) {
        this.arenaName = arenaName;
        this.initialCenter = initialCenter;
        this.allowBuilding = true;
    }

    public ArenaData(YamlConfiguration arenaConfig) {
        this.arenaName = arenaConfig.getString("map-name");
        this.allowBuilding = arenaConfig.getBoolean("AllowBuilding");
        this.initialCenter = new VectorLocation(arenaConfig.getConfigurationSection("Center-Vector"));
        if (arenaConfig.getConfigurationSection("Spawn-Location-1") != null) this.spawnLocation1 = new VectorLocation(arenaConfig.getConfigurationSection("Spawn-Location-1"));
        if (arenaConfig.getConfigurationSection("Spawn-Location-2") != null) this.spawnLocation2 = new VectorLocation(arenaConfig.getConfigurationSection("Spawn-Location-2"));
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

    public VectorLocation getInitialCenter() {
        return initialCenter;
    }

    public void setInitialCenter(VectorLocation initialCenter) {
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

    public VectorLocation getSpawnLocation1() {
        return spawnLocation1;
    }

    public void setSpawnLocation1(VectorLocation spawnLocation1) {
        this.spawnLocation1 = spawnLocation1;
    }

    public VectorLocation getSpawnLocation2() {
        return spawnLocation2;
    }

    public void setSpawnLocation2(VectorLocation spawnLocation2) {
        this.spawnLocation2 = spawnLocation2;
    }
}
