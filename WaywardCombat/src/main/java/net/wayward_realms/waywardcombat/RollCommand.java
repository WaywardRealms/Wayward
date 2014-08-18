package net.wayward_realms.waywardcombat;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Equipment;
import net.wayward_realms.waywardlib.skills.SkillsPlugin;
import net.wayward_realms.waywardlib.skills.Specialisation;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class RollCommand implements CommandExecutor {

    private WaywardCombat plugin;

    public RollCommand(WaywardCombat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length > 2) {
                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                RegisteredServiceProvider<SkillsPlugin> skillsPluginProvider = plugin.getServer().getServicesManager().getRegistration(SkillsPlugin.class);
                if (characterPluginProvider != null && skillsPluginProvider != null) {
                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                    SkillsPlugin skillsPlugin = skillsPluginProvider.getProvider();
                    Character character = characterPlugin.getActiveCharacter((Player) sender);
                    Equipment equipment = character.getEquipment();
                    StringBuilder builder = new StringBuilder();
                    for (int i = 2; i < args.length; i++) {
                        builder.append(args[i]).append(" ");
                    }
                    builder.deleteCharAt(builder.length() - 1);
                    Specialisation specialisation = skillsPlugin.getSpecialisation(builder.toString());
                    if (specialisation != null) {
                        if (args[1].equalsIgnoreCase("onhand") || args[1].equalsIgnoreCase("on")) {
                            if (args[0].equalsIgnoreCase("attack") || args[0].equalsIgnoreCase("a")) {
                                if (specialisation.meetsAttackRequirement(equipment.getOnHandItem())) {
                                    plugin.getRollsManager().roll((Player) sender, skillsPlugin.getAttackRoll(character, specialisation, true));
                                } else {
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Your onhand item does not meet the attack requirement for that specialisation.");
                                }
                            } else if (args[0].equalsIgnoreCase("defence") || args[0].equalsIgnoreCase("d")) {
                                if (specialisation.meetsDefenceRequirement(equipment.getOnHandItem())) {
                                    plugin.getRollsManager().roll((Player) sender, skillsPlugin.getDefenceRoll(character, specialisation, true));
                                } else {
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Your onhand item does not meet the defence requirement for that specialisation.");
                                }
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /roll [onhand|offhand] [attack|defence] [specialisation]");
                            }
                        } else if (args[1].equalsIgnoreCase("offhand") || args[1].equalsIgnoreCase("off")) {
                            if (args[0].equalsIgnoreCase("attack") || args[0].equalsIgnoreCase("a")) {
                                if (specialisation.meetsAttackRequirement(equipment.getOffHandItem())) {
                                    plugin.getRollsManager().roll((Player) sender, skillsPlugin.getAttackRoll(character, specialisation, false));
                                } else {
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Your offhand item does not meet the attack requirement for that specialisation.");
                                }
                            } else if (args[0].equalsIgnoreCase("defence") || args[0].equalsIgnoreCase("d")) {
                                if (specialisation.meetsDefenceRequirement(equipment.getOffHandItem())) {
                                    plugin.getRollsManager().roll((Player) sender, skillsPlugin.getDefenceRoll(character, specialisation, false));
                                } else {
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Your offhand item does not meet the defence requirement for that specialisation.");
                                }
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /roll [onhand|offhand] [attack|defence] [specialisation]");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That specialisation does not exist.");
                    }
                }
            } else if (args.length > 0) {
                plugin.getRollsManager().roll((Player) sender, args[0]);
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a roll string.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
        }
        return true;
    }

}
