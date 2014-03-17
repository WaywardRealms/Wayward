package net.wayward_realms.waywardmonsters;

import net.wayward_realms.waywardlib.util.serialisation.SerialisableLocation;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EntityLevelManager {

    private WaywardMonsters plugin;

    public Set<Location> zeroPoints = new HashSet<>();

    public EntityLevelManager(WaywardMonsters plugin) {
        this.plugin = plugin;
    }

    public void saveState() {
        List<SerialisableLocation> zeroPoints = new ArrayList<>();
        for (Location zeroPoint : this.zeroPoints) {
            zeroPoints.add(new SerialisableLocation(zeroPoint));
        }
        File zeroPointsFile = new File(plugin.getDataFolder(), "zero-points.yml");
        YamlConfiguration zeroPointsConfig = new YamlConfiguration();
        zeroPointsConfig.set("locations", zeroPoints);
        try {
            zeroPointsConfig.save(zeroPointsFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void loadState() {
        File zeroPointsFile = new File(plugin.getDataFolder(), "zero-points.yml");
        if (zeroPointsFile.exists()) {
            YamlConfiguration zeroPointsConfig = new YamlConfiguration();
            try {
                zeroPointsConfig.load(zeroPointsFile);
            } catch (IOException | InvalidConfigurationException exception) {
                exception.printStackTrace();
            }
            for (Object serialisableLocation : zeroPointsConfig.getList("locations")) {
                if (serialisableLocation instanceof SerialisableLocation) {
                    zeroPoints.add(((SerialisableLocation) serialisableLocation).toLocation());
                }
            }
        }
    }

    public void addZeroPoint(Location location) {
        zeroPoints.add(location);
    }

    public void removeZeroPoint(Location location) {
        zeroPoints.remove(location);
    }

    public Collection<Location> getZeroPoints() {
        return zeroPoints;
    }

    public int getEntityLevel(Entity entity) {
        if (zeroPoints.size() == 0) return 1;
        int minimumLevel = -1;
        for (Location zeroPoint : zeroPoints) {
            if (zeroPoint.getWorld().equals(entity.getWorld())) {
                int possibleLevel = (int) Math.floor((entity.getLocation().distance(zeroPoint) / 32D));
                minimumLevel = possibleLevel < minimumLevel || minimumLevel == -1 ? possibleLevel : minimumLevel;
            }
        }
        return Math.max(minimumLevel - 1, 0);
    }

    public int getEntityStatValue(Entity entity) {
        return (int) Math.round(((200D * (double) getEntityLevel(entity)) / 100D) + 5D);
    }

}
