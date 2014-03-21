package net.wayward_realms.waywardmechanics.portcullis;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PortcullisPlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                if (sign.getLine(0).equalsIgnoreCase(ChatColor.GRAY + "[portcullis]")) {
                    World world = Bukkit.getServer().getWorld(sign.getLine(1));
                    int x1 = Integer.parseInt(sign.getLine(2).split(",")[0]);
                    int y1 = Integer.parseInt(sign.getLine(2).split(",")[1]);
                    int z1 = Integer.parseInt(sign.getLine(2).split(",")[2]);
                    int x2 = Integer.parseInt(sign.getLine(3).split(",")[0]);
                    int y2 = Integer.parseInt(sign.getLine(3).split(",")[1]);
                    int z2 = Integer.parseInt(sign.getLine(3).split(",")[2]);
                    for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
                        for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
                            for (int z = Math.min(z1, z2); z <= Math.max(z1, z2); z++) {
                                if (world.getBlockAt(x, y, z).getType() == Material.AIR) {
                                    world.getBlockAt(x, y, z).setType(Material.IRON_FENCE);
                                } else {
                                    world.getBlockAt(x, y, z).setType(Material.AIR);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
