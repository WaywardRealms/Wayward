package net.wayward_realms.waywardcharacters;

import net.wayward_realms.waywardlib.skills.Stat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {

    private WaywardCharacters plugin;

    public StatsCommand(WaywardCharacters plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            net.wayward_realms.waywardlib.character.Character character = plugin.getActiveCharacter(player);
            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + character.getName() + "'s stats");
            sender.sendMessage(ChatColor.GREEN + "Health: " + character.getHealth() + "/" + character.getMaxHealth());
            sender.sendMessage(ChatColor.GREEN + "Mana: " + character.getMana() + "/" + character.getMaxMana());
            sender.sendMessage(ChatColor.GREEN + "Thirst: " + character.getThirst() + "/" + 20);
            sender.sendMessage(ChatColor.GREEN + "Melee attack: " + character.getStatValue(Stat.MELEE_ATTACK));
            sender.sendMessage(ChatColor.GREEN + "Melee defence: " + character.getStatValue(Stat.MELEE_DEFENCE));
            sender.sendMessage(ChatColor.GREEN + "Ranged attack: " + character.getStatValue(Stat.RANGED_ATTACK));
            sender.sendMessage(ChatColor.GREEN + "Ranged defence: " + character.getStatValue(Stat.RANGED_DEFENCE));
            sender.sendMessage(ChatColor.GREEN + "Magic attack: " + character.getStatValue(Stat.MAGIC_ATTACK));
            sender.sendMessage(ChatColor.GREEN + "Magic defence: " + character.getStatValue(Stat.MAGIC_DEFENCE));
            sender.sendMessage(ChatColor.GREEN + "Speed: " + character.getStatValue(Stat.SPEED));
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
        }
        return true;
    }

}
