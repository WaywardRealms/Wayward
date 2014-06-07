package net.wayward_realms.waywardevents;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockDescriptor implements ConfigurationSerializable {

    private ItemStack[] items;
    private String description;

    public BlockDescriptor() {}

    public BlockDescriptor(String description, ItemStack... items) {
        this.description = description;
        this.items = items;
    }

    public ItemStack[] getItems() {
        return items;
    }

    public void setItems(ItemStack... items) {
        this.items = items;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        if (items != null) serialised.put("items", items);
        if (description != null) serialised.put("description", description);
        return serialised;
    }

    public static BlockDescriptor deserialize(Map<String, Object> serialised) {
        BlockDescriptor deserialised = new BlockDescriptor();
        if (serialised.containsKey("items")) {
            if (serialised.get("items") instanceof List<?>) {
                List<ItemStack> itemStackList = (List<ItemStack>) serialised.get("items");
                deserialised.items = itemStackList.toArray(new ItemStack[itemStackList.size()]);
            } else if (serialised.get("items") instanceof ItemStack[]) {
                deserialised.items = (ItemStack[]) serialised.get("items");
            }
        }
        if (serialised.containsKey("description")) deserialised.description = (String) serialised.get("description");
        return deserialised;
    }

}
