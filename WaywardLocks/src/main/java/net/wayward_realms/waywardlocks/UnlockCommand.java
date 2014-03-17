package net.wayward_realms.waywardlocks;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnlockCommand implements CommandExecutor {

    private WaywardLocks plugin;

    public UnlockCommand(WaywardLocks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            plugin.setUnclaiming((Player) sender, true);
            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Click the block you want to unlock.");
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to lock blocks");
        }
        return true;
    }

}
