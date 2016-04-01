package net.wayward_realms.waywardskills;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BindSpellCommand implements CommandExecutor {

    private WaywardSkills plugin;

    public BindSpellCommand(WaywardSkills plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                if (plugin.getSpell(args[0]) != null) {
                    if (player.getInventory().getItemInMainHand() != null) {
                        plugin.getSpellManager().bindSpell(player, player.getInventory().getItemInMainHand().getType(), plugin.getSpell(args[0]));
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + plugin.getSpell(args[0]).getName() + " was bound to " + player.getInventory().getItemInMainHand().getType().toString().toLowerCase().replace('_', ' '));
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be holding an item to bind a spell.");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That spell does not exist.");
                }
            } else {
                plugin.getSpellManager().unbindSpell(player, player.getInventory().getItemInMainHand().getType());
                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Spell unbound from " + player.getInventory().getItemInMainHand().getType().toString().toLowerCase().replace('_', ' '));
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to bind a spell.");
        }
        return true;
    }
}
