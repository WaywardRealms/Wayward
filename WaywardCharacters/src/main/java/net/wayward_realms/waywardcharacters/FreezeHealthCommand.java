package net.wayward_realms.waywardcharacters;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FreezeHealthCommand implements CommandExecutor {

    private final WaywardCharacters plugin;

    public FreezeHealthCommand(WaywardCharacters plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            plugin.setHealthFrozen((Player) sender, !plugin.isHealthFrozen((Player) sender));
            sender.sendMessage(ChatColor.GREEN + "Health regen " + (plugin.isHealthFrozen((Player) sender) ? "frozen" : "unfrozen"));
        }
        return true;
    }

}
