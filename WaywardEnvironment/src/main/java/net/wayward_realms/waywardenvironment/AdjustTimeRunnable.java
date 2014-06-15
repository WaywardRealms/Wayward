package net.wayward_realms.waywardenvironment;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class AdjustTimeRunnable extends BukkitRunnable{

    //Run as often as needed.  1/100th of the day length generally is a good idea
    //double cycleLength is a length in minutes for how long a day/night cycle is.  1200 = ticks in natural minute

    @Override
    public void run() {
        WaywardEnvironment plugin = (WaywardEnvironment) Bukkit.getPluginManager().getPlugin("WaywardEnvironment");
        long systime = System.currentTimeMillis();
        for (World world : Bukkit.getServer().getWorlds()) {
            double cycleLength = plugin.getConfig().getDouble("worlds." + world.getName() + ".cycleMinuteLength") * 1200;
            long convertedPosition = ((long)((systime % cycleLength)/cycleLength))*24000L;
            world.setTime(convertedPosition);
        }
    }
}