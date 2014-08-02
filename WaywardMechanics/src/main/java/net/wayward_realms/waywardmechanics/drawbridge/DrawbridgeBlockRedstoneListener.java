package net.wayward_realms.waywardmechanics.drawbridge;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class DrawbridgeBlockRedstoneListener implements Listener {

    @EventHandler
    public void onBlockRedstone(BlockRedstoneEvent event) {
        if (event.getBlock().getState() instanceof Sign && event.getBlock().getRelative(BlockFace.DOWN).getState() instanceof Sign) {
            Sign sign1 = (Sign) event.getBlock().getState();
            Sign sign2 = (Sign) event.getBlock().getRelative(BlockFace.DOWN).getState();
            if (sign1.getLine(0).equalsIgnoreCase(ChatColor.GRAY + "[drawbridge]")) {
                World world = Bukkit.getServer().getWorld(sign1.getLine(1));
                int x1 = Integer.parseInt(sign1.getLine(2).split(",")[0]);
                int y1 = Integer.parseInt(sign1.getLine(2).split(",")[1]);
                int z1 = Integer.parseInt(sign1.getLine(2).split(",")[2]);
                boolean up = Boolean.parseBoolean(sign1.getLine(3));
                int upx2 = Integer.parseInt(sign2.getLine(0).split(",")[0]);
                int upy2 = Integer.parseInt(sign2.getLine(0).split(",")[1]);
                int upz2 = Integer.parseInt(sign2.getLine(0).split(",")[2]);
                int downx2 = Integer.parseInt(sign2.getLine(1).split(",")[0]);
                int downy2 = Integer.parseInt(sign2.getLine(1).split(",")[1]);
                int downz2 = Integer.parseInt(sign2.getLine(1).split(",")[2]);
                if (up) {
                    for (int x = Math.min(x1, upx2); x <= Math.max(x1, upx2); x++) {
                        for (int y = Math.min(y1, upy2); y <= Math.max(y1, upy2); y++) {
                            for (int z = Math.min(z1, upz2); z <= Math.max(z1, upz2); z++) {
                                world.getBlockAt(x, y, z).setType(Material.AIR);
                            }
                        }
                    }
                    for (int x = Math.min(x1, downx2); x <= Math.max(x1, downx2); x++) {
                        for (int y = Math.min(y1, downy2); y <= Math.max(y1, downy2); y++) {
                            for (int z = Math.min(z1, downz2); z <= Math.max(z1, downz2); z++) {
                                world.getBlockAt(x, y, z).setType(Material.WOOD);
                            }
                        }
                    }
                } else {
                    for (int x = Math.min(x1, downx2); x <= Math.max(x1, downx2); x++) {
                        for (int y = Math.min(y1, downy2); y <= Math.max(y1, downy2); y++) {
                            for (int z = Math.min(z1, downz2); z <= Math.max(z1, downz2); z++) {
                                world.getBlockAt(x, y, z).setType(Material.AIR);
                            }
                        }
                    }
                    for (int x = Math.min(x1, upx2); x <= Math.max(x1, upx2); x++) {
                        for (int y = Math.min(y1, upy2); y <= Math.max(y1, upy2); y++) {
                            for (int z = Math.min(z1, upz2); z <= Math.max(z1, upz2); z++) {
                                world.getBlockAt(x, y, z).setType(Material.WOOD);
                            }
                        }
                    }
                }
                sign1.setLine(3, "" + !up);
                sign1.update();
            }
        }
    }

}
