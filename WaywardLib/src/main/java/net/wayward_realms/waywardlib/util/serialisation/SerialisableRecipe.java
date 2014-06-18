package net.wayward_realms.waywardlib.util.serialisation;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.Recipe;

/**
 * Aids in the serialisation of recipes
 *
 */
public interface SerialisableRecipe extends ConfigurationSerializable {

    /**
     * Converts the serialisable recipe to a recipe
     *
     * @return the recipe
     */
    public Recipe toRecipe();

}
