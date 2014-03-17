package net.wayward_realms.waywardessentials.command;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleLogMessagesCommand implements CommandExecutor {

    private WaywardEssentials plugin;

    public ToggleLogMessagesCommand(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.essentials.command.togglelogmessages")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                plugin.setLogMessagesEnabled(player, !plugin.isLogMessagesEnabled(player));
            }
        }
        return true;
    }
}
