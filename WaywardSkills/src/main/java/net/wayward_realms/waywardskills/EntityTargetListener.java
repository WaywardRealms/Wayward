package net.wayward_realms.waywardskills;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Party;
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
                            Character character = characterPlugin.getCharacter(summonerId);
                            Party party = characterPlugin.getParty(character);
                            if (party != null) {
                                for (net.wayward_realms.waywardlib.character.Character member : party.getMembers()) {
                                    if (characterPlugin.getActiveCharacter((Player) event.getTarget()).getId() == member.getId()) {
                                        event.setCancelled(true);
                                        return;
                                    }
                                }
                            }
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
