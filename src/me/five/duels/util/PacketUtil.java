package me.five.duels.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class PacketUtil {

    public static void sitPlayer(Player player, Location location) {

        ProtocolManager pman = ProtocolLibrary.getProtocolManager();
        PacketContainer spawnEntiy = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
        spawnEntiy.getIntegers().write(0, 999);
        spawnEntiy.getUUIDs().write(0, UUID.randomUUID());
        spawnEntiy.getIntegers().write(1, 4);
        spawnEntiy.getIntegers().write(2, 0);
        spawnEntiy.getIntegers().write(3, 0);
        spawnEntiy.getIntegers().write(4, 0);
        spawnEntiy.getDoubles().write(0, location.getX());
        spawnEntiy.getDoubles().write(1, location.getY());
        spawnEntiy.getDoubles().write(2, location.getZ());
        spawnEntiy.getBytes().write(0, (byte)((int)(player.getLocation().getYaw() * 256.0F / 360.0F)));
        spawnEntiy.getBytes().write(1, (byte)((int)(player.getLocation().getPitch() * 256.0F / 360.0F)));
        spawnEntiy.getBytes().write(2, (byte)((int)(player.getLocation().getYaw() * 256.0F / 360.0F)));
        PacketContainer metadata = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        metadata.getIntegers().write(0, 999);
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
        watcher.setEntity(player);
        watcher.setObject(0, serializer, (byte) (0x20));
        metadata.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        PacketContainer entityRiding = new PacketContainer(PacketType.Play.Server.MOUNT);
        entityRiding.getIntegers().write(0, 999);
        entityRiding.getIntegerArrays().write(0, new int[] {player.getEntityId()});
        try {
            pman.sendServerPacket(player, spawnEntiy);
            pman.sendServerPacket(player, metadata);
            pman.sendServerPacket(player, entityRiding);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public static void unSit(Player player) {

        ProtocolManager pman = ProtocolLibrary.getProtocolManager();
        PacketContainer entityRiding = new PacketContainer(PacketType.Play.Server.MOUNT);
        entityRiding.getIntegers().write(0, 999);
        entityRiding.getIntegerArrays().write(0, new int[] {});
        try {
            pman.sendServerPacket(player, entityRiding);
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }

    }

}
