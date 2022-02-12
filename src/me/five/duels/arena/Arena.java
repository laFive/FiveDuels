package me.five.duels.arena;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.math.BlockVector3;
import me.five.duels.FiveDuels;
import me.five.duels.brackets.Brackets;
import me.five.duels.kit.Kit;
import me.five.duels.util.ArenaUtil;
import me.five.duels.util.PacketUtil;
import me.five.duels.util.VectorLocation;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Arena {

    private Kit kit;
    private int ticks;
    private int offset;
    private Chunk chunk;
    private int gameTime;
    private Scoreboard sb;
    private Player winner;
    private int countdown;
    private String mapName;
    private ArenaData data;
    private boolean created;
    private boolean started;
    private Team statusTeam;
    private FiveDuels plugin;
    private boolean allowBuilding;
    private List<Player> players;
    private List<Player> spectators;
    private List<VectorLocation> playerBlocks;
    private Brackets brackets;

    public Arena(ArenaData data, int offset, FiveDuels plugin) {

        this.mapName = data.getArenaName();
        this.allowBuilding = data.isAllowBuilding();
        this.offset = offset;
        this.plugin = plugin;
        this.data = data;
        this.players = new ArrayList<>();
        this.spectators = new ArrayList<>();
        this.playerBlocks = new ArrayList<>();
        pasteSchematic();

    }

    public Arena(ArenaData data, int offset, FiveDuels plugin, Brackets brackets) {

        this.mapName = data.getArenaName();
        this.allowBuilding = data.isAllowBuilding();
        this.offset = offset;
        this.plugin = plugin;
        this.data = data;
        this.players = new ArrayList<>();
        this.spectators = new ArrayList<>();
        this.playerBlocks = new ArrayList<>();
        this.brackets = brackets;
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
            } catch (IOException ex) {
                plugin.getLogger().info("Failed to paste the " + mapName + " schematic! Does the schematic file exist?");
            }

        });

    }

    public boolean isDuelCreated() {
        return created;
    }

    public void endDuel(Player winner) {

        players.forEach(player -> player.sendTitle(ChatColor.RED.toString() + ChatColor.BOLD + "GAME OVER!", ChatColor.GRAY + "Winner: " + winner.getName(), 0, 120, 10));
        spectators.forEach(player -> player.sendTitle(ChatColor.RED.toString() + ChatColor.BOLD + "GAME OVER!", ChatColor.GRAY + "Winner: " + winner.getName(), 0, 120, 10));
        winner.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "WINNER!", ChatColor.GRAY + "Congratulations! You have won the duel", 0, 120, 10);
        players.forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_WITHER_HURT, 10, 1));
        spectators.forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_WITHER_HURT, 10, 1));
        this.winner = winner;
        ticks = 19;
        plugin.getDataManager().setWins(winner, plugin.getDataManager().getWins(winner) + 1);

    }

    public void createDuel(Player player1, Player player2, Kit kit) {

        this.kit = kit;
        players.add(player1);
        players.add(player2);
        this.created = true;
        this.started = false;
        this.winner = null;
        this.countdown = 10;
        this.gameTime = 0;
        this.ticks = 1;

        Location spawnLocation1 = new Location(plugin.getArenaManager().getArenaWorld(), offset + data.getSpawnLocation1().getRelativeX(), 60.1 + data.getSpawnLocation1().getRelativeY(), data.getSpawnLocation1().getRelativeZ(), data.getSpawnLocation1().getYaw(), data.getSpawnLocation1().getPitch());
        Location spawnLocation2 = new Location(plugin.getArenaManager().getArenaWorld(), offset + data.getSpawnLocation2().getRelativeX(), 60.1 + data.getSpawnLocation2().getRelativeY(), data.getSpawnLocation2().getRelativeZ(), data.getSpawnLocation2().getYaw(), data.getSpawnLocation2().getPitch());

        spawnLocation1.getChunk().load(true);
        spawnLocation2.getChunk().load(true);

        player1.getInventory().clear();
        player1.getInventory().setArmorContents(new ItemStack[] {});
        player1.setHealth(20);
        player1.setFoodLevel(20);
        player1.setAllowFlight(false);
        player2.getInventory().clear();
        player2.getInventory().setArmorContents(new ItemStack[] {});
        player2.setHealth(20);
        player2.setFoodLevel(20);
        player2.setAllowFlight(false);
        spectators.clear();

        player1.teleport(spawnLocation1);
        player2.teleport(spawnLocation2);

        generateScoreboard();
        player1.setScoreboard(sb);
        player2.setScoreboard(sb);

        plugin.getDataManager().setGames(player1, plugin.getDataManager().getGames(player1) + 1);
        plugin.getDataManager().setGames(player2, plugin.getDataManager().getGames(player2) + 1);

    }

    public void bracketsEnd() {
        if (brackets != null && created) endDuel(players.get(0));
    }

    public void tick() {

        if (players.size() == 0 || !created) return;
        if (ticks == 9) {
            Location spawnLocation1 = new Location(plugin.getArenaManager().getArenaWorld(), offset + data.getSpawnLocation1().getRelativeX(), 60 + data.getSpawnLocation1().getRelativeY(), data.getSpawnLocation1().getRelativeZ(), data.getSpawnLocation1().getYaw(), data.getSpawnLocation1().getPitch());
            Location spawnLocation2 = new Location(plugin.getArenaManager().getArenaWorld(), offset + data.getSpawnLocation2().getRelativeX(), 60 + data.getSpawnLocation2().getRelativeY(), data.getSpawnLocation2().getRelativeZ(), data.getSpawnLocation2().getYaw(), data.getSpawnLocation2().getPitch());
            PacketUtil.sitPlayer(players.get(0), spawnLocation1);
            PacketUtil.sitPlayer(players.get(1), spawnLocation2);
        }
        ++ticks;

        if (ticks % 20 == 0 && started) {
            ++gameTime;
            statusTeam.setSuffix(ChatColor.GRAY.toString() + ArenaUtil.parseSeconds(gameTime));
        }


        if (winner != null) {

            if (ticks % 20 == 0 && ticks < 160) {
                Firework firework = winner.getWorld().spawn(winner.getPlayer().getLocation(), Firework.class);
                FireworkMeta meta = firework.getFireworkMeta();
                meta.addEffect(FireworkEffect.builder().withColor(Color.BLUE).with(FireworkEffect.Type.BALL_LARGE).build());
                meta.setPower(1);
                firework.setFireworkMeta(meta);
            }

            if (ticks == 200) {
                World world = Bukkit.getWorlds().get(0);
                Iterator<Player> playerIterator = players.iterator();
                while (playerIterator.hasNext()) {
                    Player player = playerIterator.next();
                    playerIterator.remove();
                    player.teleport(world.getSpawnLocation());
                    player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
                    player.setHealth(20);
                    player.setFoodLevel(20);
                    player.setExp(0);
                    player.setLevel(0);
                    player.setFlying(false);
                    player.setAllowFlight(false);
                }
                Iterator<Player> spectatorIterator = spectators.iterator();
                while (spectatorIterator.hasNext()) {
                    Player spec = spectatorIterator.next();
                    spectatorIterator.remove();
                    spec.teleport(world.getSpawnLocation());
                    spec.setAllowFlight(false);
                    spec.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
                    Bukkit.getOnlinePlayers().forEach(player -> player.showPlayer(plugin, spec));
                }

                pasteSchematic();
                started = false;
                created = false;
                spectators.clear();
                players.clear();
                brackets = null;

            }

            return;

        }

        if (ticks % 20 == 0 && !started) {

            if (countdown == 0) {
                started = true;
                players.forEach(player -> player.sendTitle(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "GO!", ChatColor.GRAY + "Duel started!", 0, 40, 10));
                players.forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 10, 1));
                players.forEach(player -> player.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "The duel has began!"));
                spectators.forEach(player -> player.sendTitle(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "GO!", ChatColor.GRAY + "Duel started!", 0, 40, 10));
                spectators.forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 10, 1));
                spectators.forEach(player -> player.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "The duel has began!"));
                players.forEach(player -> PacketUtil.unSit(player));
                players.forEach(player -> kit.applyToPlayer(player));
                statusTeam.setPrefix(ChatColor.GOLD + "Game Time " + ChatColor.DARK_GRAY + "> ");
                statusTeam.setSuffix(ChatColor.GRAY + ArenaUtil.parseSeconds(gameTime));
                return;
            }

            players.forEach(player -> player.sendTitle(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + countdown, ChatColor.GRAY + "Duel starting!", 0, 25, 0));
            players.forEach(player -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1));
            players.forEach(player -> player.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "Duel will start in " + countdown + " seconds!"));
            spectators.forEach(player -> player.sendTitle(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + countdown, ChatColor.GRAY + "Duel starting!", 0, 25, 0));
            spectators.forEach(player -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1));
            spectators.forEach(player -> player.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "Duel will start in " + countdown + " seconds!"));
            statusTeam.setSuffix(ChatColor.GRAY + ArenaUtil.parseSeconds(countdown));
            --countdown;

        }

        if (ticks % 10 == 0) {

            for (Player spectator : spectators) {
                Location centerLocation = new Location(plugin.getArenaManager().getArenaWorld(), offset + 0.5, 70, 0.5);
                if (spectator.getLocation().distance(centerLocation) > 120 || spectator.getLocation().getY() < 40) {
                    spectator.teleport(centerLocation);
                }
            }

        }

    }

    public void removePlayer(Player player) {

        if (winner != null && players.contains(player)) {
            players.remove(player);
        }
        if (spectators.contains(player)) spectators.remove(player);
        if (players.contains(player)) {

            players.remove(player);
            endDuel(players.get(0));

        }

    }

    public void generateScoreboard() {

        sb = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective sidebar = sb.registerNewObjective("DuelSB", "N/A", ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Duel");
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
        sidebar.getScore(ChatColor.RED.toString()).setScore(6);
        sidebar.getScore(ChatColor.GOLD + "Map " + ChatColor.DARK_GRAY + "> " + ChatColor.GRAY + data.getArenaName()).setScore(5);
        sidebar.getScore(ChatColor.GOLD + "Kit " + ChatColor.DARK_GRAY + "> " + ChatColor.GRAY + kit.getName()).setScore(4);
        statusTeam = sb.registerNewTeam("status");
        statusTeam.addEntry(ChatColor.GOLD.toString() + ChatColor.BLACK);
        statusTeam.setPrefix(ChatColor.GOLD + "Status " + ChatColor.DARK_GRAY + "> ");
        statusTeam.setSuffix(ChatColor.GRAY + ArenaUtil.parseSeconds(countdown));
        sidebar.getScore(ChatColor.GOLD.toString() + ChatColor.BLACK).setScore(3);
        sidebar.getScore(ChatColor.BLACK.toString()).setScore(2);
        sidebar.getScore(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "ITrulyEvents").setScore(1);

    }

    public void addSpectator(Player player) {

        spectators.add(player);
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[] {});
        player.setHealth(20);
        player.setFoodLevel(20);
        player.teleport(new Location(plugin.getArenaManager().getArenaWorld(), offset + 0.5, 70, 0.5));
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setScoreboard(sb);
        ItemStack leaveItem = new ItemStack(Material.RED_BED);
        ItemMeta leaveMeta = leaveItem.getItemMeta();
        leaveMeta.setDisplayName(ChatColor.RED + "Quit Game " + ChatColor.GRAY + "(Right Click)");
        leaveItem.setItemMeta(leaveMeta);
        player.getInventory().setItem(8, leaveItem);
        spectators.forEach(p -> p.hidePlayer(plugin, player));
        players.forEach(p -> p.hidePlayer(plugin, player));

    }

    public boolean spectate(Player player, Player target) {

        if (players.contains(player) || spectators.contains(player)) return false;
        if (players.contains(target)) {
            addSpectator(player);
            return true;
        }

        return false;

    }

    public void onEvent(Event event) {

        if (event instanceof InventoryClickEvent) {

            InventoryClickEvent e = (InventoryClickEvent) event;
            if (spectators.contains((Player) e.getWhoClicked())) {
                e.setCancelled(true);
            }

        }

        if (event instanceof EntityDamageByEntityEvent) {

            EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
            if ((e.getDamager() instanceof Player) && (e.getEntity() instanceof Player)) {
                Player attacker = (Player) e.getDamager();
                if (spectators.contains(attacker)) {
                    e.setCancelled(true);
                }
            }

        }

        if (event instanceof PlayerDropItemEvent) {

            PlayerDropItemEvent e = (PlayerDropItemEvent) event;
            if (spectators.contains(e.getPlayer())) {
                e.setCancelled(true);
            }

        }

        if (event instanceof PlayerTeleportEvent) {

            PlayerTeleportEvent e = (PlayerTeleportEvent) event;
            if (!e.getTo().getWorld().equals(plugin.getArenaManager().getArenaWorld())) {

                if (players.contains(e.getPlayer()) && winner == null) {
                    e.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
                    players.remove(e.getPlayer());
                    started = true;
                    endDuel(players.get(0));
                    return;
                }

                if (spectators.contains(e.getPlayer())) {
                    Bukkit.getOnlinePlayers().forEach(player -> player.showPlayer(plugin, e.getPlayer()));
                    e.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
                    spectators.remove(e.getPlayer());
                }

            }

        }

        if (event instanceof PlayerQuitEvent) {

            if (players.size() <= 1 || winner != null) return;

            PlayerQuitEvent e = (PlayerQuitEvent) event;
            if (players.contains(e.getPlayer())) {

                players.remove(e.getPlayer());
                started = true;
                endDuel(players.get(0));

            }

        }

        if (event instanceof EntityDamageEvent) {

            EntityDamageEvent e = (EntityDamageEvent) event;
            if (!(e.getEntity() instanceof Player)) return;
            Player player = (Player) e.getEntity();
            if (winner != null && players.contains(player)) {
                e.setCancelled(true);
                return;
            }
            if (spectators.contains(player)) {
                e.setCancelled(true);
                return;
            }
            if (players.contains(player)) {

                if (e.getFinalDamage() > player.getHealth()) {
                    e.setCancelled(true);
                    players.remove(player);
                    addSpectator(player);
                    if (players.size() > 0) endDuel(players.get(0));
                }

            }

        }

        if (event instanceof BlockPlaceEvent) {

            BlockPlaceEvent e = (BlockPlaceEvent) event;
            if (spectators.contains(e.getPlayer())) {
                e.setCancelled(true);
                return;
            }
            if (players.contains(e.getPlayer())) {
                if (winner != null) e.setCancelled(true);
                if (!data.isAllowBuilding()) e.setCancelled(true);
                playerBlocks.add(new VectorLocation(e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ(), 0, 0));
            }

        }

        if (event instanceof BlockBreakEvent) {

            BlockBreakEvent e = (BlockBreakEvent) event;
            if (spectators.contains(e.getPlayer())) {
                e.setCancelled(true);
                return;
            }
            if (players.contains(e.getPlayer())) {
                if (winner != null) e.setCancelled(true);
                if (!data.isAllowBuilding()) e.setCancelled(true);

                Iterator<VectorLocation> playerBlockIterator = playerBlocks.iterator();
                while (playerBlockIterator.hasNext()) {
                    VectorLocation vloc = playerBlockIterator.next();
                    if (vloc.getRelativeX() == e.getBlock().getX() && vloc.getRelativeY() == e.getBlock().getY()
                            && vloc.getRelativeZ() == e.getBlock().getZ()) {

                        playerBlockIterator.remove();
                        return;

                    }
                }

                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "You can only break blocks placed by a player!");

            }

        }

        if (event instanceof PlayerInteractEvent) {

            PlayerInteractEvent e = (PlayerInteractEvent) event;
            if (spectators.contains(e.getPlayer())) {

                if (e.hasItem() && e.getItem().hasItemMeta()) {

                    if (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Quit Game " + ChatColor.GRAY + "(Right Click)")) {

                        e.getPlayer().teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
                        e.getPlayer().getInventory().clear();
                        e.getPlayer().setAllowFlight(false);
                        e.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
                        Bukkit.getOnlinePlayers().forEach(player -> player.showPlayer(plugin, e.getPlayer()));
                        spectators.remove(e.getPlayer());

                    }

                }

            }
            if (!e.hasBlock()) return;
            if (spectators.contains(e.getPlayer())) {
                e.setCancelled(true);
                return;
            }
            if (players.contains(e.getPlayer())) {
                if (winner != null) e.setCancelled(true);
                if (!data.isAllowBuilding()) e.setCancelled(true);
            }

        }

    }

    public List<Player> getPlayers() {
        return players;
    }

}
