package me.five.duels.data;

import me.five.duels.FiveDuels;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataManager {

    private FiveDuels plugin;
    private Map<UUID, YamlConfiguration> playerData;

    public DataManager(FiveDuels plugin) {
        this.plugin = plugin;
        playerData = new HashMap<>();
    }

    public void registerPlayer(Player player) {

        if (playerData.containsKey(player.getUniqueId())) return;
        File loc = new File(plugin.getDataFolder() + "/playerdata/" + player.getUniqueId() + ".yml");
        YamlConfiguration dataFile = YamlConfiguration.loadConfiguration(loc);
        if (dataFile.get("wins") == null) dataFile.set("wins", 0);
        if (dataFile.get("games") == null) dataFile.set("games", 0);
        try {
            dataFile.save(loc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        playerData.put(player.getUniqueId(), dataFile);

    }

    public int getWins(Player player) {

        return playerData.get(player.getUniqueId()).getInt("wins");

    }

    public int getGames(Player player) {

        return playerData.get(player.getUniqueId()).getInt("games");

    }

    public void setWins(Player player, int wins) {

        playerData.get(player.getUniqueId()).set("wins", wins);
        try {
            playerData.get(player.getUniqueId()).save(new File(plugin.getDataFolder() + "/playerdata/" + player.getUniqueId() + ".yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setGames(Player player, int games) {

        playerData.get(player.getUniqueId()).set("games", games);
        try {
            playerData.get(player.getUniqueId()).save(new File(plugin.getDataFolder() + "/playerdata/" + player.getUniqueId() + ".yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
