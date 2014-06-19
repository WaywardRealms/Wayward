package net.wayward_realms.waywardcharacters;

import net.wayward_realms.waywardlib.util.player.PlayerNamePlateChangeEvent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerNamePlateChangeListener implements Listener {

    @EventHandler
    public void onPlayerNamePlateChange(PlayerNamePlateChangeEvent event) {
        if (StringUtils.countMatches(event.getPlayer().getDisplayName(), "\"") >= 2) {
            event.setName(event.getPlayer().getDisplayName().split("\"")[1]);
        } else if (event.getName().contains(" ")) {
            event.setName(event.getPlayer().getDisplayName().split(" ")[0]);
        } else {
            event.setName(event.getPlayer().getDisplayName());
        }
    }

}
