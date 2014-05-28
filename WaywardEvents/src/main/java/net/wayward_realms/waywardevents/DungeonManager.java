package net.wayward_realms.waywardevents;

import net.wayward_realms.waywardlib.events.Dungeon;
import net.wayward_realms.waywardlib.util.file.filter.YamlFileFilter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DungeonManager {

    private WaywardEvents plugin;

    public DungeonManager(WaywardEvents plugin) {
        this.plugin = plugin;
    }

    public Map<String, Dungeon> getDungeons() {
        Map<String, Dungeon> dungeons = new HashMap<>();
        File dungeonsDirectory = new File(plugin.getDataFolder(), "dungeons");
        for (File file : dungeonsDirectory.listFiles(new YamlFileFilter())) {
            Dungeon dungeon = new DungeonImpl(file);
            dungeons.put(file.getName().replace(".yml", ""), dungeon);
        }
        return dungeons;
    }

    public Dungeon getDungeon(String name) {
        File dungeonsDirectory = new File(plugin.getDataFolder(), "dungeons");
        File dungeonFile = new File(dungeonsDirectory, name + ".yml");
        return new DungeonImpl(dungeonFile);
    }

    public void addDungeon(String name, Dungeon dungeon) {
        File dungeonsDirectory = new File(plugin.getDataFolder(), "dungeons");
        File dungeonFile = new File(dungeonsDirectory, name + ".yml");
        YamlConfiguration dungeonConfig = new YamlConfiguration();
        dungeonConfig.set("dungeon", dungeon);
        try {
            dungeonConfig.save(dungeonFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
