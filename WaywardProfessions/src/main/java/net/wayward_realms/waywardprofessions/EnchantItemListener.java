package net.wayward_realms.waywardprofessions;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

public class EnchantItemListener implements Listener {

    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        event.setCancelled(true);
    }
}
