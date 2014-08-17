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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
                                    roll((Player) sender, skillsPlugin.getAttackRoll(character, specialisation, true));
                                } else {
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Your onhand item does not meet the attack requirement for that specialisation.");
                                }
                            } else if (args[0].equalsIgnoreCase("defence") || args[0].equalsIgnoreCase("d")) {
                                if (specialisation.meetsDefenceRequirement(equipment.getOnHandItem())) {
                                    roll((Player) sender, skillsPlugin.getDefenceRoll(character, specialisation, true));
                                } else {
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Your onhand item does not meet the defence requirement for that specialisation.");
                                }
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /roll [onhand|offhand] [attack|defence] [specialisation]");
                            }
                        } else if (args[1].equalsIgnoreCase("offhand") || args[1].equalsIgnoreCase("off")) {
                            if (args[0].equalsIgnoreCase("attack") || args[0].equalsIgnoreCase("a")) {
                                if (specialisation.meetsAttackRequirement(equipment.getOffHandItem())) {
                                    roll((Player) sender, skillsPlugin.getAttackRoll(character, specialisation, false));
                                } else {
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Your offhand item does not meet the attack requirement for that specialisation.");
                                }
                            } else if (args[0].equalsIgnoreCase("defence") || args[0].equalsIgnoreCase("d")) {
                                if (specialisation.meetsDefenceRequirement(equipment.getOffHandItem())) {
                                    roll((Player) sender, skillsPlugin.getDefenceRoll(character, specialisation, false));
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
                roll((Player) sender, args[0]);
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a roll string.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
        }
        return true;
    }

    private int roll(Player roller, String rollString) {
        try {
            int amount = 1;
            int maxRoll;
            int plus = 0;

            String secondHalf;
            if (rollString.contains("d")) {
                amount = Integer.parseInt(rollString.split("d")[0]);
                secondHalf = rollString.split("d")[1];
            } else {
                secondHalf = rollString;
            }
            if (amount >= 100) {
                roller.sendMessage(plugin.getPrefix() + ChatColor.RED + "You can't roll that many times!");
                return -1;
            }
            if (rollString.contains("+")) {
                plus = Integer.parseInt(secondHalf.split("\\+")[1]);
                maxRoll = Integer.parseInt(secondHalf.split("\\+")[0]);
            } else if (rollString.contains("-")) {
                plus = -Integer.parseInt(secondHalf.split("\\-")[1]);
                maxRoll = Integer.parseInt(secondHalf.split("\\-")[0]);
            } else {
                maxRoll = Integer.parseInt(secondHalf);
            }
            if (maxRoll <= 0) {
                roller.sendMessage(plugin.getPrefix() + ChatColor.RED + "You can't roll a zero or negative number!");
                return -1;
            }
            List<Integer> rolls = new ArrayList<>();
            Random random = new Random();
            for (int i = 0; i < amount; i++) {
                rolls.add(random.nextInt(maxRoll) + 1);
            }
            String output = ChatColor.GRAY + "(";
            int rollTotal = 0;
            for (int roll : rolls) {
                output += roll;
                output += "+";
                rollTotal += roll;
            }
            rollTotal += plus;
            output += plus + ") = " + rollTotal;
            for (Player player : roller.getWorld().getPlayers()) {
                if (player.getLocation().distanceSquared(roller.getLocation()) <= 256) {
                    if (plus > 0) {
                        player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + roller.getName() + ChatColor.GRAY + "/" + ChatColor.GREEN + roller.getDisplayName() + ChatColor.GRAY + " rolled " + ChatColor.YELLOW + amount + "d" + maxRoll + "+" + plus);
                    } else if (plus < 0) {
                        player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + roller.getName() + ChatColor.GRAY + "/" + ChatColor.GREEN + roller.getDisplayName() + ChatColor.GRAY + " rolled " + ChatColor.YELLOW + amount + "d" + maxRoll + "" + plus);
                    } else if (plus == 0) {
                        player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + roller.getName() + ChatColor.GRAY + "/" + ChatColor.GREEN + roller.getDisplayName() + ChatColor.GRAY + " rolled " + ChatColor.YELLOW + amount + "d" + maxRoll);
                    }
                    player.sendMessage(plugin.getPrefix() + output);
                }
            }
            return rollTotal;
        } catch (NumberFormatException exception) {
            roller.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /roll [roll|stat]");
            roller.sendMessage(ChatColor.RED + "If rolling stats, make sure to replace spaces with underscores!");
            return -1;
        }
    }

}
