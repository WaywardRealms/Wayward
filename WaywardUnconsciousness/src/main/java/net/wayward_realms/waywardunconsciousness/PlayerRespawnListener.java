package net.wayward_realms.waywardunconsciousness;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerRespawnListener implements Listener {

    private WaywardUnconsciousness plugin;

    public PlayerRespawnListener(WaywardUnconsciousness plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(final PlayerRespawnEvent event) {
        if (plugin.isDeath(plugin.getDeathCause(event.getPlayer()))) {
            event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "Your death was determined to be a fatality, and a new character has been created for you.");
            RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
            if (characterPluginProvider != null) {
                CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                Character character = characterPlugin.getActiveCharacter(event.getPlayer());
                character.setDead(true);
                Character newCharacter = characterPlugin.createNewCharacter(event.getPlayer());
                characterPlugin.setActiveCharacter(event.getPlayer(), newCharacter);
                character.setInventoryContents(plugin.getDeathInventory(character));
                plugin.removeDeathInventory(character);
                character.setLocation(plugin.getServer().getWorlds().get(0).getSpawnLocation());
                event.setRespawnLocation(newCharacter.getLocation());
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0), true);
                        plugin.removeDeathInventory(event.getPlayer());
                    }
                }, 20L);
            }
        } else {
            event.setRespawnLocation(plugin.getDeathLocation(event.getPlayer()));
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0), true);
                    event.getPlayer().getInventory().addItem(plugin.getDeathInventory(event.getPlayer()));
                    plugin.removeDeathInventory(event.getPlayer());
                }
            }, 20L);
        }
    }

}
