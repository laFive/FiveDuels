package me.five.duels.brackets;

import me.five.duels.FiveDuels;
import org.bukkit.entity.Player;

public class BracketsManager {

    private Brackets brackets;
    private FiveDuels fiveDuels;

    public BracketsManager(FiveDuels plugin) {
        this.fiveDuels = plugin;
    }

    public boolean isBracketsCreated() {
        return brackets != null;
    }

    public void createBrackets(Player host, int playerLimit, boolean build) {
        brackets = new Brackets(playerLimit, host, build, fiveDuels);
    }

    public void createBrackets(Player player) {

    }

    public void cancelBrackets() {
        brackets = null;
    }

}
