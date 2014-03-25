package net.wayward_realms.waywardlocks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerInteractListener implements Listener {

    private WaywardLocks plugin;

    public PlayerInteractListener(WaywardLocks plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.hasBlock()) {
            if (event.getClickedBlock().getType() == Material.CHEST || event.getClickedBlock().getType() == Material.WOODEN_DOOR) {
                if (plugin.isClaiming(event.getPlayer())) {
                    Block block = null;
                    xLoop: for (int x = event.getClickedBlock().getX() - 1; x <= event.getClickedBlock().getX() + 1; x++) {
                        for (int y = event.getClickedBlock().getY() - 1; y <= event.getClickedBlock().getY() + 1; y++) {
                            for (int z = event.getClickedBlock().getZ() - 1; z <= event.getClickedBlock().getZ() + 1; z++) {
                                if (plugin.isLocked(event.getClickedBlock().getWorld().getBlockAt(x, y, z))) {
                                    block = event.getClickedBlock().getWorld().getBlockAt(x, y, z);
                                    break xLoop;
                                }
                            }
                        }
                    }
                    if (block == null) {
                        if (event.getPlayer().getItemInHand().getAmount() == 1) {
                            event.getPlayer().setItemInHand(null);
                        } else {
                            event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount() - 1);
                        }
                        for (ItemStack item : event.getPlayer().getInventory().addItem(plugin.lock(event.getClickedBlock())).values()) {
                            event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), item);
                        }
                        event.getPlayer().updateInventory();
                        event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Block successfully locked. You've been given the key, take good care of it.");
                    } else {
                        event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "That block is already locked!");
                    }
                    event.setCancelled(true);
                } else if (plugin.isUnclaiming(event.getPlayer())) {
                    Block block = null;
                    xLoop: for (int x = event.getClickedBlock().getX() - 1; x <= event.getClickedBlock().getX() + 1; x++) {
                        for (int y = event.getClickedBlock().getY() - 1; y <= event.getClickedBlock().getY() + 1; y++) {
                            for (int z = event.getClickedBlock().getZ() - 1; z <= event.getClickedBlock().getZ() + 1; z++) {
                                if (plugin.isLocked(event.getClickedBlock().getWorld().getBlockAt(x, y, z))) {
                                    block = event.getClickedBlock().getWorld().getBlockAt(x, y, z);
                                    break xLoop;
                                }
                            }
                        }
                    }
                    if (block != null) {
                        if (event.getPlayer().getItemInHand() != null
                                && event.getPlayer().getItemInHand().getType() == Material.IRON_INGOT
                                && event.getPlayer().getItemInHand().hasItemMeta()
                                && event.getPlayer().getItemInHand().getItemMeta().hasDisplayName()
                                && event.getPlayer().getItemInHand().getItemMeta().hasLore()
                                && event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("Key")
                                && event.getPlayer().getItemInHand().getItemMeta().getLore().contains(block.getWorld().getName() + "," + block.getLocation().getBlockX() + "," + block.getLocation().getBlockY() + "," + block.getLocation().getBlockZ())) {
                            plugin.unlock(block);
                            event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "The lock was successfully removed from the " + block.getType().toString().toLowerCase().replace('_', ' '));
                            event.getPlayer().setItemInHand(null);
                            ItemStack lockItem = new ItemStack(Material.IRON_INGOT);
                            ItemMeta lockMeta = lockItem.getItemMeta();
                            lockMeta.setDisplayName("Lock");
                            List<String> lore = new ArrayList<>();
                            lore.add("Used for locking chests");
                            lockMeta.setLore(lore);
                            lockItem.setItemMeta(lockMeta);
                            event.getPlayer().getInventory().addItem(lockItem);
                            event.getPlayer().updateInventory();
                        } else {
                            event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be holding the key to a lock to remove the lock.");
                        }
                    } else {
                        event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "There are no locked blocks there.");
                    }
                    plugin.setUnclaiming(event.getPlayer(), false);
                    event.setCancelled(true);
                } else if (plugin.isGettingKey(event.getPlayer())) {
                    Block block = null;
                    xLoop: for (int x = event.getClickedBlock().getX() - 1; x <= event.getClickedBlock().getX() + 1; x++) {
                        for (int y = event.getClickedBlock().getY() - 1; y <= event.getClickedBlock().getY() + 1; y++) {
                            for (int z = event.getClickedBlock().getZ() - 1; z <= event.getClickedBlock().getZ() + 1; z++) {
                                if (plugin.isLocked(event.getClickedBlock().getWorld().getBlockAt(x, y, z))) {
                                    block = event.getClickedBlock().getWorld().getBlockAt(x, y, z);
                                    break xLoop;
                                }
                            }
                        }
                    }
                    if (block == null) {
                        event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "That block is not locked.");
                    } else {
                        ItemStack key = new ItemStack(Material.IRON_INGOT, 1);
                        //int lockId = plugin.getLockId(block);
                        ItemMeta keyMeta = key.getItemMeta();
                        keyMeta.setDisplayName("Key");
                        List<String> keyLore = new ArrayList<>();
                        keyLore.add(block.getWorld().getName() + "," + block.getLocation().getBlockX() + "," + block.getLocation().getBlockY() + "," + block.getLocation().getBlockZ());
                        keyMeta.setLore(keyLore);
                        key.setItemMeta(keyMeta);
                        for (ItemStack item : event.getPlayer().getInventory().addItem(key).values()) {
                            event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), item);
                        }
                        event.getPlayer().updateInventory();
                        event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Here is the key.");
                    }
                    plugin.setGettingKey(event.getPlayer(), false);
                    event.setCancelled(true);
                } else {
                    Block block = null;
                    xLoop: for (int x = event.getClickedBlock().getX() - 1; x <= event.getClickedBlock().getX() + 1; x++) {
                        for (int y = event.getClickedBlock().getY() - 1; y <= event.getClickedBlock().getY() + 1; y++) {
                            for (int z = event.getClickedBlock().getZ() - 1; z <= event.getClickedBlock().getZ() + 1; z++) {
                                if (plugin.isLocked(event.getClickedBlock().getWorld().getBlockAt(x, y, z))) {
                                    block = event.getClickedBlock().getWorld().getBlockAt(x, y, z);
                                    break xLoop;
                                }
                            }
                        }
                    }
                    if (block != null) {
                        if (event.getPlayer().getItemInHand() != null) {
                            if (event.getPlayer().getItemInHand().hasItemMeta()) {
                                if (event.getPlayer().getItemInHand().getItemMeta().hasDisplayName()) {
                                    if (event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("Lockpick")) {
                                        if (event.getPlayer().getItemInHand().getAmount() > 1) {
                                            event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount() - 1);
                                        } else {
                                            event.getPlayer().setItemInHand(null);
                                        }
                                        event.getPlayer().updateInventory();
                                        Random random = new Random();
                                        if (random.nextInt(100) > 15) {
                                            event.setCancelled(true);
                                            switch (random.nextInt(5)) {
                                                case 0: event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "Your lockpick bent inside the lock."); break;
                                                case 1: event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "Your lockpick snapped as you tried to use it."); break;
                                                case 2: event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "Your lockpick seems to have gotten stuck."); break;
                                                case 3: event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "Your lockpick twists out of shape. It looks unusable."); break;
                                                case 4: event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "Your lockpick makes a grating sound inside the lock, but nothing happens."); break;
                                            }
                                        } else {
                                            event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "You hear a click and the lock opens, allowing you access to the " + block.getType().toString().toLowerCase().replace('_', ' '));
                                            final Player player = event.getPlayer();
                                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                                @Override
                                                public void run() {
                                                    player.closeInventory();
                                                    player.sendMessage(ChatColor.RED + "The chest snaps shut again, the lock clicking shut.");
                                                }
                                            }, 60L);
                                        }
                                        return;
                                    }
                                    if (!event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("Key")
                                            || !event.getPlayer().getItemInHand().getItemMeta().hasLore()
                                            || !event.getPlayer().getItemInHand().getItemMeta().getLore().contains(block.getWorld().getName() + "," + block.getLocation().getBlockX() + "," + block.getLocation().getBlockY() + "," + block.getLocation().getBlockZ())) {
                                        event.setCancelled(true);
                                        event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "The " + block.getType().toString().toLowerCase().replace('_', ' ') + " seems to be locked. You would need the key or a lockpick to get in.");
                                    }
                                } else {
                                    event.setCancelled(true);
                                    event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "The " + block.getType().toString().toLowerCase().replace('_', ' ') + " seems to be locked. You would need the key or a lockpick to get in.");
                                }
                            } else {
                                event.setCancelled(true);
                                event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "The " + block.getType().toString().toLowerCase().replace('_', ' ') + " seems to be locked. You would need the key or a lockpick to get in.");
                            }
                        } else {
                            event.setCancelled(true);
                            event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "The " + block.getType().toString().toLowerCase().replace('_', ' ') + " seems to be locked. You would need the key or a lockpick to get in.");
                        }
                    }
                }
            }
        }
    }

}
