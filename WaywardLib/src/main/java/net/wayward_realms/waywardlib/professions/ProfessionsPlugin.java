package net.wayward_realms.waywardlib.professions;

import net.wayward_realms.waywardlib.WaywardPlugin;
import net.wayward_realms.waywardlib.character.Character;
import org.bukkit.Material;

/**
 * Represents a professions plugin
 */
public interface ProfessionsPlugin extends WaywardPlugin {

    /**
     * Gets the maximum craftable tool durability of a given type
     *
     * @param character the character
     * @param toolType the type of tool
     * @return the maximum durability a player may craft to
     */
    public int getMaxToolDurability(Character character, ToolType toolType);

    /**
     * Gets the efficiency, out of one hundred, the character has in crafting a specific material
     *
     * @param character the character
     * @param material the material
     * @return the efficiency the player has at crafting the material
     */
    public int getCraftEfficiency(Character character, Material material);

    /**
     * Gets the efficiency, out of one hundred, the character has in mining a specific material
     *
     * @param character the character
     * @param material the material
     * @return the efficiency
     */
    public int getMiningEfficiency(Character character, Material material);

    /**
     * Gets the efficiency, out of one hundred, the character has in brewing
     *
     * @param character the character
     * @return the efficiency
     */
    public int getBrewingEfficiency(Character character);

    /**
     * Gets the efficiency, out of one hundred, the character has in enchanting
     *
     * @param character the character
     * @return the efficiency
     */
    public int getEnchantEfficiency(Character character);

}
