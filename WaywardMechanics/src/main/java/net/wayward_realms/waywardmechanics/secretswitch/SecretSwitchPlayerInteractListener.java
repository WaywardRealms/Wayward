package net.wayward_realms.waywardmechanics.secretswitch;

import net.wayward_realms.waywardmechanics.WaywardMechanics;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SecretSwitchPlayerInteractListener implements Listener {

    private WaywardMechanics plugin;

    public SecretSwitchPlayerInteractListener(WaywardMechanics plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            for (BlockFace face : BlockFace.values()) {
                Block block = event.getClickedBlock().getRelative(face);
                if (block.getState() instanceof Sign) {
                    Sign sign = (Sign) block.getState();
                    if (sign.getLine(0).equalsIgnoreCase(ChatColor.BLUE + "[switch]")) {
                        String type = sign.getLine(1);
                        final Block redstone = block.getRelative(face);
                        if (type.equalsIgnoreCase("button")) {
                            if (redstone.getType() == Material.AIR) {
                                redstone.setType(Material.REDSTONE_BLOCK);
                                redstone.getWorld().playEffect(redstone.getLocation(), Effect.CLICK1, 0);
                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                    @Override
                                    public void run() {
                                        redstone.setType(Material.AIR);
                                        redstone.getWorld().playEffect(redstone.getLocation(), Effect.CLICK2, 0);
                                    }
                                }, 20);
                            }
                        }
                        if (type.equalsIgnoreCase("lever")) {
                            if (redstone.getType() == Material.AIR) {
                                redstone.setType(Material.REDSTONE_BLOCK);
                                redstone.getWorld().playEffect(redstone.getLocation(), Effect.CLICK1, 0);
                            } else if (block.getRelative(face).getType() == Material.REDSTONE_BLOCK) {
                                redstone.setType(Material.AIR);
                                redstone.getWorld().playEffect(redstone.getLocation(), Effect.CLICK2, 0);
                            }
                        }
                    }
                }
            }
        }
    }

}
