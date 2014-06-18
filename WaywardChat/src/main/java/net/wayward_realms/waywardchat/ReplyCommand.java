package net.wayward_realms.waywardchat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplyCommand implements CommandExecutor {

    private WaywardChat plugin;

    public ReplyCommand(WaywardChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (plugin.getLastPrivateMessage((Player) sender) != null) {
                if (args.length >= 1) {
                    StringBuilder message = new StringBuilder();
                    for (String arg : args) {
                        message.append(arg).append(" ");
                    }
                    plugin.sendPrivateMessage((Player) sender, plugin.getLastPrivateMessage((Player) sender), message.toString());
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a message to reply with");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You have not recieved a private message recently (or the last group you recieved a message from was disbanded)");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
        }
        return true;
    }

}
