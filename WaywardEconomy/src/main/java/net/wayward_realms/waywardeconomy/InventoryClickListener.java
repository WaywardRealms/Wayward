package net.wayward_realms.waywardeconomy;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

public class InventoryClickListener implements Listener {

    private WaywardEconomy plugin;

    public InventoryClickListener(WaywardEconomy plugin) {
        this.plugin =  plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().toLowerCase().contains("wallet")) {
            if (event.getCurrentItem() != null) {
                event.setCancelled(true);
                if (event.getCurrentItem().hasItemMeta()) {
                    if (event.getCurrentItem().getItemMeta().hasDisplayName()) {
                        if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(plugin.getCurrency(event.getInventory().getTitle().split("\\[")[1].replace("]", "")).getNameSingular())) {
                            event.setCancelled(false);
                        }
                    }
                }
                if (event.getCurrentItem().getType() == Material.AIR) {
                    event.setCancelled(false);
                }
                if (event.isCancelled()) {
                    ((Player) event.getWhoClicked()).sendMessage(plugin.getPrefix() + ChatColor.RED + "You may not place non-currency items or different currencies in a wallet");
                }
            }
        }
        if (event.getInventory().getType() == InventoryType.CHEST) {
            if (event.getCurrentItem() != null) {
                if (event.getCurrentItem().getType() != Material.AIR) {
                    if (event.getInventory().getHolder() instanceof Chest) {
                        if (event.getSlot() == event.getRawSlot()) {
                            Chest chest = (Chest) event.getInventory().getHolder();
                            Player player = (Player) event.getWhoClicked();
                            ItemStack item = new ItemStack(event.getCurrentItem());
                            if (chest.getBlock().getRelative(BlockFace.UP).getState() instanceof Sign) {
                                Sign sign = (Sign) chest.getBlock().getRelative(BlockFace.UP).getState();
                                if (sign.getLine(0).equalsIgnoreCase(ChatColor.DARK_PURPLE + "[shop]")) {
                                    if (sign.getLine(1).contains("sell ")) {
                                        ((Player) event.getWhoClicked()).sendMessage(plugin.getPrefix() + ChatColor.RED + "This is a sell shop, you may not steal items.");
                                        event.setCancelled(true);
                                        return;
                                    }
                                    RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                                    if (characterPluginProvider != null) {
                                        CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                                        try {
                                            if (sign.getLine(3).equalsIgnoreCase("admin") || characterPlugin.getActiveCharacter(player).getId() != Integer.parseInt(sign.getLine(3))) {
                                                event.setCancelled(true);
                                                if (plugin.getMoney(player) >= Integer.parseInt(sign.getLine(2).split(" ")[1])) {
                                                    if (sign.getLine(3).equalsIgnoreCase("admin")) {
                                                        plugin.addMoney(player, -Integer.parseInt(sign.getLine(2).split(" ")[1]));
                                                    } else {
                                                        plugin.transferMoney(characterPlugin.getActiveCharacter(player), characterPlugin.getCharacter(Integer.parseInt(sign.getLine(3))), Integer.parseInt(sign.getLine(2).split(" ")[1]));
                                                    }
                                                    item.setAmount(Integer.parseInt(sign.getLine(1).split(" ")[1]));
                                                    player.getInventory().addItem(item);
                                                    chest.getInventory().removeItem(item);
                                                    player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Bought " + item.getAmount() + " x " + item.getType().toString().toLowerCase().replace('_', ' ') + " for " + sign.getLine(2).split(" ")[1] + " " + (Integer.parseInt(sign.getLine(1).split(" ")[1]) == 1 ? plugin.getPrimaryCurrency().getNameSingular() : plugin.getPrimaryCurrency().getNamePlural()));
                                                    if (!sign.getLine(3).equalsIgnoreCase("admin") && characterPlugin.getCharacter(Integer.parseInt(sign.getLine(3))).getPlayer().isOnline()) {
                                                        characterPlugin.getCharacter(Integer.parseInt(sign.getLine(3))).getPlayer().getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + player.getDisplayName() + " bought " + item.getAmount() + " x " + item.getType().toString().toLowerCase().replace('_', ' ') + " for " + sign.getLine(2).split(" ")[1] + " " + (Integer.parseInt(sign.getLine(1).split(" ")[1]) == 1 ? plugin.getPrimaryCurrency().getNameSingular() : plugin.getPrimaryCurrency().getNamePlural()) + " from your shop.");
                                                    }
                                                } else {
                                                    player.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have enough money.");
                                                }
                                            }
                                        } catch (NumberFormatException ignored) {}
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
