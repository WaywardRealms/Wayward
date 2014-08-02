package net.wayward_realms.waywardeconomy;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.economy.Currency;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

public class PlayerInteractListener implements Listener {

    private WaywardEconomy plugin;

    public PlayerInteractListener(WaywardEconomy plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.hasBlock()) {
            if (event.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                // Banks
                if (sign.getLine(0).equalsIgnoreCase(ChatColor.GOLD + "[bank]")) {
                    event.setCancelled(true);
                    RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                    if (characterPluginProvider != null) {
                        CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                        Character character = characterPlugin.getActiveCharacter(event.getPlayer());
                        Currency currency = plugin.getCurrency(sign.getLine(3));
                        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                            switch (sign.getLine(2)) {
                                case "1":
                                    sign.setLine(2, "10");
                                    sign.update();
                                    break;
                                case "10":
                                    sign.setLine(2, "100");
                                    sign.update();
                                    break;
                                case "100":
                                    sign.setLine(2, "1000");
                                    sign.update();
                                    break;
                                case "1000":
                                    sign.setLine(2, "1");
                                    sign.update();
                                    break;
                                default:
                                    break;
                            }
                        } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                            if (sign.getLine(1).equalsIgnoreCase("withdraw")) {
                                if (plugin.getMoney(character, currency) + Integer.parseInt(sign.getLine(2)) > plugin.getMaximumMoney()) {
                                    event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "You cannot withdraw that amount, it would not fit in your wallet.");
                                } else if (Integer.parseInt(sign.getLine(2)) > plugin.getBankBalance(character, currency)) {
                                    event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "You cannot withdraw that amount, your bank balance is not high enough.");
                                } else {
                                    plugin.setBankBalance(character, currency, plugin.getBankBalance(character, currency) - Integer.parseInt(sign.getLine(2)));
                                    plugin.addMoney(character, currency, Integer.parseInt(sign.getLine(2)));
                                    event.getPlayer().sendMessage(new String[] {
                                            plugin.getPrefix() + ChatColor.GREEN + "Withdrew " + sign.getLine(2) + " " + (Integer.parseInt(sign.getLine(2)) == 1 ? currency.getNameSingular() : currency.getNamePlural()),
                                            ChatColor.GRAY + "Wallet balance: " + plugin.getMoney(character, currency),
                                            ChatColor.GRAY + "Bank balance: " + plugin.getBankBalance(character, currency)
                                    });
                                }
                            } else if (sign.getLine(1).equalsIgnoreCase("deposit")) {
                                if (Integer.parseInt(sign.getLine(2)) > plugin.getMoney(character, currency)) {
                                    event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "You cannot deposit that amount, your wallet balance is not high enough.");
                                } else {
                                    plugin.setBankBalance(character, currency, plugin.getBankBalance(character, currency) + Integer.parseInt(sign.getLine(2)));
                                    plugin.addMoney(character, currency, -Integer.parseInt(sign.getLine(2)));
                                    event.getPlayer().sendMessage(new String[] {
                                            plugin.getPrefix() + ChatColor.GREEN + "Deposited " + sign.getLine(2) + " " + (Integer.parseInt(sign.getLine(2)) == 1 ? currency.getNameSingular() : currency.getNamePlural()),
                                            ChatColor.GRAY + "Wallet balance: " + plugin.getMoney(character, currency),
                                            ChatColor.GRAY + "Bank balance: " + plugin.getBankBalance(character, currency)
                                    });
                                }
                            } else if (sign.getLine(1).equalsIgnoreCase("balance")) {
                                event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Balance: " + plugin.getBankBalance(character, currency));
                            }
                        }
                    }
                }
                // Shops
                if (event.getClickedBlock().getRelative(BlockFace.DOWN).getType() == Material.CHEST) {
                    if (event.getClickedBlock().getRelative(BlockFace.DOWN).getState() instanceof Chest) {
                        Chest chest = (Chest) event.getClickedBlock().getRelative(BlockFace.DOWN).getState();
                        if (sign.getLine(0).equalsIgnoreCase(ChatColor.DARK_PURPLE + "[shop]")) {
                            event.setCancelled(true);
                            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                                if (characterPluginProvider != null) {
                                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                                    if ((sign.getLine(3).equalsIgnoreCase("admin") && event.getPlayer().hasPermission("wayward.economy.shop.admin")) || characterPlugin.getActiveCharacter(event.getPlayer()).getId() == Integer.parseInt(sign.getLine(3))) {
                                        event.getClickedBlock().setType(Material.AIR);
                                        plugin.addMoney(characterPlugin.getActiveCharacter(event.getPlayer()), plugin.getConfig().getInt("shop.sell", 50));
                                        event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Sold shop for " + plugin.getConfig().getInt("shop.sell", 50) + " " + plugin.getPrimaryCurrency().getNamePlural());
                                        return;
                                    }
                                }
                            }
                            if (validateShopSign(sign, event.getPlayer())) {
                                if (sign.getLine(1).toLowerCase().contains("buy")) {
                                    event.getPlayer().openInventory(chest.getInventory());
                                } else if (sign.getLine(1).toLowerCase().contains("sell")) {
                                    if (event.getPlayer().getItemInHand().getType() == Material.matchMaterial(sign.getLine(1).split(" ")[1].replace(' ', '_'))) {
                                        if (event.getPlayer().getItemInHand().getAmount() >= Integer.parseInt(sign.getLine(1).split(" ")[2])) {
                                            RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                                            if (characterPluginProvider != null) {
                                                CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                                                try {
                                                    event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Sold " + Integer.parseInt(sign.getLine(1).split(" ")[2]) + " x " + event.getPlayer().getItemInHand().getType().toString().toLowerCase().replace('_', ' ') + " for " + Integer.parseInt(sign.getLine(2).replace("for ", "")) + " " + (Integer.parseInt(sign.getLine(2).replace("for ", "")) == 1 ? plugin.getPrimaryCurrency().getNameSingular() : plugin.getPrimaryCurrency().getNamePlural()));
                                                    if (characterPlugin.getCharacter(Integer.parseInt(sign.getLine(3))).getPlayer().isOnline()) {
                                                        characterPlugin.getCharacter(Integer.parseInt(sign.getLine(3))).getPlayer().getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Your shop bought " + Integer.parseInt(sign.getLine(1).split(" ")[2]) + " x " + event.getPlayer().getItemInHand().getType().toString().toLowerCase().replace('_', ' ') + " for " + Integer.parseInt(sign.getLine(2).replace("for ", "")) + (Integer.parseInt(sign.getLine(2).replace("for ", "")) == 1 ? plugin.getPrimaryCurrency().getNameSingular() : plugin.getPrimaryCurrency().getNamePlural()) + " from " + event.getPlayer().getDisplayName());
                                                    }
                                                    if (sign.getLine(3).equalsIgnoreCase("admin")) {
                                                        plugin.addMoney(event.getPlayer(), -Integer.parseInt(sign.getLine(2).split(" ")[1]));
                                                    } else {
                                                        plugin.transferMoney(characterPlugin.getCharacter(Integer.parseInt(sign.getLine(3))), characterPlugin.getActiveCharacter(event.getPlayer()), Integer.parseInt(sign.getLine(2).replace("for ", "")));
                                                    }
                                                    ItemStack item = new ItemStack(event.getPlayer().getItemInHand());
                                                    item.setAmount(Integer.parseInt(sign.getLine(1).split(" ")[2]));
                                                    chest.getInventory().addItem(item);
                                                    if (event.getPlayer().getItemInHand().getAmount() > Integer.parseInt(sign.getLine(1).split(" ")[2])) {
                                                        event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount() - Integer.parseInt(sign.getLine(1).split(" ")[2]));
                                                    } else {
                                                        event.getPlayer().setItemInHand(null);
                                                    }
                                                } catch (NumberFormatException ignored) {
                                                }
                                            }
                                        } else {
                                            event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have enough " + event.getPlayer().getItemInHand().getType().toString().toLowerCase().replace('_', ' ') + "s to sell");
                                        }
                                    } else {
                                        event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be holding at least " + Integer.parseInt(sign.getLine(1).split(" ")[2]) + " " + Material.matchMaterial(sign.getLine(1).split(" ")[1].replace(' ', '_')).toString().toLowerCase().replace('_', ' ') + "s");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean validateShopSign(Sign sign, Player player) {
        if (sign.getLine(0).equalsIgnoreCase(ChatColor.DARK_PURPLE + "[shop]")) {
            RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
            if (characterPluginProvider != null) {
                CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                try {
                    if (sign.getLine(3).equalsIgnoreCase("admin") || characterPlugin.getCharacter(Integer.parseInt(sign.getLine(3))) != null) {
                        String playerName = sign.getLine(3).equalsIgnoreCase("admin") ? "an admin" : characterPlugin.getCharacter(Integer.parseInt(sign.getLine(3))).getPlayer().getName();
                        if (!(sign.getLine(1).toLowerCase().contains("buy ") || sign.getLine(1).toLowerCase().contains("sell "))) {
                            player.sendMessage(plugin.getPrefix() + ChatColor.RED + "The shop is formatted incorrectly. Please get " + playerName + " to update accordingly.");
                            return false;
                        }
                        try {
                            Integer.parseInt(sign.getLine(1).split(" ")[sign.getLine(1).contains("buy") ? 1 : 2]);
                        } catch (NumberFormatException exception) {
                            player.sendMessage(plugin.getPrefix() + ChatColor.RED + "The shop is formatted incorrectly. Please get " + playerName + " to update accordingly.");
                            return false;
                        }
                        if (sign.getLine(1).toLowerCase().contains("sell")) {
                            if (Material.matchMaterial(sign.getLine(1).split(" ")[1].replace(' ', '_')) == null) {
                                player.sendMessage(plugin.getPrefix() + ChatColor.RED + "The shop is formatted incorrectly. Please get " + playerName + " to update accordingly.");
                                return false;
                            }
                            if (StringUtils.countMatches(sign.getLine(1), " ") < 2) {
                                player.sendMessage(plugin.getPrefix() + ChatColor.RED + "The shop is formatted incorrectly. Please get " + playerName + " to update accordingly.");
                                return false;
                            }
                        }
                        try {
                            if (!sign.getLine(2).contains("for ")) {
                                sign.setLine(2, "for " + Integer.parseInt(sign.getLine(2)));
                            } else {
                                Integer.parseInt(sign.getLine(2).replace("for ", ""));
                            }
                        } catch (NumberFormatException exception) {
                            player.sendMessage(plugin.getPrefix() + ChatColor.RED + "The shop is formatted incorrectly. Please get " + playerName + " to update accordingly.");
                            return false;
                        }
                    }
                } catch (NumberFormatException exception) {
                    player.sendMessage(plugin.getPrefix() + ChatColor.RED + "The shop is formatted incorrectly. Please get the shop owner to update accordingly.");
                    return false;
                }
            }
        }
        return true;
    }

}
