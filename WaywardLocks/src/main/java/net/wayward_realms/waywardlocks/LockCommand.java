package net.wayward_realms.waywardlocks;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class LockCommand implements CommandExecutor {

    private WaywardLocks plugin;

    public LockCommand(WaywardLocks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "This command is no longer used. Please craft a lock using an iron ingot above an iron block.");
        return true;
    }

}
