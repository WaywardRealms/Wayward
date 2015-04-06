package net.wayward_realms.waywardcharacters;

import net.wayward_realms.waywardlib.character.Race;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.essentials.Kit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class RaceImpl implements Race, ConfigurationSerializable {

    private String name;
    private Kit kit;
    private Map<Stat, Integer> statBonuses = new EnumMap<>(Stat.class);

    public RaceImpl(String name) {
        this(name, new RaceKit(), new EnumMap<Stat, Integer>(Stat.class));
    }

    public RaceImpl(String name, Kit kit, Map<Stat, Integer> statBonuses) {
        this.name = name;
        this.kit = kit;
        this.statBonuses.putAll(statBonuses);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getStatBonus(Stat stat) {
        return statBonuses.containsKey(stat) ? statBonuses.get(stat) : 0;
    }

    public Kit getKit() {
        return kit;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("name", name);
        serialised.put("kit", kit);
        for (Stat stat : Stat.values()) {
            serialised.put(stat.toString().toLowerCase() + "-bonus", getStatBonus(stat));
        }
        return serialised;
    }

    public static RaceImpl deserialize(Map<String, Object> serialised) {
        Map<Stat, Integer> statBonuses = new EnumMap<>(Stat.class);
        for (Stat stat : Stat.values()) {
            statBonuses.put(stat, serialised.containsKey(stat.toString().toLowerCase() + "-bonus") ? (int) serialised.get(stat.toString().toLowerCase() + "-bonus") : 0);
        }
        return new RaceImpl((String) serialised.get("name"), (Kit) serialised.get("kit"), statBonuses);
    }

}
