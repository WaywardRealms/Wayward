package net.wayward_realms.waywardessentials.command;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SudoCommand implements CommandExecutor {

    private WaywardEssentials plugin;

    public SudoCommand(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.essentials.command.sudo")) {
            if (sender instanceof Player) {
                sender.setOp(true);
            }
            StringBuilder sudoCommand = new StringBuilder();
            for (String arg : args) {
                sudoCommand.append(arg).append(" ");
            }
            plugin.getServer().dispatchCommand(sender, sudoCommand.toString());
            if (sender instanceof Player) {
                sender.setOp(false);
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
        }
        return true;
    }

}
