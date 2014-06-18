package net.wayward_realms.waywardenvironment;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

public class AdjustTimeRunnable extends BukkitRunnable{

    private final WaywardEnvironment plugin;
    private final String worldname;
    private final FileConfiguration config;

    public AdjustTimeRunnable(final WaywardEnvironment inplugin, final World inworld, final FileConfiguration inconfig){
        plugin = inplugin;
        worldname = inworld.getName();
        config = inconfig;
    }

    //Run as often as needed.  1/100th of the day length generally is a good idea
    //double cycleLength is a length in minutes for how long a day/night cycle is.  1200 = ticks in natural minute

    @Override
    public void run() {
        long systime = System.currentTimeMillis();
                Object entry = config.get("worlds." + worldname + ".cycleMinuteLength");
                if (entry != null) {
                    if (entry instanceof Number) {
                        double cycleLength = ((Number) entry).doubleValue() * 1200;
                        final long convertedPosition = ((long) ((systime % cycleLength) / cycleLength)) * 24000L;
                        Bukkit.getScheduler().runTask(plugin, new Runnable() {
                            public void run() {
                                Bukkit.getWorld(worldname).setTime(convertedPosition);
                            }
                        });
                    }
        } else {
            Bukkit.getScheduler().runTask(plugin, new Runnable() {
                public void run() {
                    plugin.getLogger().info("[WaywardEnvironment.AdjustTimeRunnable] Worlds/Config weren't collected from main thread, canceling runnable");
                }
            });
            this.cancel();
        }
    }
}