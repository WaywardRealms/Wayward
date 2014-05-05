package net.wayward_realms.waywardchat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageCommand implements CommandExecutor {

    private WaywardChat plugin;

    public MessageCommand(WaywardChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 2) {
            if (plugin.getChatGroup(args[0]) != null) {
                if (plugin.getChatGroup(args[0]).getPlayers().contains(sender.getName())) {
                    StringBuilder message = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        message.append(args[i]).append(" ");
                    }
                    plugin.sendPrivateMessage((Player) sender, plugin.getChatGroup(args[0]), message.toString());
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You are not a member of that chat group");
                }
            } else if (plugin.getServer().getPlayer(args[0]) != null) {
                Player player = plugin.getServer().getPlayer(args[0]);
                ChatGroup chatGroup = new ChatGroup(plugin, "_pm_" + sender.getName() + "_" + player.getName(), (Player) sender, player);
                plugin.addChatGroup(chatGroup);
                StringBuilder message = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    message.append(args[i]).append(" ");
                }
                plugin.sendPrivateMessage((Player) sender, chatGroup, message.toString());
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Could not find a group or player by that name");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a group or player and a message.");
        }
        return true;
    }

}
