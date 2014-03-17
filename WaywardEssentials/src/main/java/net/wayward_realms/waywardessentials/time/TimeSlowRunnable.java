package net.wayward_realms.waywardessentials.time;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.World;

public class TimeSlowRunnable implements Runnable {

    private WaywardEssentials plugin;

    public TimeSlowRunnable(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (World world : plugin.getServer().getWorlds()) {
            world.setFullTime((long) (world.getFullTime() - 100L + (100L * (1 / plugin.getConfig().getDouble("time-slow-factor")))));
        }
    }

}
