package me.five.duels.util;

public class ArenaUtil {

    public static String parseSeconds(int seconds) {

        int minutes = (int) Math.floor(seconds / 60);
        seconds -= minutes * 60;

        String time = "";
        if (minutes > 0) {
            time = minutes + "m ";
        }
        if (seconds > 0) {
            time = time + seconds + "s";
        } else if (minutes <= 0) {
            time = "0s";
        }

        return time;

    }

}
