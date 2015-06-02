package net.wayward_realms.waywardcharacters;

import net.wayward_realms.waywardlib.character.Character;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private WaywardCharacters plugin;

    public PlayerJoinListener(WaywardCharacters plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.getActiveCharacter(event.getPlayer()) == null) {
            plugin.setActiveCharacter(event.getPlayer(), new CharacterImpl(plugin, event.getPlayer()));
        }
        Character character = plugin.getActiveCharacter(event.getPlayer());
        event.getPlayer().setDisplayName(character.isNameHidden() ? ChatColor.MAGIC + character.getName() + ChatColor.RESET : character.getName());
    }

}
