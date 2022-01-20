package me.five.duels.arena;

import me.five.duels.util.RelativeLocation;

public class ArenaData {

    private String arenaName;
    private RelativeLocation spawnLocation1;
    private RelativeLocation spawnLocation2;

    public String getArenaName() {
        return arenaName;
    }

    public void setArenaName(String arenaName) {
        this.arenaName = arenaName;
    }

    public RelativeLocation getSpawnLocation1() {
        return spawnLocation1;
    }

    public void setSpawnLocation1(RelativeLocation spawnLocation1) {
        this.spawnLocation1 = spawnLocation1;
    }

    public RelativeLocation getSpawnLocation2() {
        return spawnLocation2;
    }

    public void setSpawnLocation2(RelativeLocation spawnLocation2) {
        this.spawnLocation2 = spawnLocation2;
    }
}
