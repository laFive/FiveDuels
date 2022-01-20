package me.five.duels.util;

import org.bukkit.configuration.ConfigurationSection;

public class RelativeLocation {

    private float yaw;
    private float pitch;
    private double relativeX;
    private double relativeY;
    private double relativeZ;

    public RelativeLocation(ConfigurationSection section) {

        relativeX = section.getDouble("RelativeX");
        relativeY = section.getDouble("RelativeY");
        relativeZ = section.getDouble("RelativeZ");
        yaw = ((Double)section.getDouble("yaw")).floatValue();
        pitch = ((Double)section.getDouble("pitch")).floatValue();

    }

    public RelativeLocation(double x, double y, double z, float yaw, float pitch) {
        this.relativeX = x;
        this.relativeY = y;
        this.relativeZ = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public void saveToConfig(ConfigurationSection section) {
        section.set("RelativeX", relativeX);
        section.set("RelativeY", relativeY);
        section.set("RelativeZ", relativeZ);
        section.set("yaw", yaw);
        section.set("pitch", pitch);
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public double getRelativeX() {
        return relativeX;
    }

    public double getRelativeY() {
        return relativeY;
    }

    public double getRelativeZ() {
        return relativeZ;
    }
}
