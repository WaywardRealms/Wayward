package net.wayward_realms.waywardlib.util.player;

import net.wayward_realms.waywardlib.Wayward;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerNamePlateUtils {

    private static PlayerDisplayModifier factory;

    public static void init(Wayward plugin) {
        factory = new PlayerDisplayModifier(plugin);
    }

    public static void refreshPlayer(Player player) {
        PlayerNamePlateChangeEvent event = new PlayerNamePlateChangeEvent(player, player.getName(), player.getDisplayName());
        Bukkit.getPluginManager().callEvent(event);
        factory.changeDisplay(event.getPlayer(), event.getSkin(), event.getName());
    }

}
