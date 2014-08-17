package net.wayward_realms.waywardskills;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class LevelCommand implements CommandExecutor {

    private WaywardSkills plugin;

    public LevelCommand(WaywardSkills plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (sender.hasPermission("wayward.skills.command.level")) {
            if (args.length >= 2) {
                if (plugin.getServer().getPlayer(args[1]) != null) {
                    player = plugin.getServer().getPlayer(args[1]);
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player is not online!");
                    return true;
                }
            }
        }
        if (player != null) {
            RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
            if (characterPluginProvider != null) {
                CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                Character character = characterPlugin.getActiveCharacter(player);
                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + player.getDisplayName() + ChatColor.GREEN + " is currently lv" + plugin.getLevel(character));
                sender.sendMessage(ChatColor.GRAY + " (Progress towards level " + (plugin.getLevel(character) + 1) + ": " + plugin.getExperience(character) + "/" + plugin.getExperienceForLevel(plugin.getLevel(character) + 1) + ")");
            }
        }
        return true;
    }

}
