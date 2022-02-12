package me.five.duels;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import me.five.duels.api.DuelsAPI;
import me.five.duels.arena.ArenaManager;
import me.five.duels.brackets.BracketsManager;
import me.five.duels.command.impl.*;
import me.five.duels.data.DataManager;
import me.five.duels.kit.KitManager;
import me.five.duels.listener.BukkitListener;
import me.five.duels.queue.QueueManager;
import me.five.duels.util.ArenaTickTask;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FiveDuels extends JavaPlugin {

    private KitManager kitManager;
    private DataManager dataManager;
    private ArenaManager arenaManager;
    private QueueManager queueManager;
    private BracketsManager bracketsManager;
    private ExecutorService executorService;

    @Override
    public void onEnable() {

        loadConfigs();
        File kitsFile = new File(getDataFolder() + "/kits.yml");
        kitManager = new KitManager(YamlConfiguration.loadConfiguration(kitsFile), kitsFile);
        dataManager = new DataManager(this);
        queueManager = new QueueManager(this);
        bracketsManager = new BracketsManager(this);
        DuelsAPI.setPlugin(this);
        executorService = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("FAWE-FiveDuels-Thread").build());
        arenaManager = new ArenaManager(this);
        arenaManager.loadArenas();
        new ArenaTickTask(this).schedule();
        Bukkit.getPluginManager().registerEvents(new BukkitListener(this), this);
        registerCommands();
        getLogger().info("Plugin enabled!");

    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled");
    }

    public void registerCommands() {
        getCommand("duels").setExecutor(new DuelsCommand(this));
        getCommand("leavequeue").setExecutor(new LeaveQueueCommand(this));
        getCommand("spectate").setExecutor(new SpectateCommand(this));
        getCommand("duel").setExecutor(new DuelCommand(this));
        getCommand("stats").setExecutor(new StatsCommand(this));
    }

    public void loadConfigs() {

        if (!new File(getDataFolder() + "/kits.yml").exists())
            saveResource("kits.yml", false);

    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public BracketsManager getBracketsManager() {
        return bracketsManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public QueueManager getQueueManager() {
        return queueManager;
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
