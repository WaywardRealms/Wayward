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
            } catch (NumberFormatException exception) {
                event.getBlock().breakNaturally();
                event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "Second line format must be: buy [amount] or sell [material] [amount]");
                event.getPlayer().sendMessage(ChatColor.RED + "You appear to have given a non-integer value for the amount.");
                return;
            }
            if (event.getLine(1).toLowerCase().contains("sell")) {
                if (Material.matchMaterial(event.getLine(1).split(" ")[1].replace(' ', '_')) == null) {
                    event.getBlock().breakNaturally();
                    event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "Second line format must be: buy [amount] or sell [material] [amount]");
                    event.getPlayer().sendMessage(ChatColor.RED + "You appear to have neglected to mention which material you want.");
                    return;
                }
                if (StringUtils.countMatches(event.getLine(1), " ") < 2) {
                    event.getBlock().breakNaturally();
                    event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "Second line format must be: buy [amount] or sell [material] [amount]");
                    event.getPlayer().sendMessage(ChatColor.RED + "You appear to have neglected to mention the amount and/or material you want.");
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
            }
            if (plugin.getMoney(event.getPlayer()) >= plugin.getConfig().getInt("shop.chest", 200)) {
                plugin.addMoney(event.getPlayer(), - plugin.getConfig().getInt("shop.chest", 200));
            } else if (!event.getPlayer().hasPermission("wayward.economy.shop.free")) {
                event.getBlock().breakNaturally();
                event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have enough money for a shop.");
                return;
            }
            RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
            if (characterPluginProvider != null) {
                CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                event.setLine(3, "" + characterPlugin.getActiveCharacter(event.getPlayer()).getId());
            }
        }
    }

}
