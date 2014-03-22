package net.wayward_realms.waywardevents;

import net.wayward_realms.waywardlib.events.Dungeon;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DungeonManager {

    private Map<String, Dungeon> dungeons = new HashMap<>();

    public Map<String, Dungeon> getDungeons() {
        return dungeons;
    }

    public Dungeon getDungeon(String name) {
        return dungeons.get(name);
    }

    public void save(net.wayward_realms.waywardevents.WaywardEvents plugin) {
        File file = new File(plugin.getDataFolder().getPath() + File.separator + "dungeons.yml");
        YamlConfiguration config = new YamlConfiguration();
        for (String dungeonName : dungeons.keySet()) {
            config.set(dungeonName, dungeons.get(dungeonName));
        }
        try {
            config.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void load(net.wayward_realms.waywardevents.WaywardEvents plugin) {
        File file = new File(plugin.getDataFolder().getPath() + File.separator + "dungeons.yml");
        if (file.exists()) {
            YamlConfiguration config = new YamlConfiguration();
            try {
                config.load(file);
            } catch (IOException | InvalidConfigurationException exception) {
                exception.printStackTrace();
            }
            for (String dungeonName : config.getKeys(false)) {
                dungeons.put(dungeonName, (Dungeon) config.get(dungeonName));
            }
        }
    }

}
