package net.wayward_realms.waywardcharacters;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Character;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

public class FoodLevelChangeListener implements Listener {

    private WaywardCharacters plugin;

    public FoodLevelChangeListener(WaywardCharacters plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (plugin.isHungerDisabled(player)) {
                event.setCancelled(true);
                player.setFoodLevel(20);
                player.setExhaustion(0.0F);
                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                if (characterPluginProvider != null) {
                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                    Character character = characterPlugin.getActiveCharacter(player);
                    character.setFoodLevel(20);
                }
                return;
            }
            RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
            if (characterPluginProvider != null) {
                CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                Character character = characterPlugin.getActiveCharacter(player);
                character.setFoodLevel(event.getFoodLevel());
            }
        }
    }

}
