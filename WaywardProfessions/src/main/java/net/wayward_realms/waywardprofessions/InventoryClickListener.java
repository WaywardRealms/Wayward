package net.wayward_realms.waywardprofessions;

import net.wayward_realms.waywardlib.character.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Random;

public class InventoryClickListener implements Listener {

    private WaywardProfessions plugin;

    public InventoryClickListener(WaywardProfessions plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getType() == InventoryType.BREWING) {
            if (event.getSlotType() == InventoryType.SlotType.RESULT) {
                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                if (characterPluginProvider != null) {
                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                    net.wayward_realms.waywardlib.character.Character character = characterPlugin.getActiveCharacter((Player) event.getWhoClicked());
                    Random random = new Random();
                    int brewingEfficiency = plugin.getBrewingEfficiency(character);
                    int amount = (int) ((double) (random.nextInt(100) <= 30 ? (brewingEfficiency > 10 ? random.nextInt(brewingEfficiency) : random.nextInt(10)) : 25) * (4D / 100D) * (double) event.getCurrentItem().getAmount());
                    event.getCurrentItem().setAmount(amount);
                    plugin.setBrewingEfficiency(character, plugin.getBrewingEfficiency(character) + (random.nextInt(100) <= 30 ? random.nextInt(3) : 0));
                }
            }
        }
    }

}
