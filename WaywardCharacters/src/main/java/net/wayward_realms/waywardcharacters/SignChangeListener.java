package net.wayward_realms.waywardcharacters;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeListener implements Listener {

    private WaywardCharacters plugin;

    public SignChangeListener(WaywardCharacters plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[masheekwell]")) {
            if (event.getPlayer().hasPermission("wayward.characters.createmasheekwell")) {
                event.setLine(0, ChatColor.YELLOW + "[masheekwell]");
                try {
                    Integer.parseInt(event.getLine(1));
                } catch (NumberFormatException exception) {
                    event.setLine(1, "20");
                }
            } else {
                event.getBlock().breakNaturally();
                event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission to create masheek wells.");
            }
        }
    }

}
