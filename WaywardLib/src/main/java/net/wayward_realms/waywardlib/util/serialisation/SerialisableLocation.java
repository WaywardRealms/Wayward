package net.wayward_realms.waywardlib.util.serialisation;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Aids in the serialisation of locations
 *
 */
public class SerialisableLocation implements ConfigurationSerializable, Serializable {

    private static final long serialVersionUID = 1721511236574072962L;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private String worldName;

    private SerialisableLocation() {}

    public SerialisableLocation(Location location) {
        x = location.getX();
        y = location.getY();
        z = location.getZ();
        yaw = location.getYaw();
        pitch = location.getPitch();
        worldName = location.getWorld().getName();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("x", x);
        serialised.put("y", y);
        serialised.put("z", z);
        serialised.put("yaw", yaw);
        serialised.put("pitch", pitch);
        serialised.put("world", worldName);
        return serialised;
    }

    public static SerialisableLocation deserialize(Map<String, Object> serialised) {
        SerialisableLocation deserialised = new SerialisableLocation();
        deserialised.x = (double) serialised.get("x");
        deserialised.y = (double) serialised.get("y");
        deserialised.z = (double) serialised.get("z");
        deserialised.yaw = ((Double) serialised.get("yaw")).floatValue();
        deserialised.pitch = ((Double) serialised.get("pitch")).floatValue();
        deserialised.worldName = (String) serialised.get("world");
        return deserialised;
    }

    /**
     * Converts the SerialisableLocation back to a Bukkit Location
     *
     * @return the original Location
     */
    public Location toLocation() {
        return new Location(Bukkit.getServer().getWorld(worldName), x, y, z, yaw, pitch);
    }

}
