package net.wayward_realms.waywardcombat;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.skills.SkillsPlugin;
import net.wayward_realms.waywardlib.skills.Specialisation;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class DamageCommand implements CommandExecutor {

    private WaywardCombat plugin;

    public DamageCommand(WaywardCombat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        RegisteredServiceProvider<SkillsPlugin> skillsPluginProvider = plugin.getServer().getServicesManager().getRegistration(SkillsPlugin.class);
        if (characterPluginProvider != null && skillsPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            SkillsPlugin skillsPlugin = skillsPluginProvider.getProvider();
            if (args.length > 2) {
                Player defendingPlayer = plugin.getServer().getPlayer(args[0]);
                StringBuilder builder = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    builder.append(args[i]).append(" ");
                }
                Specialisation specialisation = skillsPlugin.getSpecialisation(builder.toString());
                if (defendingPlayer != null) {
                    if (specialisation != null) {
                        if (args[2].equalsIgnoreCase("onhand") || args[1].equalsIgnoreCase("on")) {
                            String damageRollString = skillsPlugin.getDamageRoll(characterPlugin.getActiveCharacter((Player) sender), specialisation, true, characterPlugin.getActiveCharacter(defendingPlayer));
                            int damageRoll = plugin.getRollsManager().roll((Player) sender, damageRollString);
                            defendingPlayer.damage(damageRoll);
                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Dealt " + damageRoll + " damage");
                        } else if (args[2].equalsIgnoreCase("offhand") || args[1].equalsIgnoreCase("off")) {
                            String damageRollString = skillsPlugin.getDamageRoll(characterPlugin.getActiveCharacter((Player) sender), specialisation, false, characterPlugin.getActiveCharacter(defendingPlayer));
                            int damageRoll = plugin.getRollsManager().roll((Player) sender, damageRollString);
                            defendingPlayer.damage(damageRoll);
                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Dealt " + damageRoll + " damage");
                        }
                    }
                }
            }
        }
        return true;
    }

}
