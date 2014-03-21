package net.wayward_realms.waywardmechanics.chairs;

import net.wayward_realms.waywardmechanics.WaywardMechanics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ChairManager {

    private WaywardMechanics plugin;

    public ChairManager(WaywardMechanics plugin) {
        this.plugin = plugin;
    }

    private HashMap<String, Entity> sit = new HashMap<>();
    private HashMap<Block, String> sitBlock = new HashMap<>();
    private HashMap<String, Location> sitStopTeleportLoc = new HashMap<>();
    private HashMap<String, Integer> sitTask = new HashMap<>();

    public boolean isSitting(Player player) {
        return sit.containsKey(player.getName());
    }

    public boolean isBlockOccupied(Block block) {
        return sitBlock.containsKey(block);
    }

    public Player getPlayerOnChair(Block chair) {
        return Bukkit.getPlayerExact(sitBlock.get(chair));
    }

    public void sitPlayer(Player player, Block blocktooccupy, Location sitlocation) {
        try {
            player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "You are now sitting.");
            sitStopTeleportLoc.put(player.getName(), player.getLocation());
            player.teleport(sitlocation);
            Location arrowloc = sitlocation.getBlock().getLocation().add(0.5, 0 , 0.5);
            Entity arrow = arrowloc.getWorld().spawnEntity(arrowloc, EntityType.ARROW);
            arrow.setPassenger(player);
            sit.put(player.getName(), arrow);
            sitBlock.put(blocktooccupy, player.getName());
            startReSitTask(player);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void startReSitTask(final Player player) {
        int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                reSitPlayer(player);
            }
        }, 1000, 1000);
        sitTask.put(player.getName(), task);
    }

    public void reSitPlayer(final Player player) {
        try {
            final Entity prevarrow = sit.get(player.getName());
            sit.remove(player.getName());
            player.eject();
            Entity arrow = prevarrow.getLocation().getWorld().spawnEntity(prevarrow.getLocation(), EntityType.ARROW);
            arrow.setPassenger(player);
            sit.put(player.getName(), arrow);
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    prevarrow.remove();
                }
            }, 100);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void unsitPlayerNormal(Player player) {
        unsitPlayer(player, false, true, false);
    }

    public void unsitPlayerForce(Player player) {
        unsitPlayer(player, true, true, false);
    }

    public void unsitPlayerNow(Player player) {
        unsitPlayer(player, true, false, true);
    }

    private void unsitPlayer(final Player player, boolean eject, boolean restoreposition, boolean correctleaveposition) {
        final Entity arrow = sit.get(player.getName());
        sit.remove(player.getName());
        if (eject) {
            player.eject();
        }
        arrow.remove();
        final Location tploc = sitStopTeleportLoc.get(player.getName());
        if (restoreposition) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(
                    plugin,
                    new Runnable() {
                        @Override
                        public void run() {
                            player.teleport(tploc);
                            player.setSneaking(false);
                        }
                    },
                    1
            );
        } else if (correctleaveposition) {
            player.teleport(tploc);
        }
        sitBlock.values().remove(player.getName());
        sitStopTeleportLoc.remove(player.getName());
        Bukkit.getScheduler().cancelTask(sitTask.get(player.getName()));
        sitTask.remove(player.getName());
        player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "You are now standing.");
    }

}
