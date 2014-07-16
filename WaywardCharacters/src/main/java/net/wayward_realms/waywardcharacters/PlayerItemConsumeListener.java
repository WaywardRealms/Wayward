package net.wayward_realms.waywardcharacters;

import net.wayward_realms.waywardlib.character.Character;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static org.bukkit.block.Biome.*;

public class PlayerItemConsumeListener implements Listener {

    private WaywardCharacters plugin;

    public PlayerItemConsumeListener(WaywardCharacters plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        Character character = plugin.getActiveCharacter(player);
        if (event.getItem().getType() == Material.POTION) {
            if (event.getItem().hasItemMeta()) {
                List<String> biomeStringList = event.getItem().getItemMeta().getLore();
                String biomeString = biomeStringList.get(1);
                Biome biome = plugin.convertBiomeFromString(biomeString);
                if (biome != null) {
                    boolean isSafe = plugin.isSafeWater(biome);
                    if (!isSafe){
                        player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 1500, 2), true);
                        player.sendMessage(ChatColor.RED + "You feel sick. Perhaps it was the water.");
                        if (biome == OCEAN || biome == BEACH) {
                            character.setThirst(character.getThirst() - 2);
                            player.sendMessage(ChatColor.RED + "You suddenly feel dehydrated from drinking salt water.");
                            player.sendMessage(ChatColor.RED + "Thirst: -2" + ChatColor.GRAY + " (Total: " + character.getThirst() + ")");
                        }
                    }
                } else {
                    if (event.getItem().getItemMeta().hasDisplayName()) {
                        if (event.getItem().getItemMeta().getDisplayName().equals("Masheek")) {
                            if (event.getItem().getItemMeta().hasLore()) {
                                if (event.getItem().getItemMeta().getLore().contains("+5 mana")) {
                                    character.setMana(Math.min(character.getMana() + 5, character.getMaxMana()));
                                    event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "+5 mana" + ChatColor.GRAY + " (" + character.getMana() + "/" + character.getMaxMana() + ")");
                                }
                            }
                        }
                    }
                }
            } else {
                character.setThirst(character.getThirst() + 5);
                player.sendMessage(ChatColor.GREEN + "Thirst: +5" + ChatColor.GRAY + " (Total: " + character.getThirst() + ")");
            }
        } else if (event.getItem().getType() == Material.MILK_BUCKET) {
            character.setThirst(character.getThirst() + 5);
            event.getPlayer().sendMessage(ChatColor.GREEN + "Thirst: +5 " + ChatColor.GRAY + "(Total: " + character.getThirst() + ")");
        }
        if (event.getItem().getType() == Material.ROTTEN_FLESH) {
            Character character = plugin.getActiveCharacter(event.getPlayer());
            character.setHealth(character.getHealth() - 5);
            event.getPlayer().sendMessage(ChatColor.RED + "Health: -5 " + ChatColor.GRAY + "(Total: " + character.getHealth() + ")");
        }
    }

}