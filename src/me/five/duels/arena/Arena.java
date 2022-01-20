package me.five.duels.arena;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.math.BlockVector3;
import me.five.duels.FiveDuels;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Arena {

    private int offset;
    private int gameTime;
    private int countdown;
    private String mapName;
    private ArenaData data;
    private boolean started;
    private FiveDuels plugin;
    private boolean allowBuilding;
    private List<Player> players;
    private List<Player> spectators;

    public Arena(ArenaData data, int offset, FiveDuels plugin) {

        this.mapName = data.getArenaName();
        this.allowBuilding = data.isAllowBuilding();
        this.offset = offset;
        this.plugin = plugin;
        pasteSchematic();

    }

    public void pasteSchematic() {

        plugin.getExecutorService().execute(() -> {

            File dir = new File(plugin.getDataFolder() + "/schematics/");
            dir.mkdirs();
            File schem = new File(plugin.getDataFolder() + "/schematics/" + mapName + ".schem");
            try {
                EditSession editsession = ClipboardFormats.findByFile(schem).load(schem).paste(FaweAPI.getWorld(plugin.getArenaManager().getArenaWorld().getName()), BlockVector3.at(offset, 60, 0));
                editsession.commit();
                editsession.close();
                plugin.getLogger().info("done " + plugin.getArenaManager().getArenaWorld().getName() + " " + offset);
            } catch (IOException ex) {
                plugin.getLogger().info("Failed to paste the " + mapName + " schematic! Does the schematic file exist?");
            }

        });

    }

}
