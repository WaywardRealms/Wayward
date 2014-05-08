package net.wayward_realms.waywardskills;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EntityTargetListener implements Listener {

    private WaywardSkills plugin;

    public EntityTargetListener(WaywardSkills plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.getTarget() instanceof Player) {
            if (event.getEntity().getMetadata("summoner") != null) {
                for (MetadataValue metadata : event.getEntity().getMetadata("summoner")) {
                    if (metadata.getOwningPlugin() == plugin) {
                        int summonerId = metadata.asInt();
                        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                        if (characterPluginProvider != null) {
                            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                            if (characterPlugin.getActiveCharacter((Player) event.getTarget()).getId() == summonerId) {
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }

}
