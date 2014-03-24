package net.wayward_realms.waywardprofessions;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DurabilityCommand implements CommandExecutor {

    private final WaywardProfessions plugin;

    public DurabilityCommand(WaywardProfessions plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (((Player) sender).getItemInHand() != null) {
                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Durability: " + (((Player) sender).getItemInHand().getType().getMaxDurability() - ((Player) sender).getItemInHand().getDurability()));
            }
        }
        return true;
    }

}
