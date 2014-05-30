package net.wayward_realms.waywardevents;

import net.wayward_realms.waywardlib.character.Race;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RaceImpl implements Race {

    private File file;

    public RaceImpl(File file) {
        this.file = file;
    }

    public RaceImpl(WaywardEvents plugin, String name) {
        this.file = new File(new File(plugin.getDataFolder(), "races"), name + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("name", name);
        try {
            config.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public RaceImpl(WaywardEvents plugin, Race race) {
        this(plugin, race.getName());
    }

    @Override
    public String getName() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        return config.getString("name");
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("name", getName());
        return serialised;
    }
}
