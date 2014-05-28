package net.wayward_realms.waywardessentials.warp;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import net.wayward_realms.waywardlib.util.file.filter.YamlFileFilter;
import net.wayward_realms.waywardlib.util.serialisation.SerialisableLocation;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WarpManager {

    private WaywardEssentials plugin;

    public WarpManager(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    public Map<String, Location> getWarps() {
        Map<String, Location> warps = new HashMap<>();
        File warpDirectory = new File(plugin.getDataFolder(), "warps");
        for (File file : warpDirectory.listFiles(new YamlFileFilter())) {
            YamlConfiguration warpConfig = YamlConfiguration.loadConfiguration(file);
            Location warp = ((SerialisableLocation) warpConfig.get("location")).toLocation();
            warps.put(file.getName().replace(".yml", ""), warp);
        }
        return warps;
    }

    public Location getWarp(String name) {
        File warpDirectory = new File(plugin.getDataFolder(), "warps");
        File warpFile = new File(warpDirectory, name.toLowerCase() + ".yml");
        if (warpFile.exists()) {
            YamlConfiguration warpConfig = YamlConfiguration.loadConfiguration(warpFile);
            return ((SerialisableLocation) warpConfig.get("location")).toLocation();
        }
        return null;
    }

    public void addWarp(String name, Location location) {
        File warpDirectory = new File(plugin.getDataFolder(), "warps");
        File warpFile = new File(warpDirectory, name.toLowerCase() + ".yml");
        YamlConfiguration warpConfig = new YamlConfiguration();
        warpConfig.set("location", new SerialisableLocation(location));
        try {
            warpConfig.save(warpFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void removeWarp(String name) {
        File warpDirectory = new File(plugin.getDataFolder(), "warps");
        File warpFile = new File(warpDirectory, name.toLowerCase() + ".yml");
        if (warpFile.exists()) {
            warpFile.delete();
        }
    }

}
