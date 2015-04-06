package net.wayward_realms.waywarddonations;

import net.wayward_realms.waywardlib.essentials.Kit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class KitImpl implements Kit, ConfigurationSerializable {

    private String name;
    private Collection<ItemStack> items = new ArrayList<>();

    @Override
    public void give(Player player) {
        for (ItemStack item : items) {
            player.getInventory().addItem(item);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Collection<ItemStack> getItems() {
        return items;
    }

    @Override
    public void addItem(ItemStack item) {
        items.add(item);
    }

    @Override
    public void removeItem(ItemStack item) {
        items.remove(item);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("name", name);
        serialised.put("items", items);
        return serialised;
    }

    @SuppressWarnings("unchecked")
    public static KitImpl deserialize(Map<String, Object> serialised) {
        KitImpl deserialised = new KitImpl();
        deserialised.items = (Collection<ItemStack>) serialised.get("items");
        deserialised.name = serialised.containsKey("name") ? (String) serialised.get("name") : "unknownkit";
        return deserialised;
    }

}
