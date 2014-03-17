package net.wayward_realms.waywardeconomy;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

public class PlayerInteractListener implements Listener {

    private WaywardEconomy plugin;

    public PlayerInteractListener(WaywardEconomy plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.hasBlock()) {
            if (event.getClickedBlock().getState() instanceof Sign && event.getClickedBlock().getRelative(BlockFace.DOWN).getType() == Material.CHEST) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                if (event.getClickedBlock().getRelative(BlockFace.DOWN).getState() instanceof Chest) {
                    Chest chest = (Chest) event.getClickedBlock().getRelative(BlockFace.DOWN).getState();
                    if (sign.getLine(0).equalsIgnoreCase(ChatColor.DARK_PURPLE + "[shop]")) {
                        if (sign.getLine(1).toLowerCase().contains("buy")) {
                            event.getPlayer().openInventory(chest.getInventory());
                            /*for (ItemStack item : chest.getInventory().getContents()) {
                                if (item != null) {
                                    RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                                    if (characterPluginProvider != null) {
                                        CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                                        try {
                                            plugin.transferMoney(characterPlugin.getActiveCharacter(event.getPlayer()), characterPlugin.getCharacter(Integer.parseInt(sign.getLine(3))), Integer.parseInt(sign.getLine(1).split(":")[1]));
                                            event.getPlayer().getInventory().addItem(item);
                                            chest.getInventory().removeItem(item);
                                            event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Bought " + item.getAmount() + " x " + item.getType().toString().toLowerCase().replace('_', ' ') + " for " + sign.getLine(1).split(":")[1] + " " + (Integer.parseInt(sign.getLine(1).split(":")[1]) == 1 ? plugin.getPrimaryCurrency().getNameSingular() : plugin.getPrimaryCurrency().getNamePlural()));
                                        } catch (NumberFormatException ignored) {}
                                    }
                                    return;
                                }
                            }*/
                        } else if (sign.getLine(1).toLowerCase().contains("sell")) {
                            if (event.getPlayer().getItemInHand().getType() == Material.matchMaterial(sign.getLine(2))) {
                                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                                if (characterPluginProvider != null) {
                                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                                    try {
                                        event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Sold 1 x " + event.getPlayer().getItemInHand().getType().toString().toLowerCase().replace('_', ' ') + " for " + sign.getLine(1).split(":")[1] + " " + (Integer.parseInt(sign.getLine(1).split(":")[1]) == 1 ? plugin.getPrimaryCurrency().getNameSingular() : plugin.getPrimaryCurrency().getNamePlural()));
                                        plugin.transferMoney(characterPlugin.getCharacter(Integer.parseInt(sign.getLine(3))), characterPlugin.getActiveCharacter(event.getPlayer()), Integer.parseInt(sign.getLine(1).split(":")[1]));
                                        chest.getInventory().addItem(event.getPlayer().getItemInHand());
                                        if (event.getPlayer().getItemInHand().getAmount() > 1) {
                                            event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount() - 1);
                                        } else {
                                            event.getPlayer().setItemInHand(null);
                                        }

                                    } catch (NumberFormatException ignored) {
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
