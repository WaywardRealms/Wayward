package net.wayward_realms.waywardeconomy;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

public class SignChangeListener implements Listener {

    private WaywardEconomy plugin;

    public SignChangeListener(WaywardEconomy plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        // Shop signs
        if (event.getLine(0).equalsIgnoreCase("[shop]")) {
            event.setLine(0, ChatColor.DARK_PURPLE + "[shop]");
            if (!(event.getLine(1).toLowerCase().contains("buy ") || event.getLine(1).toLowerCase().contains("sell "))) {
                event.getBlock().breakNaturally();
                event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "Second line format must be: buy [amount] or sell [material] [amount]");
                event.getPlayer().sendMessage(ChatColor.RED + "You appear to have neglected to mention whether this is a buy or sell shop.");
                return;
            }
            try {
                Integer.parseInt(event.getLine(1).split(" ")[event.getLine(1).contains("buy") ? 1 : 2]);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException exception) {
                event.getBlock().breakNaturally();
                event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "Second line format must be: buy [amount] or sell [material] [amount]");
                event.getPlayer().sendMessage(ChatColor.RED + "You appear to have given a non-integer value for the amount, or failed to specify an amount..");
                return;
            }
            if (event.getLine(1).toLowerCase().contains("sell")) {
                if (StringUtils.countMatches(event.getLine(1), " ") < 2) {
                    event.getBlock().breakNaturally();
                    event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "Second line format must be: buy [amount] or sell [material] [amount]");
                    event.getPlayer().sendMessage(ChatColor.RED + "You appear to have neglected to mention the amount and/or material you want.");
                    return;
                }
                if (Material.matchMaterial(event.getLine(1).split(" ")[1].replace(' ', '_')) == null) {
                    event.getBlock().breakNaturally();
                    event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "Second line format must be: buy [amount] or sell [material] [amount]");
                    event.getPlayer().sendMessage(ChatColor.RED + "You appear to have neglected to mention which material you want.");
                    return;
                }
            }
            try {
                if (!event.getLine(2).contains("for ")) {
                    event.setLine(2, "for " + Integer.parseInt(event.getLine(2)));
                } else {
                    Integer.parseInt(event.getLine(2).replace("for ", ""));
                }
            } catch (NumberFormatException exception) {
                event.getBlock().breakNaturally();
                event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "Third line format must be: for [price]");
                return;
            }
            if (plugin.getMoney(event.getPlayer()) >= plugin.getConfig().getInt("shop.cost", 200)) {
                plugin.addMoney(event.getPlayer(), - plugin.getConfig().getInt("shop.cost", 200));
                event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Bought a shop for " + plugin.getConfig().getInt("shop.cost", 200) + " " + plugin.getPrimaryCurrency().getNamePlural());
            } else if (!event.getPlayer().hasPermission("wayward.economy.shop.free")) {
                event.getBlock().breakNaturally();
                event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have enough money for a shop.");
                return;
            }
            if (event.getLine(3).equalsIgnoreCase("admin")) {
                if (!event.getPlayer().hasPermission("wayward.economy.shop.admin")) {
                    event.getBlock().breakNaturally();
                    event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission to create admin shops.");
                }
            } else {
                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                if (characterPluginProvider != null) {
                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                    event.setLine(3, "" + characterPlugin.getActiveCharacter(event.getPlayer()).getId());
                }
            }
        }
        // Bank signs
        if (event.getLine(0).equalsIgnoreCase("[bank]")) {
            event.setLine(0, ChatColor.GOLD + "[bank]");
            if (!event.getPlayer().hasPermission("wayward.economy.bank.create")) {
                event.getBlock().breakNaturally();
                event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission to create banks.");
                return;
            }
            if (!(event.getLine(1).equalsIgnoreCase("withdraw") || event.getLine(1).equalsIgnoreCase("deposit") || event.getLine(1).equalsIgnoreCase("balance"))) {
                event.getBlock().breakNaturally();
                event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "Second line must be: \"withdraw\", \"deposit\" or \"balance\"");
                return;
            }
            if (event.getLine(1).equalsIgnoreCase("balance")) {
                event.setLine(2, "");
            } else {
                event.setLine(2, "1");
            }
            if (plugin.getCurrency(event.getLine(3)) == null) {
                event.setLine(3, plugin.getPrimaryCurrency().getName());
            }
        }
    }

}
