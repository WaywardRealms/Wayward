package net.wayward_realms.waywardmechanics.chairs;

import net.wayward_realms.waywardmechanics.WaywardMechanics;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Stairs;
import org.bukkit.material.Step;
import org.bukkit.material.WoodenStep;

public class ChairPlayerInteractListener implements Listener {

    private WaywardMechanics plugin;

    public ChairPlayerInteractListener(WaywardMechanics plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();
            if (sitAllowed(player, block)) {
                event.setCancelled(true);
                Location sitLocation = getSitLocation(block, player.getLocation().getYaw());
                plugin.getChairManager().sitPlayer(player, block, sitLocation);
            }
        }
    }

    private boolean sitAllowed(Player player, Block block) {
        if (isSitting(player)) {
            return false;
        }
        if (player.getItemInHand().getType() != Material.AIR) {
            return false;
        }
        if (player.isSneaking()) {
            return false;
        }
        if (plugin.getChairManager().isBlockOccupied(block)) {
            player.sendMessage(plugin.getPrefix() + ChatColor.RED + "There is already a player sitting there.");
            return false;
        }
        Stairs stairs = null;
        Step step = null;
        WoodenStep wStep = null;
        if (isValidChair(block)) {
            if (block.getState().getData() instanceof Stairs) {
                stairs = (Stairs) block.getState().getData();
            } else if (block.getState().getData() instanceof Step) {
                step = (Step) block.getState().getData();
            } else if (block.getState().getData() instanceof WoodenStep) {
                wStep = (WoodenStep) block.getState().getData();
            }
            int chairwidth = 1;
            if (block.getRelative(BlockFace.DOWN).isLiquid()) {
                return false;
            }
            if (block.getRelative(BlockFace.DOWN).isEmpty()) {
                return false;
            }
            if (!block.getRelative(BlockFace.DOWN).getType().isSolid()) {
                return false;
            }
            if (player.getLocation().distance(block.getLocation().add(0.5, 0, 0.5)) > 8) {
                return false;
            }
            if (stairs != null && stairs.isInverted()) {
                return false;
            }
            if (step != null && step.isInverted()) {
                return false;
            }
            if (wStep != null && wStep.isInverted()) {
                return false;
            }
            if (stairs != null) {
                if (stairs.getDescendingDirection() == BlockFace.NORTH || stairs.getDescendingDirection() == BlockFace.SOUTH) {
                    chairwidth += getChairWidth(block, BlockFace.EAST);
                    chairwidth += getChairWidth(block, BlockFace.WEST);
                } else if (stairs.getDescendingDirection() == BlockFace.EAST || stairs.getDescendingDirection() == BlockFace.WEST) {
                    chairwidth += getChairWidth(block, BlockFace.NORTH);
                    chairwidth += getChairWidth(block, BlockFace.SOUTH);
                }
                if (chairwidth > 4) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private Location getSitLocation(Block block, Float playerYaw) {
        double sh = 0.7;
        Stairs stairs = null;
        if (block.getState().getData() instanceof Stairs) {
            stairs = (Stairs) block.getState().getData();
        }
        Location plocation = block.getLocation();
        plocation.add(0.5D, (sh - 0.5D), 0.5D);
        if (stairs != null) {
            switch (stairs.getDescendingDirection()) {
                case NORTH: {
                    plocation.setYaw(180);
                    break;
                }
                case EAST: {
                    plocation.setYaw(-90);
                    break;
                }
                case SOUTH: {
                    plocation.setYaw(0);
                    break;
                }
                case WEST: {
                    plocation.setYaw(90);
                    break;
                }
                default: {
                }
            }
        } else {
            plocation.setYaw(playerYaw);
        }
        return plocation;
    }

    private boolean isValidChair(Block block) {
        return block.getType().toString().endsWith("_STAIRS");
    }

    private boolean isSitting(Player player) {
        return plugin.getChairManager().isSitting(player);
    }

    private int getChairWidth(Block block, BlockFace face) {
        int width = 0;
        for (int i = 1; i <= 4; i++) {
            Block relative = block.getRelative(face, i);
            if (relative.getState().getData() instanceof Stairs) {
                if (isValidChair(relative) && ((Stairs) relative.getState().getData()).getDescendingDirection() == ((Stairs) block.getState().getData()).getDescendingDirection()) {
                    width++;
                } else {
                    break;
                }
            }
        }
        return width;
    }

}
