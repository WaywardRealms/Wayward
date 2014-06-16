package net.wayward_realms.waywardenvironment;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AdjustTimeRunnable extends BukkitRunnable{

    private WaywardEnvironment plugin;
    List<World> worlds = null;
    FileConfiguration config = null;

    public AdjustTimeRunnable(WaywardEnvironment inplugin){
        plugin = inplugin;
        Future<List<World>> futureWorlds = Bukkit.getScheduler().callSyncMethod(plugin, new Callable<List<World>>() {
                    @Override
                    public List<World> call() {
                        return plugin.getServer().getWorlds();
                    }
                }
        );
        try {
            worlds = futureWorlds.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            plugin.getLogger().severe("[WaywardEnvironment.AdjustTimeRunnable] Couldn't retrieve the worlds from the main thread");
        } catch (ExecutionException e) {
            e.printStackTrace();
            plugin.getLogger().severe("[WaywardEnvironment.AdjustTimeRunnable] Couldn't retrieve the worlds from the main thread");
        }
        Future<FileConfiguration> futureConfig = Bukkit.getScheduler().callSyncMethod(plugin, new Callable<FileConfiguration>(){
                    @Override
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
        if(worlds != null && config != null) {
            for (final World world : worlds) {
                Object entry = config.get("worlds." + world.getName() + ".cycleMinuteLength");
                if (entry != null) {
                    if (entry instanceof Number) {
                        double cycleLength = ((Number) entry).doubleValue() * 1200;
                        final long convertedPosition = ((long) ((systime % cycleLength) / cycleLength)) * 24000L;
                        Bukkit.getScheduler().runTask(plugin, new Runnable() {
                            public void run() {
                                Bukkit.getWorld(world.getName()).setTime(convertedPosition);
                            }
                        });
                    }
                }
            }
        } else {
            plugin.getLogger().info("[WaywardEnvironment.AdjustTimeRunnable] Worlds/Config weren't collected from main thread, canceling runnable");
            this.cancel();
        }
    }
}