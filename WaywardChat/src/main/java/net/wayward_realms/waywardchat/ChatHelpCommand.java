package net.wayward_realms.waywardchat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ChatHelpCommand implements CommandExecutor {

    private VaylerynChat plugin;

    public ChatHelpCommand(VaylerynChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("chathelp")) {
            if (sender.hasPermission("wayward.chat.help")) {
                sender.sendMessage(plugin.getPrefix() + ChatColor.BLUE + "Chat help:");
                sender.sendMessage(ChatColor.BLUE + "/ch list: " + ChatColor.GRAY + "Lists channels");
                sender.sendMessage(ChatColor.BLUE + "/ch talkin [channel]: " + ChatColor.GRAY + "Makes you talk in the given channel");
                sender.sendMessage(ChatColor.BLUE + "/ch mute [channel]: " + ChatColor.GRAY + "Mutes a certain channel");
                sender.sendMessage(ChatColor.BLUE + "/ch listen [channel]: " + ChatColor.GRAY + "Unmutes a certain channel");
            }
            return true;
        }
        return false;
    }

}
