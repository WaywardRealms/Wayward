package net.wayward_realms.waywardprofessions;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.professions.ToolType;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Random;

public class PrepareItemCraftListener implements Listener {

    private WaywardProfessions plugin;

    public PrepareItemCraftListener(WaywardProfessions plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        if (event.getViewers().size() > 0 && event.getViewers().get(0).getGameMode() != GameMode.CREATIVE) {
            if (plugin.canGainCraftEfficiency(event.getRecipe().getResult().getType())) return;
            if (ToolType.getToolType(event.getInventory().getResult().getType()) != null) {
                ToolType type = ToolType.getToolType(event.getInventory().getResult().getType());
                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                if (characterPluginProvider != null) {
                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                    net.wayward_realms.waywardlib.character.Character character = characterPlugin.getActiveCharacter((Player) event.getViewers().get(0));
                    ItemStack itemWithAdjustedDurability = event.getInventory().getResult();
                    itemWithAdjustedDurability.setDurability((short) (event.getInventory().getResult().getType().getMaxDurability() - (0.75D * (double) plugin.getMaxToolDurability(character, type))));
                    event.getInventory().setResult(itemWithAdjustedDurability);
                }
            }
            RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
            if (characterPluginProvider != null) {
                CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                net.wayward_realms.waywardlib.character.Character character = characterPlugin.getActiveCharacter((Player) event.getViewers().get(0));
                Random random = new Random();
                int craftEfficiency = plugin.getCraftEfficiency(character, event.getInventory().getResult().getType());
                int amount = (int) ((double) (random.nextInt(100) <= 30 ? (craftEfficiency > 10 ? random.nextInt(craftEfficiency) : random.nextInt(10)) : 25) * (4D / 100D) * (double) event.getInventory().getResult().getAmount());
                event.getInventory().getResult().setAmount(amount);
            }
        }
    }
}
