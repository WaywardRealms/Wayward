package net.wayward_realms.waywardcharacters;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleThirstCommand implements CommandExecutor {

    private WaywardCharacters plugin;

    public ToggleThirstCommand(WaywardCharacters plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            plugin.setThirstDisabled((Player) sender, !plugin.isThirstDisabled((Player) sender));
            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + (plugin.isThirstDisabled((Player) sender) ? "Thirst disabled." : "Thirst enabled."));
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
        }
        return true;
    }

}
