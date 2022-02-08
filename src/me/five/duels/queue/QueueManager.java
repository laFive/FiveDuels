package me.five.duels.queue;

import me.five.duels.FiveDuels;
import me.five.duels.util.DuelRequest;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class QueueManager {

    private FiveDuels plugin;
    private Map<String, List<Player>> duelsQueues;
    private List<DuelRequest> duelRequests;

    public QueueManager(FiveDuels plugin) {
        this.plugin = plugin;
        this.duelRequests = new ArrayList<>();
        this.duelsQueues = new HashMap<>();
    }

    public int getQueueCount(String queue) {

        if (!duelsQueues.containsKey(queue)) return 0;
        return duelsQueues.get(queue).size();

    }

    public boolean checkDuelRequest(Player player, Player target) {

        List<DuelRequest> validDuelRequests = duelRequests.stream().filter(dr -> dr.getPlayer().equals(target) && dr.getTarget().equals(player) && System.currentTimeMillis() - dr.getSendTime() < 60000L).toList();
        if (validDuelRequests.isEmpty()) return false;
        DuelRequest dr = validDuelRequests.get(0);
        plugin.getArenaManager().getLoadedArenas().stream().filter(ar -> ar.isDuelCreated()).forEach(arena -> arena.removePlayer(target));
        plugin.getArenaManager().getRandomArena().createDuel(dr.getPlayer(), dr.getTarget(), plugin.getKitManager().getKit(dr.getKit()));
        duelRequests.remove(dr);
        return true;

    }

    public void duelRequest(DuelRequest dr) {
        if (hasPendingRequest(dr.getPlayer(), dr.getTarget())) {
            dr.getPlayer().sendMessage(ChatColor.RED + "You already have a pending duel request for that player!");
            return;
        }
        duelRequests.add(dr);
        dr.getPlayer().sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "Duel request sent!");
        dr.getTarget().sendMessage(ChatColor.DARK_GRAY + "-------------------------------------------");
        dr.getTarget().sendMessage(ChatColor.DARK_AQUA + dr.getPlayer().getName() + ChatColor.GRAY + " has sent you a " + ChatColor.DARK_AQUA + dr.getKit() + ChatColor.GRAY + " duel request!");
        TextComponent message = new TextComponent(ChatColor.GRAY + "Click " + ChatColor.DARK_AQUA + "here " + ChatColor.GRAY + "to accept!");
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click to accept").create()));
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duel " + dr.getPlayer().getName()));
        dr.getTarget().spigot().sendMessage(message);
        dr.getTarget().sendMessage(ChatColor.DARK_GRAY + "-------------------------------------------");

    }

    public boolean hasPendingRequest(Player player, Player target) {

        return duelRequests.stream().anyMatch(dr -> dr.getPlayer().equals(player) && dr.getTarget().equals(target) && System.currentTimeMillis() - dr.getSendTime() < 60000L);

    }

    public void queuePlayer(Player player, String queue) {

        Iterator<DuelRequest> drIterator = duelRequests.iterator();
        boolean hasDuelsRequests = false;
        while (drIterator.hasNext()) {
            DuelRequest dr = drIterator.next();
            if (dr.getPlayer().equals(player)) {
                drIterator.remove();
                hasDuelsRequests = true;
            }
        }
        if (hasDuelsRequests) player.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GOLD + "Your duel requests have been cancelled as you joined a duels queue!");
        unQueuePlayer(player);

        if (duelsQueues.containsKey(queue)) {

            List<Player> currentQueue = duelsQueues.get(queue);
            currentQueue.add(player);
            checkQueue(queue);
            return;

        }

        List<Player> queueList = new ArrayList<>();
        queueList.add(player);
        duelsQueues.put(queue, queueList);

    }

    public void checkQueue(String queue) {

        List<Player> queuePlayers = duelsQueues.get(queue);
        if (queuePlayers.size() < 2) return;
        plugin.getArenaManager().getRandomArena().createDuel(queuePlayers.remove(0), queuePlayers.remove(0), plugin.getKitManager().getKit(queue));

    }

    public void unQueuePlayer(Player player) {

        Iterator<DuelRequest> drIterator = duelRequests.iterator();
        while (drIterator.hasNext()) {
            DuelRequest dr = drIterator.next();
            if (dr.getPlayer().equals(player)) drIterator.remove();
        }

        Iterator<List<Player>> queueIterator = duelsQueues.values().iterator();
        while (queueIterator.hasNext()) {
            List<Player> queuePlayers = queueIterator.next();
            if (queuePlayers.contains(player)) {
                queuePlayers.remove(player);
                player.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "You have left the duels queue");
                return;
            }
        }

    }

}
