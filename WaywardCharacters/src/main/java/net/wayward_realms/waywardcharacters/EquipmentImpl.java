package net.wayward_realms.waywardcharacters;

import net.wayward_realms.waywardlib.character.Equipment;
import net.wayward_realms.waywardlib.character.Pet;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquipmentImpl implements Equipment {

    private ItemStack onHandItem;
    private ItemStack offHandItem;
    private Pet pet;
    private ItemStack[] scrolls;

    public EquipmentImpl(ItemStack onHandItem, ItemStack offHandItem, Pet pet, ItemStack[] scrolls) {
        this.onHandItem = onHandItem;
        this.offHandItem = offHandItem;
        this.pet = pet;
        this.scrolls = new ItemStack[9];
        System.arraycopy(scrolls, 0, this.scrolls, 0, 9);
    }

    @Override
    public ItemStack getOnHandItem() {
        return onHandItem;
    }

    @Override
    public ItemStack getOffHandItem() {
        return offHandItem;
    }

    @Override
    public Pet getPet() {
        return pet;
    }

    @Override
    public ItemStack[] getScrolls() {
        return scrolls;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("onhand", onHandItem);
        serialised.put("offhand", offHandItem);
        serialised.put("pet", pet);
        serialised.put("scrolls", scrolls);
        return serialised;
    }

    public static EquipmentImpl deserialize(Map<String, Object> serialised) {
        return new EquipmentImpl(
                (ItemStack) serialised.get("onhand"),
                (ItemStack) serialised.get("offhand"),
                (Pet) serialised.get("pet"),
                serialised.get("scrolls") instanceof List<?> ? ((List<ItemStack>) serialised.get("scrolls")).toArray(new ItemStack[9]) : new ItemStack[9]
        );
    }

}
