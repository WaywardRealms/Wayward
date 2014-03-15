package net.wayward_realms.waywardlib.items;

import org.bukkit.inventory.ItemStack;

public interface CustomItemStack {

    public CustomMaterial getMaterial();

    public int getAmount();

    public ItemStack toMinecraftItemStack();

}
