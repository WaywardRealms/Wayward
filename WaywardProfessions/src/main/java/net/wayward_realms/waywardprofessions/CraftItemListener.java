package net.wayward_realms.waywardprofessions;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.professions.ToolType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Random;

public class CraftItemListener implements Listener {

    private final WaywardProfessions plugin;

    public CraftItemListener(WaywardProfessions plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (ToolType.getToolType(event.getCurrentItem().getType()) != null) {
            ToolType type = ToolType.getToolType(event.getCurrentItem().getType());
            RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
            if (characterPluginProvider != null) {
                CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                Character character = characterPlugin.getActiveCharacter((Player) event.getWhoClicked());
                event.getCurrentItem().setDurability((short) (event.getCurrentItem().getType().getMaxDurability() - plugin.getMaxToolDurability(character, type)));
                Random random = new Random();
                plugin.setMaxToolDurability(character, type, Math.min(plugin.getMaxToolDurability(character, type) + (random.nextInt(100) <= 30 ? random.nextInt(3) : 0), event.getCurrentItem().getType().getMaxDurability()));
            }
        }
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            Character character = characterPlugin.getActiveCharacter((Player) event.getWhoClicked());
            Material material = event.getCurrentItem().getType();
            Random random = new Random();
            int amount = (int) ((double) random.nextInt(plugin.getCraftEfficiency(character, event.getCurrentItem().getType())) * (8D / 100D) * (double) event.getCurrentItem().getAmount());
            event.getCurrentItem().setAmount(amount);
            plugin.setCraftEfficiency(character, material, plugin.getCraftEfficiency(character, material) + (random.nextInt(100) <= 30 ? random.nextInt(3) : 0));
        }
    }

}
