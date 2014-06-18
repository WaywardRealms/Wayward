package net.wayward_realms.waywardprofessions;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.professions.ToolType;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InventoryClickListener implements Listener {

    private WaywardProfessions plugin;

    public InventoryClickListener(WaywardProfessions plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (event.getWhoClicked().getGameMode() != GameMode.CREATIVE) {
            if (event.getInventory().getType() == InventoryType.WORKBENCH || event.getInventory().getType() == InventoryType.CRAFTING) {
                if (event.getSlotType() == InventoryType.SlotType.RESULT) {
                    if (event.getAction() == InventoryAction.PICKUP_HALF
                            || event.getAction() == InventoryAction.PICKUP_ONE
                            || event.getAction() == InventoryAction.PICKUP_ALL
                            || event.getAction() == InventoryAction.PICKUP_SOME
                            || event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                        if (event.getCurrentItem() != null) {
                            if (!plugin.canGainCraftEfficiency(event.getCurrentItem().getType())) return;
                            if (ToolType.getToolType(event.getCurrentItem().getType()) != null) {
                                ToolType type = ToolType.getToolType(event.getCurrentItem().getType());
                                if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                                    event.setCancelled(true);
                                    return;
                                }
                                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                                if (characterPluginProvider != null) {
                                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                                    Character character = characterPlugin.getActiveCharacter((Player) event.getWhoClicked());
                                    Random random = new Random();
                                    plugin.setMaxToolDurability(character, type, Math.min(plugin.getMaxToolDurability(character, type) + (random.nextInt(100) <= 75 ? random.nextInt(3) + 1 : 0), event.getCurrentItem().getType().getMaxDurability()));
                                }
                            }
                            RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                            if (characterPluginProvider != null) {
                                CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                                Character character = characterPlugin.getActiveCharacter((Player) event.getWhoClicked());
                                Material material = event.getCurrentItem().getType();
                                Random random = new Random();
                                plugin.setCraftEfficiency(character, material, plugin.getCraftEfficiency(character, material) + (random.nextInt(100) <= 75 ? random.nextInt(3) + 1 : 0));
                            }
                        }
                    }
                }
            }
            if (event.getInventory().getType() == InventoryType.BREWING) {
                if (event.getSlotType() == InventoryType.SlotType.CRAFTING) {
                    if (event.getAction() == InventoryAction.PICKUP_HALF
                            || event.getAction() == InventoryAction.PICKUP_ONE
                            || event.getAction() == InventoryAction.PICKUP_ALL
                            || event.getAction() == InventoryAction.PICKUP_SOME
                            || event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                        if (characterPluginProvider != null) {
                            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                            Character character = characterPlugin.getActiveCharacter((Player) event.getWhoClicked());
                            if (!event.getCurrentItem().getItemMeta().hasLore() || !event.getCurrentItem().getItemMeta().getLore().contains("-")) {
                                Random random = new Random();
                                int brewingEfficiency = plugin.getBrewingEfficiency(character);
                                int amount = (int) ((double) (random.nextInt(100) <= 75 ? (brewingEfficiency > 10 ? random.nextInt(brewingEfficiency) : random.nextInt(10)) : 25) * (4D / 100D) * (double) event.getCurrentItem().getAmount());
                                event.getCurrentItem().setAmount(amount);
                                plugin.setBrewingEfficiency(character, plugin.getBrewingEfficiency(character) + (random.nextInt(100) <= 75 ? random.nextInt(3) + 1 : 0));
                                ItemMeta meta = event.getCurrentItem().getItemMeta();
                                List<String> lore = new ArrayList<>();
                                lore.add("-");
                                meta.setLore(lore);
                                event.getCurrentItem().setItemMeta(meta);
                            }
                        }
                    }
                }
            }
        }
    }

}
