package net.wayward_realms.waywardmoderation;

import net.wayward_realms.waywardlib.moderation.Warning;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
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
        YamlConfiguration warningsSave = YamlConfiguration.loadConfiguration(warningsFile);
        return (List<Warning>) warningsSave.getList("warnings");
    }

    public void addWarning(OfflinePlayer player, Warning warning) {
        File warningsDirectory = new File(plugin.getDataFolder(), "warnings");
        File warningsFile = new File(warningsDirectory, player.getName() + ".yml");
        YamlConfiguration warningsSave = YamlConfiguration.loadConfiguration(warningsFile);
        List<Warning> warnings = (List<Warning>) warningsSave.getList("warnings");
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
        YamlConfiguration warningsSave = YamlConfiguration.loadConfiguration(warningsFile);
        List<Warning> warnings = (List<Warning>) warningsSave.getList("warnings");
        warnings.remove(warning);
        warningsSave.set("warnings", warnings);
        try {
            warningsSave.save(warningsFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
