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
        setupLeashRecipe();
        setupIronHorseArmourRecipe();
        setupGoldHorseArmourRecipe();
        setupDiamondHorseArmourRecipe();
    }

    private void setupSaddleRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(new ItemStack(Material.SADDLE));
        recipe.shape("lll", "sls", " i ").setIngredient('l', Material.LEATHER).setIngredient('s', Material.STRING).setIngredient('i', Material.IRON_INGOT);
        plugin.getServer().addRecipe(recipe);
    }

    private void setupLeashRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(new ItemStack(Material.LEASH));
        recipe.shape(" ss", " ss", "s  ").setIngredient('s', Material.STRING);
        plugin.getServer().addRecipe(recipe);
    }

    private void setupIronHorseArmourRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(new ItemStack(Material.IRON_BARDING));
        recipe.shape("  i", "lIl", "i i").setIngredient('i', Material.IRON_INGOT).setIngredient('l', Material.LEATHER).setIngredient('I', Material.IRON_BLOCK);
        plugin.getServer().addRecipe(recipe);
    }

    private void setupGoldHorseArmourRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(new ItemStack(Material.GOLD_BARDING));
        recipe.shape("  g", "lGl", "g g").setIngredient('g', Material.GOLD_INGOT).setIngredient('l', Material.LEATHER).setIngredient('G', Material.GOLD_BLOCK);
        plugin.getServer().addRecipe(recipe);
    }

    private void setupDiamondHorseArmourRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(new ItemStack(Material.DIAMOND_BARDING));
        recipe.shape("  d", "lDl", "d d").setIngredient('d', Material.DIAMOND).setIngredient('l', Material.LEATHER).setIngredient('D', Material.DIAMOND_BLOCK);
        plugin.getServer().addRecipe(recipe);
    }

}
