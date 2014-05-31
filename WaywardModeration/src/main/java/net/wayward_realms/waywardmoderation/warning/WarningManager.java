package net.wayward_realms.waywardmoderation.warning;

import net.wayward_realms.waywardlib.moderation.Warning;
import net.wayward_realms.waywardmoderation.WaywardModeration;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WarningManager {

    private WaywardModeration plugin;

    public WarningManager(WaywardModeration plugin) {
        this.plugin = plugin;
    }

    public Collection<Warning> getWarnings(OfflinePlayer player) {
        File warningsDirectory = new File(plugin.getDataFolder(), "warnings");
        File warningsFile = new File(warningsDirectory, player.getName() + ".yml");
        if (warningsFile.exists()) {
            YamlConfiguration warningsSave = YamlConfiguration.loadConfiguration(warningsFile);
            return (List<Warning>) warningsSave.getList("warnings");
        } else {
            return new ArrayList<>();
        }
    }

    public void addWarning(OfflinePlayer player, Warning warning) {
        File warningsDirectory = new File(plugin.getDataFolder(), "warnings");
        File warningsFile = new File(warningsDirectory, player.getName() + ".yml");
        YamlConfiguration warningsSave;
        List<Warning> warnings;
        if (warningsFile.exists()) {
            warningsSave = YamlConfiguration.loadConfiguration(warningsFile);
            warnings = (List<Warning>) warningsSave.getList("warnings");
        } else {
            warningsSave = new YamlConfiguration();
            warnings = new ArrayList<>();
        }
        warnings.add(warning);
        warningsSave.set("warnings", warnings);
        try {
            warningsSave.save(warningsFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void removeWarning(OfflinePlayer player, Warning warning) {
        File warningsDirectory = new File(plugin.getDataFolder(), "warnings");
        File warningsFile = new File(warningsDirectory, player.getName() + ".yml");
        YamlConfiguration warningsSave;
        List<Warning> warnings;
        if (warningsFile.exists()) {
            warningsSave = YamlConfiguration.loadConfiguration(warningsFile);
            warnings = (List<Warning>) warningsSave.getList("warnings");
        } else {
            warningsSave = new YamlConfiguration();
            warnings = new ArrayList<>();
        }
        warnings.remove(warning);
        warningsSave.set("warnings", warnings);
        try {
            warningsSave.save(warningsFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
