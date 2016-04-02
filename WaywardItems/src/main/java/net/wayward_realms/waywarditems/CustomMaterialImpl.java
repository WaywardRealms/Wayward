package net.wayward_realms.waywarditems;

import net.wayward_realms.waywardlib.items.CustomMaterial;
import net.wayward_realms.waywardlib.util.serialisation.SerialisableRecipe;
import net.wayward_realms.waywardlib.util.serialisation.SerialisableShapedRecipe;
import net.wayward_realms.waywardlib.util.serialisation.SerialisableShapelessRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class CustomMaterialImpl implements CustomMaterial {

    private String name;
    private Material minecraftMaterial;
    private Recipe recipe;

    public CustomMaterialImpl(String name, Material minecraftMaterial, Recipe recipe) {
        this.name = name;
        this.minecraftMaterial = minecraftMaterial;
        this.recipe = recipe;
    }

    public CustomMaterialImpl(String name, Material minecraftMaterial, String[] recipeShape, Map<Character, ItemStack> recipeIngredients) {
        this.name = name;
        this.minecraftMaterial = minecraftMaterial;
        this.recipe = new ShapedRecipe(new CustomItemStackImpl(this, 1).toMinecraftItemStack());
        try {
            Field shapeField = recipe.getClass().getDeclaredField("rows");
            shapeField.setAccessible(true);
            shapeField.set(recipe, recipeShape);
            Field ingredientsField = recipe.getClass().getDeclaredField("ingredients");
            ingredientsField.setAccessible(true);
            ingredientsField.set(recipe, recipeIngredients);
        } catch (IllegalAccessException | NoSuchFieldException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Material getMinecraftMaterial() {
        return minecraftMaterial;
    }

    @Override
    public boolean isMaterial(ItemStack itemStack) {
        if (itemStack.hasItemMeta()) {
            if (itemStack.getItemMeta().hasDisplayName()) {
                return itemStack.getItemMeta().getDisplayName().equals(name) && itemStack.getType() == minecraftMaterial;
            }
        }
        return false;
    }

    @Override
    public Recipe getRecipe() {
        return recipe;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("name", name);
        serialised.put("material", minecraftMaterial.name());
        if (recipe != null) {
            serialised.put("recipe", recipe instanceof ShapedRecipe ? new SerialisableShapedRecipe((ShapedRecipe) recipe) : new SerialisableShapelessRecipe((ShapelessRecipe) recipe));
        }
        return serialised;
    }

    public static CustomMaterialImpl deserialize(Map<String, Object> serialised) {
        if (serialised.get("recipe") != null) {
            return new CustomMaterialImpl(
                    (String) serialised.get("name"),
                    Material.getMaterial((String) serialised.get("material")),
                    ((SerialisableRecipe) serialised.get("recipe")).toRecipe()
            );
        } else {
            return new CustomMaterialImpl(
                    (String) serialised.get("name"),
                    Material.getMaterial((String) serialised.get("material")),
                    null
            );
        }
    }

}
