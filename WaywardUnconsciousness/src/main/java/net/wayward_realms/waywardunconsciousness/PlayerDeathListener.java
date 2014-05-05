package net.wayward_realms.waywardunconsciousness;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;
import java.util.List;

public class PlayerDeathListener implements Listener {

    private WaywardUnconsciousness plugin;

    public PlayerDeathListener(WaywardUnconsciousness plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage("");
        switch (event.getEntity().getLastDamageCause().getCause()) {
            case FIRE:
            case FIRE_TICK:
            case LAVA:
            case DROWNING:
                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                if (characterPluginProvider != null) {
                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                    characterPlugin.getActiveCharacter(event.getEntity()).setDead(true);
                    characterPlugin.setActiveCharacter(event.getEntity(), characterPlugin.createNewCharacter(event.getEntity()));
                    event.getDrops().clear();
                    event.getEntity().sendMessage(plugin.getPrefix() + ChatColor.RED + "Your death was determined to be a fatality, and a new character has been created for you.");
                }
                break;
            case FALL:
                if (event.getEntity().getFallDistance() >= 64) {
                    characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                    if (characterPluginProvider != null) {
                        CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                        characterPlugin.getActiveCharacter(event.getEntity()).setDead(true);
                        characterPlugin.setActiveCharacter(event.getEntity(), characterPlugin.createNewCharacter(event.getEntity()));
                        event.getDrops().clear();
                        event.getEntity().sendMessage(plugin.getPrefix() + ChatColor.RED + "Your death was determined to be a fatality, and a new character has been created for you.");
                    }
                }
                break;
            default:
                plugin.setUnconscious(event.getEntity(), true);
                plugin.setDeathLocation(event.getEntity(), event.getEntity().getLocation());
                plugin.setDeathTime(event.getEntity());
                List<ItemStack> drops = new ArrayList<>(event.getDrops());
                plugin.setDeathInventory(event.getEntity(), drops);
                event.getDrops().clear();
                break;
        }

    }

}
