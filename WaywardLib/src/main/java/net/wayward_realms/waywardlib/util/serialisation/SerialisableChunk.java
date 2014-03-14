package net.wayward_realms.waywardlib.util.serialisation;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

/**
 * Aids in the serialisation of chunks
 *
 */
public class SerialisableChunk implements ConfigurationSerializable {

    private String world;
    private int x;
    private int z;

    private SerialisableChunk() {}

    public SerialisableChunk(Chunk chunk) {
        this(chunk.getWorld(), chunk.getX(), chunk.getZ());
    }

    public SerialisableChunk(World world, int x, int z) {
        this(world.getName(), x, z);
    }

    public SerialisableChunk(String world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    /**
     * Converts the SerialisableChunk back into a Bukkit Chunk
     *
     * @return the original Chunk
     */
    public Chunk toChunk() {
        return Bukkit.getWorld(world).getChunkAt(x, z);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("world", world);
        serialised.put("x", x);
        serialised.put("z", z);
        return serialised;
    }

    public static SerialisableChunk deserialize(Map<String, Object> serialised) {
        SerialisableChunk deserialised = new SerialisableChunk();
        deserialised.world = (String) serialised.get("world");
        deserialised.x = (Integer) serialised.get("x");
        deserialised.z = (Integer) serialised.get("z");
        return deserialised;
    }

}
