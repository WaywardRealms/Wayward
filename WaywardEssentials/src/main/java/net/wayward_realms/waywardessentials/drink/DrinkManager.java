package net.wayward_realms.waywardessentials.drink;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Character;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class DrinkManager {

    private WaywardEssentials plugin;

    private Map<Long, Integer> drunkenness = new HashMap<>();

    public DrinkManager(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    public int getDrunkenness(Character character) {
        return drunkenness.get(character.getId()) != null ? drunkenness.get(character.getId()) : 0;
    }

    public void setDrunkenness(final Character character, int drunkenness) {
        for (int i = 1; i <= drunkenness - getDrunkenness(character); i++) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    DrinkManager.this.setDrunkenness(character, getDrunkenness(character) - 1);
                }
            }, i * 6000L);
        }
        this.drunkenness.put(character.getId(), drunkenness);
        if (getDrunkenness(character) == 100) {
            character.setDead(true);
        }
        if (character.getPlayer().getPlayer().isOnline()) {
            if (getDrunkenness(character) == 100) {
                character.getPlayer().getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + character.getName() + " died from alcohol poisoning.");
            } else if (getDrunkenness(character) >= 75) {
                character.getPlayer().getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + character.getName() + " is feeling very ill.");
            } else if (getDrunkenness(character) >= 50) {
                character.getPlayer().getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + character.getName() + " is feeling very drunk.");
            } else if (getDrunkenness(character) >= 10) {
                character.getPlayer().getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + character.getName() + " is feeling quite drunk.");
            } else if (getDrunkenness(character) >= 5) {
                character.getPlayer().getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + character.getName() + " is feeling a bit tipsy.");
            }
        }
        if (character.getPlayer().isOnline()) {
            applyPotionEffects(character.getPlayer().getPlayer(), drunkenness);
        }
    }

    public void increaseDrunkenness(Character character, int amount) {
        setDrunkenness(character, getDrunkenness(character) + amount);
    }

    private void applyPotionEffects(Player player, int drunkenness) {
        if (drunkenness >= 75) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, drunkenness * 10, drunkenness / 10));
        }
        if (drunkenness >= 50) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, drunkenness * 10, drunkenness / 10));
        }
        if (drunkenness >= 10) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, drunkenness * 10, drunkenness / 10));
        }
        if (drunkenness > 0) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, drunkenness * 20, drunkenness / 10));
        }
    }

    public int getDrunkenness(OfflinePlayer player) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            return getDrunkenness(characterPlugin.getActiveCharacter(player));
        }
        return 0;
    }

    public void setDrunkenness(OfflinePlayer player, int drunkenness) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            setDrunkenness(characterPlugin.getActiveCharacter(player), drunkenness);
        }
    }

    public void increaseDrunkenness(OfflinePlayer player, int amount) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            increaseDrunkenness(characterPlugin.getActiveCharacter(player), amount);
        }
    }

    public void setupRecipes() {
        ShapelessRecipe beerRecipe = new ShapelessRecipe(getDrink("Beer")).addIngredient(Material.WHEAT).addIngredient(Material.POTION).addIngredient(Material.FERMENTED_SPIDER_EYE).addIngredient(Material.SUGAR);
        plugin.getServer().addRecipe(beerRecipe);
        ShapelessRecipe wineRecipe = new ShapelessRecipe(getDrink("Wine")).addIngredient(Material.MELON).addIngredient(Material.POTION);
        plugin.getServer().addRecipe(wineRecipe);
        ShapelessRecipe sherryRecipe = new ShapelessRecipe(getDrink("Sherry")).addIngredient(5, Material.MELON).addIngredient(Material.POTION);
        plugin.getServer().addRecipe(sherryRecipe);
        ShapelessRecipe vodkaRecipe = new ShapelessRecipe(getDrink("Vodka")).addIngredient(Material.POISONOUS_POTATO).addIngredient(Material.POTION);
        plugin.getServer().addRecipe(vodkaRecipe);
        ShapelessRecipe whiskyRecipe = new ShapelessRecipe(getDrink("Whisky")).addIngredient(5, Material.WHEAT).addIngredient(Material.POTION);
        plugin.getServer().addRecipe(whiskyRecipe);
        ShapelessRecipe rumRecipe = new ShapelessRecipe(getDrink("Rum")).addIngredient(Material.SUGAR_CANE).addIngredient(Material.POTION);
        plugin.getServer().addRecipe(rumRecipe);
        ShapelessRecipe ciderRecipe = new ShapelessRecipe(getDrink("Cider")).addIngredient(Material.APPLE).addIngredient(Material.POTION);
        plugin.getServer().addRecipe(ciderRecipe);
    }

    public ItemStack getDrink(String name) {
        ItemStack drink = new ItemStack(Material.POTION);
        ItemMeta meta = drink.getItemMeta();
        meta.setDisplayName(name);
        drink.setItemMeta(meta);
        return drink;
    }

}
