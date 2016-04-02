package net.wayward_realms.waywardlib.items;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

public interface CustomItemStack extends ConfigurationSerializable {

    public CustomMaterial getMaterial();

    public int getAmount();

    public ItemStack toMinecraftItemStack();

}
