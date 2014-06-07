package net.wayward_realms.waywardmonsters.drops;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MobDrop implements ConfigurationSerializable {

    private int minLevel;
    private int maxLevel;
    private int chance;
    private ItemStack drop;

    private MobDrop() {}

    public MobDrop(int minLevel, int maxLevel, int chance, ItemStack drop) {
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.chance = chance;
        this.drop = drop;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getChance() {
        return chance;
    }

    public ItemStack getDrop() {
        return drop;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("min-level", minLevel);
        serialised.put("max-level", maxLevel);
        serialised.put("chance", chance);
        serialised.put("drop", drop);
        return serialised;
    }

    public static MobDrop deserialize(Map<String, Object> serialised) {
        MobDrop deserialised = new MobDrop();
        deserialised.minLevel = (int) serialised.get("min-level");
        deserialised.maxLevel = (int) serialised.get("max-level");
        deserialised.chance = (int) serialised.get("chance");
        deserialised.drop = (ItemStack) serialised.get("drop");
        return deserialised;
    }

}
