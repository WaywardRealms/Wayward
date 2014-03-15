package net.wayward_realms.waywardcharacters;

import net.wayward_realms.waywardlib.character.Race;
import net.wayward_realms.waywardlib.essentials.Kit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class RaceImpl implements Race, ConfigurationSerializable {

    private String name;
    private Kit kit;

    public RaceImpl(String name) {
        this(name, new RaceKit());
    }

    public RaceImpl(String name, Kit kit) {
        this.name = name;
        this.kit = kit;
    }

    @Override
    public String getName() {
        return name;
    }

    public Kit getKit() {
        return kit;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("name", name);
        serialised.put("kit", kit);
        return serialised;
    }

    public static RaceImpl deserialize(Map<String, Object> serialised) {
        return new RaceImpl((String) serialised.get("name"), (Kit) serialised.get("kit"));
    }

}
