package net.wayward_realms.waywardcharacters;

import net.wayward_realms.waywardlib.essentials.Kit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RaceKit implements Kit {

    private Collection<ItemStack> items = new ArrayList<>();

    @Override
    public void give(Player player) {
        for (ItemStack item : items) {
            player.getInventory().addItem(item);
        }
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
        serialised.put("items", items);
        return serialised;
    }

    @SuppressWarnings("unchecked")
    public static RaceKit deserialize(Map<String, Object> serialised) {
        RaceKit deserialised = new RaceKit();
        deserialised.items = (Collection<ItemStack>) serialised.get("items");
        return deserialised;
    }

}
