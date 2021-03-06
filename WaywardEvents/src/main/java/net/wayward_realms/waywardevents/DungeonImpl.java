package net.wayward_realms.waywardevents;

import net.wayward_realms.waywardlib.events.Dungeon;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DungeonImpl implements Dungeon, ConfigurationSerializable {

    private File file;

    private Set<UUID> dungeonMasters = new HashSet<>();
    private Set<UUID> players = new HashSet<>();
    private boolean active;

    public DungeonImpl() {}

    public DungeonImpl(File file) {
        this.file = file;
        load();
    }

    @Override
    public Collection<OfflinePlayer> getDungeonMasters() {
        Set<OfflinePlayer> dungeonMasters = new HashSet<>();
        for (UUID uuid : this.dungeonMasters) {
            dungeonMasters.add(Bukkit.getServer().getOfflinePlayer(uuid));
        }
        return dungeonMasters;
    }

    @Override
    public void addDungeonMaster(OfflinePlayer dungeonMaster) {
        dungeonMasters.add(dungeonMaster.getUniqueId());
        save();
    }

    @Override
    public void removeDungeonMaster(OfflinePlayer dungeonMaster) {
        dungeonMasters.remove(dungeonMaster.getUniqueId());
        save();
    }

    @Override
    public Collection<OfflinePlayer> getPlayers() {
        Set<OfflinePlayer> players = new HashSet<>();
        for (UUID uuid : this.players) {
            players.add(Bukkit.getServer().getOfflinePlayer(uuid));
        }
        return players;
    }

    @Override
    public void addPlayer(OfflinePlayer player) {
        players.add(player.getUniqueId());
        save();
    }

    @Override
    public void removePlayer(OfflinePlayer player) {
        players.remove(player.getUniqueId());
        save();
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
        save();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        Set<String> dungeonMasterUUIDStrings = new HashSet<>();
        for (UUID uuid : dungeonMasters) {
            dungeonMasterUUIDStrings.add(uuid.toString());
        }
        serialised.put("dungeon-masters", dungeonMasterUUIDStrings);
        Set<String> playerUUIDStrings = new HashSet<>();
        for (UUID uuid : players) {
            playerUUIDStrings.add(uuid.toString());
        }
        serialised.put("players", playerUUIDStrings);
        serialised.put("active", active);
        return serialised;
    }

    @SuppressWarnings("unchecked")
    public static DungeonImpl deserialize(Map<String, Object> serialised) {
        DungeonImpl deserialised = new DungeonImpl();
        Set<String> dungeonMasterUUIDs = (Set<String>) serialised.get("dungeon-masters");
        for (String dungeonMasterUUIDString : dungeonMasterUUIDs) {
            deserialised.dungeonMasters.add(UUID.fromString(dungeonMasterUUIDString));
        }
        Set<String> playerUUIDs = (Set<String>) serialised.get("players");
        for (String playerUUIDString : playerUUIDs) {
            deserialised.players.add(UUID.fromString(playerUUIDString));
        }
        deserialised.active = (Boolean) serialised.get("active");
        return deserialised;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void save() {
        YamlConfiguration config = new YamlConfiguration();
        config.set("dungeon", this);
        try {
            config.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void load() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        Dungeon dungeon = (Dungeon) config.get("dungeon");
        for (OfflinePlayer dungeonMaster : dungeon.getDungeonMasters()) {
            dungeonMasters.add(dungeonMaster.getUniqueId());
        }
        for (OfflinePlayer player : dungeon.getPlayers()) {
            dungeonMasters.add(player.getUniqueId());
        }
        active = dungeon.isActive();
    }

}
