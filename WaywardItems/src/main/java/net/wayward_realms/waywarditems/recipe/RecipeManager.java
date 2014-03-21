package net.wayward_realms.waywarditems.recipe;

import net.wayward_realms.waywarditems.WaywardItems;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class RecipeManager {

    private WaywardItems plugin;

    public RecipeManager(WaywardItems plugin) {
        this.plugin = plugin;
    }

    public void setupRecipes() {
        setupSaddleRecipe();
    }

    private void setupSaddleRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(new ItemStack(Material.SADDLE));
        recipe.shape("lll", "sls", " i ").setIngredient('l', Material.LEATHER).setIngredient('s', Material.STRING).setIngredient('i', Material.IRON_INGOT);
        plugin.getServer().addRecipe(recipe);
    }

}
