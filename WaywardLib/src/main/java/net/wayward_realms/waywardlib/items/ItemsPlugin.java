package net.wayward_realms.waywardlib.items;

import net.wayward_realms.waywardlib.WaywardPlugin;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

/**
 * Represents an items plugin
 */
public interface ItemsPlugin extends WaywardPlugin {

    /**
     * Gets all the available custom materials
     *
     * @return a collection containing all materials
     */
    public Collection<? extends CustomMaterial> getMaterials();

    /**
     * Gets a custom material by name
     *
     * @param name the name of the material
     * @return the material
     */
    public CustomMaterial getMaterial(String name);

    /**
     * Adds a custom material
     *
     * @param material the custom material to add
     */
    public void addMaterial(CustomMaterial material);

    /**
     * Removes a custom material
     *
     * @param material the custom material to remove
     */
    public void removeMaterial(CustomMaterial material);

    /**
     * Creates a new {@link net.wayward_realms.waywardlib.items.CustomItemStack}
     *
     * @param material the material
     * @param amount the amount of the material
     * @return the {@link net.wayward_realms.waywardlib.items.CustomItemStack} created
     */
    public CustomItemStack createNewItemStack(CustomMaterial material, int amount);

    /**
     * Converts a Minecraft {@link org.bukkit.inventory.ItemStack} from a {@link net.wayward_realms.waywardlib.items.CustomItemStack}
     *
     * @param minecraftStack the minecraft {@link org.bukkit.inventory.ItemStack}
     * @return the {@link net.wayward_realms.waywardlib.items.CustomItemStack}
     */
    public CustomItemStack fromMinecraftItemStack(ItemStack minecraftStack);

}
