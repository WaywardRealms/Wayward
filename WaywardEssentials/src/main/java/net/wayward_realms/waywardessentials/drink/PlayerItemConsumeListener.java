package net.wayward_realms.waywardessentials.drink;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Character;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

public class PlayerItemConsumeListener implements Listener {

    private WaywardEssentials plugin;

    public PlayerItemConsumeListener(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if (event.getItem().hasItemMeta()) {
            ItemMeta meta = event.getItem().getItemMeta();
            if (meta.hasDisplayName()) {
                if (event.getItem().getType() == Material.POTION) {
                    RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                    if (characterPluginProvider != null) {
                        CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                        Character character = characterPlugin.getActiveCharacter(event.getPlayer());
                        if (meta.getDisplayName().equalsIgnoreCase("Beer")) {
                            plugin.getDrinkManager().increaseDrunkenness(character, 3);
                        } else if (meta.getDisplayName().equalsIgnoreCase("Wine")) {
                            plugin.getDrinkManager().increaseDrunkenness(character, 6);
                        } else if (meta.getDisplayName().equalsIgnoreCase("Sherry")) {
                            plugin.getDrinkManager().increaseDrunkenness(character, 10);
                        } else if (meta.getDisplayName().equalsIgnoreCase("Vodka")) {
                            plugin.getDrinkManager().increaseDrunkenness(character, 20);
                        } else if (meta.getDisplayName().equalsIgnoreCase("Whisky")) {
                            plugin.getDrinkManager().increaseDrunkenness(character, 20);
                        } else if (meta.getDisplayName().equalsIgnoreCase("Rum")) {
                            plugin.getDrinkManager().increaseDrunkenness(character, 20);
                        } else if (meta.getDisplayName().equalsIgnoreCase("Cider")) {
                            plugin.getDrinkManager().increaseDrunkenness(character, 3);
                        }
                    }
                }
            }
        }
    }

}
