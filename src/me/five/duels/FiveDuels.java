package me.five.duels;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import me.five.duels.arena.ArenaManager;
import me.five.duels.command.impl.DuelsCommand;
import me.five.duels.kit.KitManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FiveDuels extends JavaPlugin {

    private KitManager kitManager;
    private ArenaManager arenaManager;
    private ExecutorService executorService;

    @Override
    public void onEnable() {

        loadConfigs();
        File kitsFile = new File(getDataFolder() + "/kits.yml");
        kitManager = new KitManager(YamlConfiguration.loadConfiguration(kitsFile), kitsFile);
        executorService = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("FAWE-FiveDuels-Thread").build());
        arenaManager = new ArenaManager(this);
        arenaManager.loadArenas();
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

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
