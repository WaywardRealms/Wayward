package net.wayward_realms.waywardmoderation;

import net.wayward_realms.waywardlib.moderation.Warning;
import org.bukkit.OfflinePlayer;

import java.util.*;

public class WarningManager {

    private Map<String, Set<Warning>> warnings = new HashMap<>();

    public Collection<Warning> getWarnings(OfflinePlayer player) {
        if (warnings.get(player.getName()) == null) {
            warnings.put(player.getName(), new HashSet<Warning>());
        }
        return warnings.get(player.getName());
    }

    public void addWarning(OfflinePlayer player, Warning warning) {
        getWarnings(player).add(warning);
    }

    public void removeWarning(OfflinePlayer player, Warning warning) {
        getWarnings(player).remove(warning);
    }

}
