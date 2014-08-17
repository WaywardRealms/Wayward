package net.wayward_realms.waywardskills;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.skills.Specialisation;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.material.Wool;
import org.bukkit.plugin.RegisteredServiceProvider;

public class InventoryClickListener implements Listener {

    private WaywardSkills plugin;

    public InventoryClickListener(WaywardSkills plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equalsIgnoreCase("Specialise")) {
            event.setCancelled(true);
            if (event.getCurrentItem().getType() == Material.WOOL) {
                if (event.getCurrentItem().getData() instanceof Wool) {
                    Wool woolData = (Wool) event.getCurrentItem().getData();
                    if (woolData.getColor() == DyeColor.GREEN) {
                        Specialisation specialisation = plugin.getSpecialisation(event.getCurrentItem().getItemMeta().getDisplayName());
                        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                        if (characterPluginProvider != null) {
                            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                            Character character = characterPlugin.getActiveCharacter((Player) event.getWhoClicked());
                            if (plugin.getUnassignedSpecialisationPoints(character) > 0) {
                                plugin.setSpecialisationValue(character, specialisation, plugin.getSpecialisationValue(character, specialisation) + 1);
                                ((Player) event.getWhoClicked()).sendMessage(ChatColor.GREEN + "Assigned a specialisation point to " + specialisation.getName());
                            } else {
                                ((Player) event.getWhoClicked()).sendMessage(ChatColor.RED + "You do not have any remaining specialisation points to assign.");
                            }
                        }
                    } else {
                        event.getWhoClicked().closeInventory();
                        Specialisation specialisation = plugin.getSpecialisation(event.getCurrentItem().getItemMeta().getDisplayName());
                        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                        if (characterPluginProvider != null) {
                            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                            Character character = characterPlugin.getActiveCharacter((Player) event.getWhoClicked());
                            event.getWhoClicked().openInventory(plugin.getSpecialisationInventory(specialisation, character));
                        }
                    }
                }
            }
            return;
        }
        if (event.getCurrentItem() != null) {
            if (event.getCurrentItem().hasItemMeta()) {
                if (event.getCurrentItem().getItemMeta().hasDisplayName()) {
                    if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Mage armour")) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

}
