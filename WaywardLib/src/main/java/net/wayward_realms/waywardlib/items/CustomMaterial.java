package net.wayward_realms.waywardlib.items;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

/**
 * Represents a custom material
 */
public interface CustomMaterial extends ConfigurationSerializable {

    /**
     * Gets the name of the material
     *
     * @return the name of the material, as it appears in =-game
     */
    public String getName();

    /**
     * Gets the Minecraft material used to represent this material
     *
     * @return the Minecraft material
     */
    public Material getMinecraftMaterial();

    /**
     * Checks if a given {@link ItemStack} is of this material
     *
     * @param itemStack the {@link ItemStack} to check
     * @return whether the {@link ItemStack} is of this material
     */
    public boolean isMaterial(ItemStack itemStack);

    /**
     * Gets the recipe used to craft this material
     *
     * @return the recipe used to craft the material, or null if none
     */
    public Recipe getRecipe();

}
