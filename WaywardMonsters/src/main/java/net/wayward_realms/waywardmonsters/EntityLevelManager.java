package net.wayward_realms.waywardmonsters;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.skills.SkillsPlugin;
import net.wayward_realms.waywardlib.util.math.MathUtils;
import net.wayward_realms.waywardlib.util.serialisation.SerialisableLocation;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class EntityLevelManager {

    private WaywardMonsters plugin;

    public EntityLevelManager(WaywardMonsters plugin) {
        this.plugin = plugin;
    }

    private Cache<Entity, Integer> levelCache = CacheBuilder.newBuilder().
            maximumSize(100).
            expireAfterAccess(3, TimeUnit.MINUTES).
            build(new CacheLoader<Entity, Integer>() {
                public Integer load(Entity entity) {
                    Collection<Location> zeroPoints = getZeroPoints();
                    if (zeroPoints.size() == 0) return 1;
                    int minimumLevel = -1;
                    for (Location zeroPoint : zeroPoints) {
                        if (zeroPoint.getWorld().equals(entity.getWorld())) {
                            int possibleLevel = (int) Math.floor((MathUtils.fastsqrt(entity.getLocation().distanceSquared(zeroPoint)) / plugin.getConfig().getDouble("mob-level-scale", 32D)));
                            minimumLevel = possibleLevel < minimumLevel || minimumLevel == -1 ? possibleLevel : minimumLevel;
                        }
                    }
                    return Math.max(minimumLevel - 1, 0);
                }
            });

    public void addZeroPoint(Location location) {
        File zeroPointsFile = new File(plugin.getDataFolder(), "zero-points.yml");
        YamlConfiguration zeroPointsConfig = YamlConfiguration.loadConfiguration(zeroPointsFile);
        List<SerialisableLocation> zeroPoints = (List<SerialisableLocation>) zeroPointsConfig.getList("locations");
        zeroPoints.add(new SerialisableLocation(location));
        zeroPointsConfig.set("locations", zeroPoints);
        try {
            zeroPointsConfig.save(zeroPointsFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void removeZeroPoint(Location location) {
        File zeroPointsFile = new File(plugin.getDataFolder(), "zero-points.yml");
        YamlConfiguration zeroPointsConfig = YamlConfiguration.loadConfiguration(zeroPointsFile);
        List<SerialisableLocation> zeroPoints = (List<SerialisableLocation>) zeroPointsConfig.getList("locations");
        zeroPoints.remove(new SerialisableLocation(location));
        zeroPointsConfig.set("locations", zeroPoints);
        try {
            zeroPointsConfig.save(zeroPointsFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public Collection<Location> getZeroPoints() {
        File zeroPointsFile = new File(plugin.getDataFolder(), "zero-points.yml");
        YamlConfiguration zeroPointsConfig = YamlConfiguration.loadConfiguration(zeroPointsFile);
        List<SerialisableLocation> zeroPoints = (List<SerialisableLocation>) zeroPointsConfig.getList("locations");
        List<Location> locations = new ArrayList<>();
        for (SerialisableLocation location : zeroPoints) {
            locations.add(location.toLocation());
        }
        return locations;
    }

    public int getEntityLevel(Entity entity) {
        if (entity instanceof Player) {
            RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
            if (characterPluginProvider != null) {
                CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                RegisteredServiceProvider<SkillsPlugin> skillsPluginProvider = plugin.getServer().getServicesManager().getRegistration(SkillsPlugin.class);
                if (skillsPluginProvider != null) {
                    SkillsPlugin skillsPlugin = skillsPluginProvider.getProvider();
                    return skillsPlugin.getLevel(characterPlugin.getActiveCharacter((Player) entity));
                }
            }
        } else if (entity instanceof Monster) {
            try {
                return levelCache.get(entity);
            } catch (ExecutionException exception) {
                exception.printStackTrace();
            }
        }
        return 0;
    }

    public int getEntityStatValue(Entity entity) {
        return (int) Math.round(((200D * (double) getEntityLevel(entity)) / 100D) + 5D);
    }

}
