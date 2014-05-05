package net.wayward_realms.waywardmoderation;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AmIVanishedCommand implements CommandExecutor {

    private WaywardModeration plugin;

    public AmIVanishedCommand(WaywardModeration plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            sender.sendMessage(plugin.isVanished((Player) sender) ? ChatColor.GREEN + "You are vanished." : ChatColor.RED + "You are not vanished.");
        }
        return true;
    }
}
