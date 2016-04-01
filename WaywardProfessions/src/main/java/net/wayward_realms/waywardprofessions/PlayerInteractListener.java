package net.wayward_realms.waywardprofessions;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.professions.ToolType;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

public class PlayerInteractListener implements Listener {

    private WaywardProfessions plugin;

    public PlayerInteractListener(WaywardProfessions plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (event.getClickedBlock().getType() == Material.ANVIL) {
                    event.setCancelled(true);
                    if (event.getPlayer().getInventory().getItemInMainHand() != null) {
                        ItemStack itemInHand = event.getPlayer().getInventory().getItemInMainHand();
                        Material material = itemInHand.getType();
                        ToolType toolType = ToolType.getToolType(material);
                        if (toolType != null) {
                            RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                            if (characterPluginProvider != null) {
                                CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                                Character character = characterPlugin.getActiveCharacter(event.getPlayer());
                                ItemStack cost = null;
                                if (material.toString().startsWith("WOOD_")) {
                                    cost = new ItemStack(Material.WOOD);
                                } else if (material.toString().startsWith("STONE_")) {
                                    cost = new ItemStack(Material.COBBLESTONE);
                                } else if (material.toString().startsWith("IRON_") || material.toString().startsWith("CHAINMAIL_")) {
                                    cost = new ItemStack(Material.IRON_INGOT);
                                } else if (material.toString().startsWith("GOLD_")) {
                                    cost = new ItemStack(Material.GOLD_INGOT);
                                } else if (material.toString().startsWith("DIAMOND_")) {
                                    cost = new ItemStack(Material.DIAMOND);
                                } else if (material.toString().startsWith("LEATHER_")) {
                                    cost = new ItemStack(Material.LEATHER);
                                } else if (material == Material.BOW) {
                                    cost = new ItemStack(Material.STICK);
                                }
                                if (cost == null || event.getPlayer().getInventory().containsAtLeast(cost, 1)) {
                                    event.getPlayer().getInventory().removeItem(cost);
                                    event.getPlayer().getInventory().getItemInMainHand().setDurability((short) (material.getMaxDurability() - plugin.getMaxToolDurability(character, toolType)));
                                    event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Item repaired.");
                                } else {
                                    event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "You need at least " + cost.getAmount() + " " + cost.getType().toString().toLowerCase().replace("_", " ") + (cost.getAmount() != 1 ? "s" : "") + " to repair your " + material.toString().toLowerCase().replace("_", " "));
                                }
                            }
                        } else {
                            event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be holding a repairable item to use an anvil");
                        }
                    } else {
                        event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be holding a repairable item to use an anvil");
                    }
                }
            }
        }
    }

}
