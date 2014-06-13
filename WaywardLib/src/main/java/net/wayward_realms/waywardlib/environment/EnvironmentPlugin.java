package net.wayward_realms.waywardlib.environment;

import net.wayward_realms.waywardlib.WaywardPlugin;
import org.bukkit.World;

/**
 * Represents an environment plugin WaywardPlugin
 */
public interface EnvironmentPlugin extends WaywardPlugin {
    public long getTime(World world);
    public void setTime(World world,int time);
    public String getWeather(World world);
    public void setWeather(World world, String weather);
}