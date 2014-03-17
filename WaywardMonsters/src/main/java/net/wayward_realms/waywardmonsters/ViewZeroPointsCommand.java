package net.wayward_realms.waywardmonsters;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ViewZeroPointsCommand implements CommandExecutor {

    private WaywardMonsters plugin;

    public ViewZeroPointsCommand(WaywardMonsters plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (sender.hasPermission("wayward.monsters.command.viewzeropoints")) {
                for (Location zeroPoint : plugin.getEntityLevelManager().getZeroPoints()) {
                    if (zeroPoint.getWorld().equals(player.getWorld())) {
                    player.sendBlockChange(zeroPoint, Material.DIAMOND_BLOCK, (byte) 0);
                        player.sendBlockChange(player.getWorld().getBlockAt(zeroPoint).getRelative(BlockFace.NORTH).getLocation(), Material.DIAMOND_BLOCK, (byte) 0);
                        player.sendBlockChange(player.getWorld().getBlockAt(zeroPoint).getRelative(BlockFace.SOUTH).getLocation(), Material.DIAMOND_BLOCK, (byte) 0);
                        player.sendBlockChange(player.getWorld().getBlockAt(zeroPoint).getRelative(BlockFace.EAST).getLocation(), Material.DIAMOND_BLOCK, (byte) 0);
                        player.sendBlockChange(player.getWorld().getBlockAt(zeroPoint).getRelative(BlockFace.WEST).getLocation(), Material.DIAMOND_BLOCK, (byte) 0);
                        player.sendBlockChange(player.getWorld().getBlockAt(zeroPoint).getRelative(BlockFace.NORTH_EAST).getLocation(), Material.DIAMOND_BLOCK, (byte) 0);
                        player.sendBlockChange(player.getWorld().getBlockAt(zeroPoint).getRelative(BlockFace.NORTH_WEST).getLocation(), Material.DIAMOND_BLOCK, (byte) 0);
                        player.sendBlockChange(player.getWorld().getBlockAt(zeroPoint).getRelative(BlockFace.SOUTH_EAST).getLocation(), Material.DIAMOND_BLOCK, (byte) 0);
                        player.sendBlockChange(player.getWorld().getBlockAt(zeroPoint).getRelative(BlockFace.SOUTH_WEST).getLocation(), Material.DIAMOND_BLOCK, (byte) 0);
                        player.sendBlockChange(player.getWorld().getBlockAt(zeroPoint).getRelative(BlockFace.UP).getLocation(), Material.BEACON, (byte) 0);
                        for (int x = zeroPoint.getBlockX() - 68; x < zeroPoint.getBlockX() + 68; x++) {
                            for (int z = zeroPoint.getBlockZ() - 68; z < zeroPoint.getBlockZ() + 68; z++) {
                                Block block = player.getWorld().getBlockAt(x, zeroPoint.getBlockY(), z);
                                if (block.getLocation().distanceSquared(zeroPoint) <= 4096 && block.getLocation().distanceSquared(zeroPoint) >= 3969) {
                                    player.sendBlockChange(player.getWorld().getBlockAt(x, zeroPoint.getBlockY(), z).getRelative(BlockFace.DOWN).getLocation(), Material.WOOL, (byte) 14);
                                }
                            }
                        }
                    }
                }
                player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "The zero points visible to you are now showing as beacons.");
            }
        }
        return true;
    }

}
