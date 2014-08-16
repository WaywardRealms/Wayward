package net.wayward_realms.waywardcombat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RollsCommand implements CommandExecutor {

    private WaywardCombat plugin;

    public RollsCommand(WaywardCombat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (sender.hasPermission("wayward.combat.command.rolls.other")) {
            if (args.length > 0) {
                if (plugin.getServer().getPlayer(args[0]) != null) {
                    player = plugin.getServer().getPlayer(args[0]);
                }
            }
        }
        if (player != null) {
            //TODO Do something with rolls here
        }
        return true;
    }

}
