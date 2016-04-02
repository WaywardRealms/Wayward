package net.wayward_realms.waywardlib.util.serialisation;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Aids in the serialisation of shaped recipes
 *
 */
public class SerialisableShapedRecipe implements SerialisableRecipe {

    private String[] shape;
    private ItemStack result;
    private Map<Character, ItemStack> ingredients = new HashMap<>();

    public SerialisableShapedRecipe(ShapedRecipe recipe) {
        this.shape = recipe.getShape();
        this.result = recipe.getResult();
        this.ingredients = recipe.getIngredientMap();
    }

    private SerialisableShapedRecipe() {}

    @Override
    public ShapedRecipe toRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(result);
        recipe.shape(shape);
        try {
            Field field = recipe.getClass().getDeclaredField("ingredients");
            field.setAccessible(true);
            field.set(recipe, ingredients);
        } catch (IllegalAccessException | NoSuchFieldException exception) {
            exception.printStackTrace();
        }
        return recipe;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("shape", shape);
        serialised.put("result", result);
        serialised.put("ingredients", ingredients);
        return serialised;
    }

    public static SerialisableShapedRecipe deserialize(Map<String, Object> serialised) {
        SerialisableShapedRecipe deserialised = new SerialisableShapedRecipe();
        List<String> shapeList = (List<String>) serialised.get("shape");
        deserialised.shape = shapeList.toArray(new String[shapeList.size()]);
        deserialised.result = (ItemStack) serialised.get("result");
        Map<String, ItemStack> stringIngredients = (Map<String, ItemStack>) serialised.get("ingredients");
        Map<Character, ItemStack> characterIngredients = new HashMap<>();
        for (Map.Entry<String, ItemStack> entry : stringIngredients.entrySet()) {
            characterIngredients.put(entry.getKey().charAt(0), entry.getValue());
        }
        deserialised.ingredients = characterIngredients;
        return deserialised;
    }

}
