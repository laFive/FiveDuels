package me.five.duels.util;

import org.bukkit.entity.Player;

public class DuelRequest {

    private Player player;
    private Player target;
    private String kit;
    private long sendTime;

    public DuelRequest(Player player, Player target, String kit) {
        this.player = player;
        this.target = target;
        this.kit = kit;
        this.sendTime = System.currentTimeMillis();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getTarget() {
        return target;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    public String getKit() {
        return kit;
    }

    public void setKit(String kit) {
        this.kit = kit;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }
}
