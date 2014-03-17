package net.wayward_realms.waywardeconomy;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
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
            if (!(event.getLine(1).toLowerCase().contains("buy") || event.getLine(1).toLowerCase().contains("sell")) || !event.getLine(1).contains(":")) {
                event.getBlock().breakNaturally();
                event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "Second line format must be: buy:buyprice or sell:sellprice");
                return;
            }
            try {
                Integer.parseInt(event.getLine(1).split(":")[1]);
            } catch (NumberFormatException exception) {
                event.getBlock().breakNaturally();
                event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "Second line format must be: buy:buyprice or sell:sellprice");
                return;
            }
            if (event.getLine(1).toLowerCase().contains("sell")) {
                if (Material.getMaterial(event.getLine(2).toUpperCase().replace(' ', '_')) == null) {
                    event.getBlock().breakNaturally();
                    event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "Third line on a sell sign must state the material");
                    return;
                }
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
