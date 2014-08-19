package net.wayward_realms.waywardcharacters;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.util.player.PlayerNamePlateChangeEvent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerNamePlateChangeListener implements Listener {

    private WaywardCharacters plugin;

    public PlayerNamePlateChangeListener(WaywardCharacters plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerNamePlateChange(PlayerNamePlateChangeEvent event) {
        Character character = plugin.getActiveCharacter(event.getPlayer());
        if (character.getNamePlate() != null && !character.getNamePlate().equals("")) {
            event.setName(character.getNamePlate());
        } else if (StringUtils.countMatches(character.getName(), "\"") >= 2) {
            event.setName(character.getName().split("\"")[1]);
        } else if (character.getName().contains(" ")) {
            event.setName(character.getName().split(" ")[0]);
        } else {
            event.setName(character.getName());
        }
    }

}
