package net.wayward_realms.waywardcharacters;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleHungerCommand implements CommandExecutor {

    private WaywardCharacters plugin;

    public ToggleHungerCommand(WaywardCharacters plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.characters.command.togglehunger")) {
            if (sender instanceof Player) {
                plugin.setHungerDisabled((Player) sender, !plugin.isHungerDisabled((Player) sender));
                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + (plugin.isHungerDisabled((Player) sender) ? "Hunger disabled." : "Hunger enabled."));
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
        }
        return true;
    }
}
