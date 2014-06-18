package net.wayward_realms.waywardeconomy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerJoinListener implements Listener {

    private WaywardEconomy plugin;

    public PlayerJoinListener(WaywardEconomy plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        int chests = 0;
        if (plugin.getMoney(event.getPlayer()) > plugin.getMaximumMoney()) {
            chests++;
            event.getPlayer().getWorld().getBlockAt(event.getPlayer().getLocation()).setType(Material.CHEST);
            Chest firstChest = (Chest) event.getPlayer().getWorld().getBlockAt(event.getPlayer().getLocation()).getState();
            event.getPlayer().teleport(event.getPlayer().getWorld().getBlockAt(event.getPlayer().getLocation()).getRelative(BlockFace.UP).getLocation());
            ItemStack coin = new ItemStack(Material.GOLD_NUGGET, 64);
            ItemMeta meta = coin.getItemMeta();
            meta.setDisplayName(plugin.getPrimaryCurrency().getNameSingular());
            coin.setItemMeta(meta);
            int slot = 0;
            while (slot <= firstChest.getInventory().getSize() - 1) {
                firstChest.getInventory().setItem(slot, coin);
                slot++;
            }
            while (plugin.getMoney(event.getPlayer()) > plugin.getMaximumMoney()) {
                event.getPlayer().getWorld().getBlockAt(event.getPlayer().getLocation()).setType(Material.CHEST);
                Chest chest = (Chest) event.getPlayer().getWorld().getBlockAt(event.getPlayer().getLocation()).getState();
                chest.getInventory().setContents(firstChest.getBlockInventory().getContents());
                event.getPlayer().teleport(event.getPlayer().getWorld().getBlockAt(event.getPlayer().getLocation()).getRelative(BlockFace.UP).getLocation());
                plugin.addMoney(event.getPlayer(), -1728);
                chests++;
            }
        }
        if (chests > 0) event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "Your money could not fit in your wallet, so it was transferred to " + chests + " chests.");
    }

}
