package me.five.duels.kit;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class KitManager {

    private File kitsFileLocation;
    private YamlConfiguration kitsFile;
    private Map<String, Kit> loadedKits;

    public KitManager(YamlConfiguration kitsFile, File kitsFileLocation) {

        loadedKits = new HashMap<>();
        this.kitsFile = kitsFile;
        this.kitsFileLocation = kitsFileLocation;
        kitsFile.getConfigurationSection("kits").getKeys(false).forEach(kit -> loadedKits.put(kit, new Kit(kitsFile.getConfigurationSection("kits." + kit)).setName(kit)));

    }

    public Kit getKit(String name) {
        return loadedKits.getOrDefault(name, null);
    }

    public void addKit(String name, Kit kit) {
        loadedKits.put(name, kit);
        kitsFile.set("kits", null);
        kit.saveToConfig(kitsFile.createSection("kits." + name));
        try {
            kitsFile.save(kitsFileLocation);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
