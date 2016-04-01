package net.wayward_realms.waywardskills;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BindSkillCommand implements CommandExecutor {

    private WaywardSkills plugin;

    public BindSkillCommand(WaywardSkills plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                if (plugin.getSkill(args[0]) != null) {
                    if (player.getInventory().getItemInMainHand() != null) {
                        plugin.getSkillManager().bindSkill(player, player.getInventory().getItemInMainHand().getType(), plugin.getSkill(args[0]));
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + plugin.getSkill(args[0]).getName() + " was bound to " + player.getInventory().getItemInMainHand().getType().toString().toLowerCase().replace('_', ' '));
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be holding an item to bind a skill.");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That skill does not exist.");
                }
            } else {
                plugin.getSkillManager().unbindSkill(player, player.getInventory().getItemInMainHand().getType());
                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Skill unbound from " + player.getInventory().getItemInMainHand().getType().toString().toLowerCase().replace('_', ' '));
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to bind a skill.");
        }
        return true;
    }
    
}
