package net.wayward_realms.waywardchat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EmoteModeCommand implements CommandExecutor {

    private final WaywardChat plugin;

    public EmoteModeCommand(WaywardChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length >= 1) {
                try {
                    plugin.setEmoteMode((Player) sender, EmoteMode.valueOf(args[0].toUpperCase()));
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Emote mode set to " + args[0].toLowerCase().replace("_", " "));
                } catch (IllegalArgumentException exception) {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That emote mode does not exist. If you use some unorthodox form of emoting, feel free to contact someone important to get it added.");
                    sender.sendMessage(ChatColor.RED + "For now, though, you must use one of the following:");
                    for (EmoteMode emoteMode : EmoteMode.values()) {
                        sender.sendMessage(ChatColor.RED + emoteMode.toString().toLowerCase());
                    }
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify an emote mode.");
                sender.sendMessage(ChatColor.RED + "Possibilities are: ");
                for (EmoteMode emoteMode : EmoteMode.values()) {
                    sender.sendMessage(ChatColor.RED + emoteMode.toString().toLowerCase());
                }
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to set your emote mode.");
        }
        return true;
    }

}
