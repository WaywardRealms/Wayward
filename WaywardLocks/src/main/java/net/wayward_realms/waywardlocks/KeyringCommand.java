package net.wayward_realms.waywardlocks;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KeyringCommand implements CommandExecutor {

    private WaywardLocks plugin;

    public KeyringCommand(WaywardLocks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            plugin.getKeyringManager().showKeyring(player);
        }
        return true;
    }

}
