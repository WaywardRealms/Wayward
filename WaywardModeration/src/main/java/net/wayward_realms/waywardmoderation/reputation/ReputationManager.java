package net.wayward_realms.waywardmoderation.reputation;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ReputationManager {

    public static final int MAX_REPUTATION = 25;
    private File reputationConfigFile;
    private FileConfiguration reputationConfig;

    public ReputationManager(File reputationConfigFile) {
        this.reputationConfigFile = reputationConfigFile;
        reputationConfig = new YamlConfiguration();
        if (reputationConfigFile.exists()) {
            try {
                reputationConfig.load(reputationConfigFile);
            } catch (IOException | InvalidConfigurationException exception) {
                exception.printStackTrace();
            }
        }
    }

    public int getReputation(OfflinePlayer player) {
        if (reputationConfig.get("players." + player.getName() + ".reputation") == null) {
            reputationConfig.set("players." + player.getName() + ".reputation", 0);
        }
        return reputationConfig.getInt("players." + player.getName() + ".reputation");
    }

    public void setReputation(OfflinePlayer player, int reputation) {
        reputationConfig.set("players." + player.getName() + ".reputation", reputation);
        try {
            reputationConfig.save(reputationConfigFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public boolean hasSetReputation(OfflinePlayer setter, OfflinePlayer player) {
        return reputationConfig.get("players." + setter.getName() + ".given." + player.getName()) != null;
    }

    public int getGivenReputation(OfflinePlayer setter, OfflinePlayer player) {
        return reputationConfig.getInt("players." + setter.getName() + ".given." + player.getName());
    }

    public void setGivenReputation(OfflinePlayer setter, OfflinePlayer player, int amount) {
        reputationConfig.set("players." + setter.getName() + ".given." + player.getName(), amount);
        if (amount > 0 && getReputation(player) < MAX_REPUTATION) {
            setReputation(player, getReputation(player) + amount);
        }
        if (amount < 0 && getReputation(player) > -MAX_REPUTATION) {
            setReputation(player, getReputation(player) - amount);
        }
    }

}
