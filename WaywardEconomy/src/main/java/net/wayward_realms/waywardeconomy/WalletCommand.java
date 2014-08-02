package net.wayward_realms.waywardeconomy;

import net.wayward_realms.waywardlib.economy.Currency;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class WalletCommand implements CommandExecutor {

    private WaywardEconomy plugin;

    public WalletCommand(WaywardEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Currency currency = plugin.getPrimaryCurrency();
            if (args.length > 0) {
                if (plugin.getCurrency(args[0]) != null) {
                    currency = plugin.getCurrency(args[0]);
                }
            }
            Inventory wallet = plugin.getServer().createInventory(null, 27, "Wallet [" + currency.getName() + "]");
            ItemStack coin = new ItemStack(Material.GOLD_NUGGET, 1);
            ItemMeta meta = coin.getItemMeta();
            meta.setDisplayName(currency.getNameSingular());
            coin.setItemMeta(meta);
            ItemStack coinStack = new ItemStack(Material.GOLD_NUGGET, 64);
            ItemMeta stackMeta = coinStack.getItemMeta();
            stackMeta.setDisplayName(currency.getNameSingular());
            coinStack.setItemMeta(stackMeta);
            int remainder = plugin.getMoney(player, currency) % 64;
            for (int i = 0; i < plugin.getMoney(player, currency); i += 64) {
                Map<Integer, ItemStack> leftover = wallet.addItem(coinStack);
                if (!leftover.isEmpty()) {
                    player.getWorld().dropItem(player.getLocation(), leftover.values().iterator().next());
                }
            }
            if (remainder != 0) {
                ItemStack remove = new ItemStack(coin);
                remove.setAmount(64 - remainder);
                wallet.removeItem(remove);
            }
            player.openInventory(wallet);
        }
        return true;
    }

}
