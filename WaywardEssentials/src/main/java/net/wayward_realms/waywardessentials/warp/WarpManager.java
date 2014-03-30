package net.wayward_realms.waywardessentials.warp;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import net.wayward_realms.waywardlib.util.serialisation.SerialisableLocation;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WarpManager {

    private WaywardEssentials plugin;
    private Map<String, Location> warps = new HashMap<>();

    public WarpManager(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    public Map<String, Location> getWarps() {
        return warps;
    }

    public Location getWarp(String name) {
        return warps.get(name.toLowerCase());
    }

    public void addWarp(String name, Location location) {
        warps.put(name.toLowerCase(), location);
    }

    public void removeWarp(String name) {
        warps.remove(name.toLowerCase());
    }

    public void save() {
        File warpFile = new File(plugin.getDataFolder().getPath() + File.separator + "warps.yml");
        YamlConfiguration warpConfig = new YamlConfiguration();
        for (String warpName : warps.keySet()) {
            warpConfig.set(warpName, new SerialisableLocation(warps.get(warpName)));
        }
        try {
            warpConfig.save(warpFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void load() {
        File warpFile = new File(plugin.getDataFolder().getPath() + File.separator + "warps.yml");
        if (warpFile.exists()) {
            YamlConfiguration warpConfig = new YamlConfiguration();
            try {
                warpConfig.load(warpFile);
            } catch (IOException | InvalidConfigurationException exception) {
                exception.printStackTrace();
            }
            for (String warpName : warpConfig.getKeys(false)) {
                warps.put(warpName.toLowerCase(), ((SerialisableLocation) warpConfig.get(warpName)).toLocation());
            }
        }
    }

}
