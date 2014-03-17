package net.wayward_realms.waywardeconomy;

import net.wayward_realms.waywardlib.economy.Currency;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryCloseListener implements Listener {

    private WaywardEconomy plugin;

    public InventoryCloseListener(WaywardEconomy plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getTitle().toLowerCase().contains("wallet")) {
            Currency currency = plugin.getCurrency(event.getInventory().getTitle().split("\\[")[1].replace("]", ""));
            int amount = 0;
            for (ItemStack item : event.getInventory().getContents()) {
                if (item != null) {
                    if (item.getType() == Material.GOLD_NUGGET && item.getItemMeta().getDisplayName().equalsIgnoreCase(currency.getNameSingular())) {
                        amount += item.getAmount();
                    }
                }
            }
            plugin.setMoney((Player) event.getPlayer(), currency, amount);
        }
    }

}
