package net.wayward_realms.waywarditems;

import com.sk89q.jinglenote.JingleNoteManager;
import net.wayward_realms.waywarditems.recipe.RecipeManager;
import net.wayward_realms.waywardlib.items.CustomItemStack;
import net.wayward_realms.waywardlib.items.CustomMaterial;
import net.wayward_realms.waywardlib.items.ItemsPlugin;
import net.wayward_realms.waywardlib.util.file.filter.YamlFileFilter;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class WaywardItems extends JavaPlugin implements ItemsPlugin {

    private RecipeManager recipeManager;

    private JingleNoteManager jingleNoteManager;

    @Override
    public void onEnable() {
        recipeManager = new RecipeManager(this);
        recipeManager.setupRecipes();
        saveResource("music/canon.mid", false);
        jingleNoteManager = new JingleNoteManager();
        registerListeners(new EntityShootBowListener(this), new PlayerQuitListener(this));
        Map<Character, ItemStack> harpIngredients = new HashMap<>();
        harpIngredients.put('s', new ItemStack(Material.STRING));
        harpIngredients.put('S', new ItemStack(Material.STICK));
        CustomMaterial harp = new CustomMaterialImpl("Harp", Material.BOW, new String[] {"ssS", "ssS", "ssS"}, harpIngredients);
        addMaterial(harp);
        Map<Character, ItemStack> bandageIngredients = new HashMap<>();
        bandageIngredients.put('w', new ItemStack(Material.WOOL, 1));
        CustomMaterial bandage = new CustomMaterialImpl("Bandage", Material.PAPER, new String[] {"www"}, bandageIngredients);
        addMaterial(bandage);
        ShapelessRecipe bandageRecipe = new ShapelessRecipe(new CustomItemStackImpl(bandage, 3).toMinecraftItemStack());
        bandageRecipe.addIngredient(Material.LEATHER_CHESTPLATE);
        getServer().addRecipe(bandageRecipe);
    }

    public void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    private Map<String, CustomMaterial> customMaterials = new HashMap<>();

    @Override
    public Collection<? extends CustomMaterial> getMaterials() {
        return customMaterials.values();
    }

    @Override
    public CustomMaterial getMaterial(String name) {
        return customMaterials.get(name);
    }

    @Override
    public void addMaterial(CustomMaterial material) {
        customMaterials.put(material.getName(), material);
        if (material.getRecipe() != null) {
            getServer().addRecipe(material.getRecipe());
        }
    }

    @Override
    public void removeMaterial(CustomMaterial material) {
        if (material.getRecipe() != null) {
            getServer().resetRecipes();
            for (CustomMaterial customMaterial : customMaterials.values()) {
                if (customMaterial != material) {
                    getServer().addRecipe(customMaterial.getRecipe());
                }
            }
        }
        customMaterials.remove(material.getName());
    }

    @Override
    public CustomItemStack createNewItemStack(CustomMaterial material, int amount) {
        return new CustomItemStackImpl(material, amount);
    }

    @Override
    public CustomItemStack fromMinecraftItemStack(ItemStack minecraftStack) {
        return new CustomItemStackImpl(minecraftStack);
    }

    @Override
    public String getPrefix() {
        return "";
    }

    @Override
    public void loadState() {
        File customMaterialDirectory = new File(getDataFolder(), "materials");
        if (customMaterialDirectory.exists()) {
            if (customMaterialDirectory.isDirectory()) {
                for (File file : customMaterialDirectory.listFiles(new YamlFileFilter())) {
                    YamlConfiguration materialConfig = new YamlConfiguration();
                    try {
                        materialConfig.load(file);
                    } catch (IOException | InvalidConfigurationException exception) {
                        exception.printStackTrace();
                    }
                    addMaterial((CustomMaterial) materialConfig.get("material"));
                }
            }
        } else {
            customMaterialDirectory.mkdir();
            Map<Character, ItemStack> ingredientMap = new HashMap<>();
            ingredientMap.put('s', new ItemStack(Material.SEEDS));
            ingredientMap.put('c', new ItemStack(Material.COBBLESTONE));
            CustomMaterial exampleMaterial = new CustomMaterialImpl("Example material", Material.MOSSY_COBBLESTONE, new String[] {"sss", "scs", "sss"}, ingredientMap);
            File exampleMaterialFile = new File(customMaterialDirectory, "example.yml");
            YamlConfiguration exampleMaterialConfig = new YamlConfiguration();
            exampleMaterialConfig.set("material", exampleMaterial);
            try {
                exampleMaterialConfig.save(exampleMaterialFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void saveState() {

    }

    public JingleNoteManager getJingleNoteManager() {
        return jingleNoteManager;
    }
}
