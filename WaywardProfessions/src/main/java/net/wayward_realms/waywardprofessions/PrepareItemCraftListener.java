package net.wayward_realms.waywardprofessions;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.professions.ToolType;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

public class PrepareItemCraftListener implements Listener {

    private WaywardProfessions plugin;

    public PrepareItemCraftListener(WaywardProfessions plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        for (HumanEntity viewer : event.getViewers()) {
            if (viewer.getGameMode() != GameMode.CREATIVE) {
                if(viewer instanceof Player) {
                    RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                    if (characterPluginProvider != null) {
                        ItemStack craftedItem = event.getInventory().getResult();
                        if (plugin.canGainCraftEfficiency(craftedItem.getType())) {
                            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                            net.wayward_realms.waywardlib.character.Character character = characterPlugin.getActiveCharacter((Player) viewer);
                            int craftEfficiency = plugin.getCraftEfficiency(character, craftedItem.getType());
                            int amount = (int) Math.min(1, Math.round(((double) craftEfficiency / 50D) * (double) craftedItem.getAmount()));
                            craftedItem.setAmount(amount);
                            if (ToolType.getToolType(event.getInventory().getResult().getType()) != null) {
                                ToolType type = ToolType.getToolType(event.getInventory().getResult().getType());
                                craftedItem.setDurability((short) (craftedItem.getType().getMaxDurability() - (0.75D * (double) plugin.getMaxToolDurability(character, type))));
                            }
                            event.getInventory().setResult(craftedItem);
                        }
                    }
                }
            }
        }
    }
}
