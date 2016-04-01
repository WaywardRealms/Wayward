package net.wayward_realms.waywardmonsters.trainingdummy;

import net.wayward_realms.waywardlib.classes.ClassesPlugin;
import net.wayward_realms.waywardlib.professions.ToolType;
import net.wayward_realms.waywardmonsters.WaywardMonsters;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

public class TrainingDummyPlayerInteractListener implements Listener {

    private WaywardMonsters plugin;

    public TrainingDummyPlayerInteractListener(WaywardMonsters plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            for (int i = 0; i < 6; i++) {
                Block block = event.getClickedBlock().getRelative(BlockFace.DOWN, i);
                if (block.getState() instanceof Sign) {
                    Sign sign = (Sign) block.getState();
                    if (sign.getLine(0).equalsIgnoreCase(ChatColor.DARK_RED + "[dummy]")) {
                        int uses = 0;
                        try {
                            uses = Integer.parseInt(sign.getLine(1));
                        } catch (NumberFormatException exception) {
                            event.getPlayer().sendMessage(ChatColor.RED + "Could not parse dummy uses");
                        }
                        if (uses >= 19) {
                            ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
                            ToolType toolType = ToolType.getToolType(item.getType());
                            if (toolType != null && toolType == ToolType.SWORD) {
                                item.setDurability((short) (item.getDurability() + 5));
                                if (item.getDurability() >= item.getType().getMaxDurability()) {
                                    event.getPlayer().getInventory().setItemInMainHand(null);
                                }
                            } else {
                                event.getPlayer().damage(0.5D);
                            }
                            RegisteredServiceProvider<ClassesPlugin> classesPluginProvider = plugin.getServer().getServicesManager().getRegistration(ClassesPlugin.class);
                            if (classesPluginProvider != null) {
                                ClassesPlugin classesPlugin = classesPluginProvider.getProvider();
                                classesPlugin.giveExperience(event.getPlayer(), 1);
                            }
                            uses = 0;
                        } else {
                            uses++;
                        }
                        event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + (20 - uses) + " hits remaining!");
                        sign.setLine(1, "" + uses);
                        sign.update();
                    }
                }
            }
        }
    }

}
