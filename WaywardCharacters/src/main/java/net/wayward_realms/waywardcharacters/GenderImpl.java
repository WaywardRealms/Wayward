package net.wayward_realms.waywardcharacters;

import net.wayward_realms.waywardlib.character.Gender;

import java.util.HashMap;
import java.util.Map;

public class GenderImpl implements Gender {

    private String name;

    public GenderImpl(String name) {
        this.name = name;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("name", name);
        return serialised;
    }

    public static GenderImpl deserialize(Map<String, Object> serialised) {
        return new GenderImpl((String) serialised.get("name"));
    }

    @Override
    public String getName() {
        return name;
    }

}
