package net.wayward_realms.waywardlocks;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.TrapDoor;

import java.util.ArrayList;
import java.util.Iterator;
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
                        if (event.getPlayer().getInventory().getItemInMainHand().getAmount() == 1) {
                            event.getPlayer().getInventory().setItemInMainHand(null);
                        } else {
                            event.getPlayer().getInventory().getItemInMainHand().setAmount(event.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
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
                            removeKey(event.getPlayer(), event.getClickedBlock());
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
                        if (hasKey(event.getPlayer(), block)) return;
                        if (hasLockpick(event.getPlayer())) {
                            removeLockpick(event.getPlayer());
                            event.getPlayer().updateInventory();
                            Random random = new Random();
                            if (random.nextInt(100) > plugin.getLockpickEfficiency(event.getPlayer()) / 4) {
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
                                    if (plugin.isLockpickCooledDown(block)) {
                                        Chest chest = (Chest) block.getState();
                                        int slot = random.nextInt(chest.getInventory().getSize());
                                        ItemStack item = chest.getInventory().getItem(slot);
                                        if (item != null) {
                                            for (ItemStack drop : player.getInventory().addItem(item).values()) {
                                                player.getWorld().dropItem(player.getLocation(), drop);
                                            }
                                            ItemStack fingerprints = new ItemStack(Material.PAPER);
                                            ItemMeta meta = fingerprints.getItemMeta();
                                            meta.setDisplayName(player.getDisplayName() + "'s fingerprints");
                                            List<String> lore = new ArrayList<>();
                                            lore.add("In the place of the " + item.getAmount() + " " + item.getType().toString().toLowerCase().replace('_', ' ') + (item.getAmount() == 1 ? "" : "s") + " are " + player.getDisplayName() + "'s fingerprints");
                                            meta.setLore(lore);
                                            fingerprints.setItemMeta(meta);
                                            chest.getInventory().setItem(slot, fingerprints);
                                            player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "You managed to recover " + item.getAmount() + " " + item.getType().toString().toLowerCase().replace('_', ' ') + (item.getAmount() != 1 ? "s" : "") + " before the chest slammed shut again.");
                                        } else {
                                            player.sendMessage(plugin.getPrefix() + ChatColor.RED + "You managed to open the container, but failed to find any items before it slammed shut again.");
                                        }
                                        plugin.setLockpickCooldown(block, 43200000L); // 43200000 milliseconds = 12 hours
                                    } else {
                                        player.sendMessage(plugin.getPrefix() + ChatColor.RED + "That chest has already been lockpicked today, try again tomorrow.");
                                    }
                                    event.setCancelled(true);
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
                        if (!hasKey(event.getPlayer(), block)) {
                            event.setCancelled(true);
                            event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "The " + block.getType().toString().toLowerCase().replace('_', ' ') + " seems to be locked. You would need the key or a lockpick to get in.");
                        }
                    }
                }
            }
        }
    }


    public boolean hasKey(Player player, Block block) {
    	for (ItemStack key : plugin.getKeyringManager().getKeyring(player)) {
            if (key != null
                    && key.getType() == Material.IRON_INGOT
                    && key.hasItemMeta()
                    && key.getItemMeta().hasDisplayName()
                    && key.getItemMeta().hasLore()
                    && key.getItemMeta().getDisplayName().equals("Key")
                    && key.getItemMeta().getLore().contains(block.getWorld().getName() + "," + block.getLocation().getBlockX() + "," + block.getLocation().getBlockY() + "," + block.getLocation().getBlockZ())) {
                return true;
            }
    	}
        for (ItemStack key : player.getInventory().getContents()) {
            if (key != null
                    && key.getType() == Material.IRON_INGOT
                    && key.hasItemMeta()
                    && key.getItemMeta().hasDisplayName()
                    && key.getItemMeta().hasLore()
                    && key.getItemMeta().getDisplayName().equals("Key")
                    && key.getItemMeta().getLore().contains(block.getWorld().getName() + "," + block.getLocation().getBlockX() + "," + block.getLocation().getBlockY() + "," + block.getLocation().getBlockZ())) {
                return true;
            }
        }
    	return false;
    }

    public void removeKey(Player player, Block block) {
        List<ItemStack> keyring = plugin.getKeyringManager().getKeyring(player);
        for (Iterator<ItemStack> iterator = keyring.iterator(); iterator.hasNext(); ) {
            ItemStack key = iterator.next();
            if (key != null
                    && key.getType() == Material.IRON_INGOT
                    && key.hasItemMeta()
                    && key.getItemMeta().hasDisplayName()
                    && key.getItemMeta().hasLore()
                    && key.getItemMeta().getDisplayName().equals("Key")
                    && key.getItemMeta().getLore().contains(block.getWorld().getName() + "," + block.getLocation().getBlockX() + "," + block.getLocation().getBlockY() + "," + block.getLocation().getBlockZ())) {
                if (key.getAmount() > 1) {
                    key.setAmount(key.getAmount() - 1);
                } else {
                    iterator.remove();
                }
                return;
            }
        }
        plugin.getKeyringManager().setKeyring(player, keyring);
        for (ItemStack key : player.getInventory().getContents()) {
            if (key != null
                    && key.getType() == Material.IRON_INGOT
                    && key.hasItemMeta()
                    && key.getItemMeta().hasDisplayName()
                    && key.getItemMeta().hasLore()
                    && key.getItemMeta().getDisplayName().equals("Key")
                    && key.getItemMeta().getLore().contains(block.getWorld().getName() + "," + block.getLocation().getBlockX() + "," + block.getLocation().getBlockY() + "," + block.getLocation().getBlockZ())) {
                ItemStack oneKey = new ItemStack(key);
                oneKey.setAmount(1);
                player.getInventory().removeItem(oneKey);
                return;
            }
        }
    }

    public boolean hasLockpick(Player player) {
        ItemStack lockpick = player.getInventory().getItemInMainHand();
        if (lockpick != null) {
            if (lockpick.hasItemMeta()) {
                if (lockpick.getItemMeta().hasDisplayName()) {
                    if (lockpick.getItemMeta().getDisplayName().equals("Lockpick")
                            && lockpick.getItemMeta().hasLore()
                            && lockpick.getItemMeta().getLore().contains("Used for breaking through locks")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void removeLockpick(Player player) {
        for (ItemStack lockpick : player.getInventory().getContents()) {
            if (lockpick != null) {
                if (lockpick.hasItemMeta()) {
                    if (lockpick.getItemMeta().hasDisplayName()) {
                        if (lockpick.getItemMeta().getDisplayName().equals("Lockpick")
                                && lockpick.getItemMeta().hasLore()
                                && lockpick.getItemMeta().getLore().contains("Used for breaking through locks")) {
                            ItemStack oneLockpick = new ItemStack(lockpick);
                            oneLockpick.setAmount(1);
                            player.getInventory().removeItem(oneLockpick);
                            break;
                        }
                    }
                }
            }
        }
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
