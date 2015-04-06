package net.wayward_realms.waywardcombat;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.classes.ClassesPlugin;
import net.wayward_realms.waywardlib.classes.Stat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

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
            RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
            RegisteredServiceProvider<ClassesPlugin> classesPluginProvider = plugin.getServer().getServicesManager().getRegistration(ClassesPlugin.class);
            if (characterPluginProvider != null && classesPluginProvider != null) {
                CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                Character character = characterPlugin.getActiveCharacter(player);
                sender.sendMessage(plugin.getPrefix() + ChatColor.GRAY + "==== " + ChatColor.GREEN + character.getName() + "'s rolls " + ChatColor.GRAY + " ====");
                sender.sendMessage(ChatColor.GRAY + "Melee: Attack " + ChatColor.YELLOW + plugin.getRollsManager().getRoll(character, Stat.MELEE_ATTACK) + ChatColor.GRAY + " | " + ChatColor.YELLOW + plugin.getRollsManager().getRoll(character, Stat.MELEE_DEFENCE) + ChatColor.GRAY + " Defence");
                sender.sendMessage(ChatColor.GRAY + "Ranged: Attack " + ChatColor.YELLOW + plugin.getRollsManager().getRoll(character, Stat.RANGED_ATTACK) + ChatColor.GRAY + " | " + ChatColor.YELLOW + plugin.getRollsManager().getRoll(character, Stat.RANGED_DEFENCE) + ChatColor.GRAY + " Defence");
                sender.sendMessage(ChatColor.GRAY + "Magic: Attack " + ChatColor.YELLOW + plugin.getRollsManager().getRoll(character, Stat.MAGIC_ATTACK) + ChatColor.GRAY + " | " + ChatColor.YELLOW + plugin.getRollsManager().getRoll(character, Stat.MAGIC_DEFENCE) + ChatColor.GRAY + " Defence");
                sender.sendMessage(ChatColor.GRAY + "Speed: " + ChatColor.YELLOW + plugin.getRollsManager().getRoll(character, Stat.SPEED));
            }
        }
        return true;
    }

}
