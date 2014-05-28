package net.wayward_realms.waywardessentials.kit;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import net.wayward_realms.waywardlib.essentials.Kit;
import net.wayward_realms.waywardlib.util.file.filter.YamlFileFilter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class KitManager {

    private WaywardEssentials plugin;

    public KitManager(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    public Map<String, Kit> getKits() {
        Map<String, Kit> kits = new HashMap<>();
        File kitDirectory = new File(plugin.getDataFolder(), "kits");
        for (File file : kitDirectory.listFiles(new YamlFileFilter())) {
            YamlConfiguration kitConfig = YamlConfiguration.loadConfiguration(file);
            kits.put(file.getName().replace(".yml", ""), (Kit) kitConfig.get("kit"));
        }
        return kits;
    }

    public Kit getKit(String name) {
        File kitDirectory = new File(plugin.getDataFolder(), "kits");
        File kitFile = new File(kitDirectory, name.toLowerCase() + ".yml");
        YamlConfiguration kitConfig = YamlConfiguration.loadConfiguration(kitFile);
        return (Kit) kitConfig.get("kit");
    }

    public void addKit(Kit kit) {
        File kitDirectory = new File(plugin.getDataFolder(), "kits");
        File kitFile = new File(kitDirectory, kit.getName().toLowerCase() + ".yml");
        YamlConfiguration kitConfig = YamlConfiguration.loadConfiguration(kitFile);
        kitConfig.set("kit", kit);
        try {
            kitConfig.save(kitFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void removeKit(Kit kit) {
        File kitDirectory = new File(plugin.getDataFolder(), "kits");
        File kitFile = new File(kitDirectory, kit.getName().toLowerCase() + ".yml");
        if (kitFile.exists()) {
            kitFile.delete();
        }
    }

}
