package net.wayward_realms.waywardenvironment;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AdjustTimeRunnable extends BukkitRunnable{

    private WaywardEnvironment plugin;
    List<World> worlds;
    FileConfiguration config;

    public AdjustTimeRunnable(WaywardEnvironment inplugin){
        plugin = inplugin;
        Future<List<World>> futureWorlds = plugin.getServer().getScheduler().callSyncMethod(plugin, new Callable<List<World>>(){
            public List<World> call() {
                return plugin.getServer().getWorlds();
            }
        }
        );
        try {
            worlds = futureWorlds.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Future<FileConfiguration> futureConfig = plugin.getServer().getScheduler().callSyncMethod(plugin, new Callable<FileConfiguration>(){
            public FileConfiguration call() {
                return plugin.getConfig();
            }
        }
        );
        try {
            config = futureConfig.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    //Run as often as needed.  1/100th of the day length generally is a good idea
    //double cycleLength is a length in minutes for how long a day/night cycle is.  1200 = ticks in natural minute

    @Override
    public void run() {
        long systime = System.currentTimeMillis();
            for (World world : worlds) {
                Object entry = config.get("worlds." + world.getName() + ".cycleMinuteLength");
                if (entry != null) {
                    if (entry instanceof Number) {
                        double cycleLength = ((Number) entry).doubleValue() * 1200;
                        long convertedPosition = ((long) ((systime % cycleLength) / cycleLength)) * 24000L;
                        world.setTime(convertedPosition);
                    }
                }
            }
    }
}