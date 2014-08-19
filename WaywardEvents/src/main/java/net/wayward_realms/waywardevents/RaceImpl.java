package net.wayward_realms.waywardevents;

import net.wayward_realms.waywardlib.character.Race;
import net.wayward_realms.waywardlib.skills.Stat;
import net.wayward_realms.waywardlib.events.EventsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;

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
        this.file = new File(new File(plugin.getDataFolder(), "races"), name.toLowerCase() + ".yml");
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
    public int getStatBonus(Stat stat) {
        return 0;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("name", getName());
        return serialised;
    }

    public static RaceImpl deserialize(Map<String, Object> serialised) {
        RegisteredServiceProvider<EventsPlugin> eventsPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(EventsPlugin.class);
        if (eventsPluginProvider != null) {
            EventsPlugin eventsPlugin = eventsPluginProvider.getProvider();
            if (eventsPlugin instanceof WaywardEvents) {
                return new RaceImpl((WaywardEvents) eventsPlugin, (String) serialised.get("name"));
            }
        }
        return null;
    }
}
