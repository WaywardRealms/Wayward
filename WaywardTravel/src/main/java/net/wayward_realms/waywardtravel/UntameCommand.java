package net.wayward_realms.waywardtravel;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UntameCommand implements CommandExecutor {

    private final WaywardTravel plugin;

    public UntameCommand(WaywardTravel plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            plugin.setUntaming(player, true);
            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Right click the tameable creature you wish to untame.");
        }
        return true;
    }
}
