package net.wayward_realms.waywardlib.util.location;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Operations to simplify common location tasks
 *
 */
public class LocationUtils {

    /**
     * Converts a location to a string parsable with {@link #parseLocation}
     *
     * @param location the location
     * @return the location as a string
     */
    public static String toString(Location location) {
        return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch();
    }

    /**
     * Parses a location from a string argument
     *
     * @param locationString the location as a string, with each part separated by commas. May either be in the format world,x,y,z or world,x,y,z,yaw,pitch
     * @return the location
     */
    public static Location parseLocation(String locationString) {
        if (StringUtils.countMatches(locationString, ",") == 3) {
            World world = Bukkit.getWorld(locationString.split(",")[0]);
            try {
                double x = Double.parseDouble(locationString.split(",")[1]);
                double y = Double.parseDouble(locationString.split(",")[2]);
                double z = Double.parseDouble(locationString.split(",")[3]);
                if (world != null) {
                    return new Location(world, x, y, z);
                } else {
                    throw new IllegalArgumentException("Unparsable location!");
                }
            } catch (NumberFormatException exception) {
                throw new IllegalArgumentException("Unparsable location!", exception);
            }
        } else if (StringUtils.countMatches(locationString, ",") == 5) {
            World world = Bukkit.getWorld(locationString.split(",")[0]);
            try {
                double x = Double.parseDouble(locationString.split(",")[1]);
                double y = Double.parseDouble(locationString.split(",")[2]);
                double z = Double.parseDouble(locationString.split(",")[3]);
                float yaw = Float.parseFloat(locationString.split(",")[4]);
                float pitch = Float.parseFloat(locationString.split(",")[5]);
                if (world != null) {
                    return new Location(world, x, y, z, yaw, pitch);
                } else {
                    throw new IllegalArgumentException("Unparsable location!");
                }
            } catch (NumberFormatException exception) {
                throw new IllegalArgumentException("Unparsable location!", exception);
            }
        }
        throw new IllegalArgumentException("Unparsable location!");
    }

    /**
     * Converts a location into a string parsable with the {@link #parseLocation(String) parseLocation} method
     *
     * @param location the location to convert into a string
     * @return the location string
     */
    public static String stringify(Location location) {
        return location.getWorld().getName() + ',' + location.getX() + ',' + location.getY() + ',' + location.getZ() + ',' + location.getYaw() + ',' + location.getPitch();
    }

}
