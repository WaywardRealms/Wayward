package net.wayward_realms.waywardworldgen;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class Pocket implements ConfigurationSerializable {

    private Material type;
    private int maxSize;
    private int pocketsPerChunk;

    private Pocket() {}

    public Pocket(Material type, int maxSize, int pocketsPerChunk) {
        this.type = type;
        this.maxSize = maxSize;
        this.pocketsPerChunk = pocketsPerChunk;
    }

    public Material getType() {
        return type;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getPocketsPerChunk() {
        return pocketsPerChunk;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("type", type.toString());
        serialised.put("max-size", maxSize);
        serialised.put("pockets-per-chunk", pocketsPerChunk);
        return serialised;
    }

    public static Pocket deserialize(Map<String, Object> serialised) {
        Pocket deserialised = new Pocket();
        deserialised.type = Material.getMaterial((String) serialised.get("type"));
        deserialised.maxSize = (int) serialised.get("max-size");
        deserialised.pocketsPerChunk = (int) serialised.get("pockets-per-chunk");
        return deserialised;
    }

}
