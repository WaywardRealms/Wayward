package net.wayward_realms.waywardlocks;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlocks.keyring.KeyringManager;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.TrapDoor;

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
                        if (hasKey(event.getPlayer(), event.getClickedBlock())) {
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
                                    if (event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("Lockpick")
                                            && event.getPlayer().getItemInHand().getItemMeta().hasLore()
                                            && event.getPlayer().getItemInHand().getItemMeta().getLore().contains("Used for breaking through locks")) {
                                        if (event.getPlayer().getItemInHand().getAmount() > 1) {
                                            event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount() - 1);
                                        } else {
                                            event.getPlayer().setItemInHand(null);
                                        }
                                        event.getPlayer().updateInventory();
                                        Random random = new Random();
                                        if (random.nextInt(100) > plugin.getLockpickEfficiency(event.getPlayer())) {
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
                                            if (random.nextInt(100) < 50) {
                                                plugin.setLockpickEfficiency(event.getPlayer(), plugin.getLockpickEfficiency(event.getPlayer()) + 1);
                                            }
                                            final Player player = event.getPlayer();
                                            if (block.getType() == Material.CHEST) {
                                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        player.closeInventory();
                                                        player.sendMessage(ChatColor.RED + "The chest snaps shut again, the lock clicking shut.");
                                                    }
                                                }, 60L);
                                            } else if (block.getType() == Material.WOOD_DOOR || block.getType() == Material.WOODEN_DOOR) {
                                                final Block finalBlock = block;
                                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        closeDoor(finalBlock);
                                                        player.sendMessage(ChatColor.RED + "The door slams behind you, the lock clicking shut.");
                                                    }
                                                }, 60L);
                                            }
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


    public boolean hasKey(Player character, Block block) {
    	int blockX, blockY, blockZ;
    	String world = block.getLocation().getWorld().toString();
    	blockX = (int) block.getLocation().getX();
    	blockY = (int) block.getLocation().getY();
    	blockZ = (int) block.getLocation().getZ();
    	
    	for (ItemStack key : plugin.getKeyringManager().getKeyring(character)) {
    		if (key.getItemMeta().getLore().contains(world + "," + blockX + "," + blockY + "," + blockZ))
    			return true;
    	}
    	
    	return false;
    }
    
    public boolean isDoorClosed(Block block) {
        if (block.getType() == Material.TRAP_DOOR) {
            TrapDoor trapdoor = (TrapDoor)block.getState().getData();
            return !trapdoor.isOpen();
        } else {
            byte data = block.getData();
            if ((data & 0x8) == 0x8) {
                block = block.getRelative(BlockFace.DOWN);
                data = block.getData();
            }
            return ((data & 0x4) == 0);
        }
    }

    public void openDoor(Block block) {
        if (block.getType() == Material.TRAP_DOOR) {
            BlockState state = block.getState();
            TrapDoor trapdoor = (TrapDoor)state.getData();
            trapdoor.setOpen(true);
            state.update();
        } else {
            byte data = block.getData();
            if ((data & 0x8) == 0x8) {
                block = block.getRelative(BlockFace.DOWN);
                data = block.getData();
            }
            if (isDoorClosed(block)) {
                data = (byte) (data | 0x4);
                block.setData(data, true);
                block.getWorld().playEffect(block.getLocation(), Effect.DOOR_TOGGLE, 0);
            }
        }
    }

    public void closeDoor(Block block) {
        if (block.getType() == Material.TRAP_DOOR) {
            BlockState state = block.getState();
            TrapDoor trapdoor = (TrapDoor)state.getData();
            trapdoor.setOpen(false);
            state.update();
        } else {
            byte data = block.getData();
            if ((data & 0x8) == 0x8) {
                block = block.getRelative(BlockFace.DOWN);
                data = block.getData();
            }
            if (!isDoorClosed(block)) {
                data = (byte) (data & 0xb);
                block.setData(data, true);
                block.getWorld().playEffect(block.getLocation(), Effect.DOOR_TOGGLE, 0);
            }
        }
    }

}
