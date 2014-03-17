package net.wayward_realms.waywardessentials.kit;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import net.wayward_realms.waywardlib.essentials.Kit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class KitManager {

    private Map<String, Kit> kits = new HashMap<>();
    private WaywardEssentials plugin;

    public KitManager(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    public Map<String, Kit> getKits() {
        return kits;
    }

    public Kit getKit(String name) {
        return kits.get(name);
    }

    public void addKit(Kit kit) {
        kits.put(kit.toString(), kit);
    }

    public void removeKit(Kit kit) {
        String kitName = "";
        for (Map.Entry<String, Kit> entry : kits.entrySet()) {
            if (entry.getValue() == kit) {
                kitName = entry.getKey();
            }
        }
        if (!kitName.equals("")) {
            kits.remove(kitName);
        }
    }

    public void save() {
        File kitFile = new File(plugin.getDataFolder().getPath() + File.separator + "kits.yml");
        YamlConfiguration kitConfig = new YamlConfiguration();
        for (String kitName : kits.keySet()) {
            kitConfig.set(kitName, kits.get(kitName));
        }
        try {
            kitConfig.save(kitFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void load() {
        File kitFile = new File(plugin.getDataFolder().getPath() + File.separator + "kits.yml");
        if (kitFile.exists()) {
            YamlConfiguration kitConfig = new YamlConfiguration();
            try {
                kitConfig.load(kitFile);
            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
            } catch (IOException exception) {
                exception.printStackTrace();
            } catch (InvalidConfigurationException exception) {
                exception.printStackTrace();
            }
            for (String kitName : kitConfig.getKeys(false)) {
                kits.put(kitName, (Kit) kitConfig.get(kitName));
            }
        }
    }

}
