package net.wayward_realms.waywardevents;

import net.wayward_realms.waywardlib.events.Dungeon;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

public class DungeonImpl implements Dungeon, ConfigurationSerializable {

    private Set<String> dungeonMasters = new HashSet<>();
    private Set<String> players = new HashSet<>();
    private boolean active;

    @Override
    public Collection<OfflinePlayer> getDungeonMasters() {
        Set<OfflinePlayer> dungeonMasters = new HashSet<>();
        for (String dungeonMasterName : this.dungeonMasters) {
            dungeonMasters.add(Bukkit.getServer().getOfflinePlayer(dungeonMasterName));
        }
        return dungeonMasters;
    }

    @Override
    public void addDungeonMaster(OfflinePlayer dungeonMaster) {
        dungeonMasters.add(dungeonMaster.getName());
    }

    @Override
    public void removeDungeonMaster(OfflinePlayer dungeonMaster) {
        dungeonMasters.remove(dungeonMaster.getName());
    }

    @Override
    public Collection<OfflinePlayer> getPlayers() {
        Set<OfflinePlayer> players = new HashSet<>();
        for (String playerName : this.players) {
            players.add(Bukkit.getServer().getOfflinePlayer(playerName));
        }
        return players;
    }

    @Override
    public void addPlayer(OfflinePlayer player) {
        players.add(player.getName());
    }

    @Override
    public void removePlayer(OfflinePlayer player) {
        players.remove(player.getName());
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("dungeon-masters", dungeonMasters);
        serialised.put("players", players);
        serialised.put("active", active);
        return serialised;
    }

    @SuppressWarnings("unchecked")
    public static DungeonImpl deserialize(Map<String, Object> serialised) {
        DungeonImpl deserialised = new DungeonImpl();
        deserialised.dungeonMasters.addAll((Set<String>) serialised.get("dungeon-masters"));
        deserialised.players.addAll((Set<String>) serialised.get("players"));
        deserialised.active = (Boolean) serialised.get("active");
        return deserialised;
    }

}
