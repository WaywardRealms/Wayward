package net.wayward_realms.waywardessentials.recipe;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class RecipeManager {

    private WaywardEssentials plugin;

    public RecipeManager(WaywardEssentials plugin) {
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
