package me.five.duels;

import me.five.duels.command.impl.DuelsCommand;
import me.five.duels.kit.KitManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class FiveDuels extends JavaPlugin {

    private KitManager kitManager;

    @Override
    public void onEnable() {

        loadConfigs();
        File kitsFile = new File(getDataFolder() + "/kits.yml");
        kitManager = new KitManager(YamlConfiguration.loadConfiguration(kitsFile), kitsFile);
        registerCommands();
        getLogger().info("Plugin enabled!");

    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled");
    }

    public void registerCommands() {
        getCommand("duels").setExecutor(new DuelsCommand(this));
    }

    public void loadConfigs() {

        if (!new File(getDataFolder() + "/kits.yml").exists())
            saveResource("kits.yml", false);

    }

    public KitManager getKitManager() {
        return kitManager;
    }
}
