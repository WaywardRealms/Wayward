package net.wayward_realms.waywardcombat;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Party;
import net.wayward_realms.waywardlib.combat.Fight;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FightCommand implements CommandExecutor {

    private WaywardCombat plugin;

    public FightCommand(WaywardCombat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            CharacterPlugin characterPlugin = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class).getProvider();
            if (args[0].equalsIgnoreCase("create")) {
                Fight fight = new FightImpl();
                fight.addCombatant(characterPlugin.getActiveCharacter((Player) sender));
                plugin.getActiveFights().add(fight);
                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Fight successfully created. Use /fight start once all of the players involved have joined using /fight join [player]");
            } else if (args[0].equalsIgnoreCase("join")) {
                if (args.length >= 2) {
                    if (plugin.getServer().getPlayer(args[1]) != null) {
                        Player player = plugin.getServer().getPlayer(args[1]);
                        if (player.getLocation().distanceSquared(((Player) sender).getLocation()) <= 1024) {
                            Character character = characterPlugin.getActiveCharacter(player);
                            if (plugin.getActiveFight(character) != null) {
                                Fight fight = plugin.getActiveFight(character);
                                fight.addCombatant(characterPlugin.getActiveCharacter((Player) sender));
                                player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + sender.getName() + " has joined your fight.");
                                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Successfully joined " + player.getName() + "'s fight.");
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player is not currently in a fight. One of you needs to use /fight create and get the other to use /fight join [player]");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Move closer to that player if you want to engage in combat.");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player is not currently online.");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /fight join [player]");
                }
            } else if (args[0].equalsIgnoreCase("leave")) {
                Character character = characterPlugin.getActiveCharacter((Player) sender);
                if (plugin.getActiveFight(character) != null) {
                    plugin.getActiveFight(character).removeCombatant(character);
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Successfully left your fight");
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You are not currently in a fight.");
                }
            } else if (args[0].equalsIgnoreCase("start")) {
                Fight fight = plugin.getActiveFight(characterPlugin.getActiveCharacter((Player) sender));
                if (fight != null) {
                    if (fight.getCombatants() != null) {
                        if (fight.getCombatants().size() > 1) {
                            Character character = fight.getCharacters().iterator().next();
                            Party party = characterPlugin.getParty(character);
                            if (party != null) {
                                boolean allParty = true;
                                for (Character character1 : fight.getCharacters()) {
                                    boolean containsCharacter = false;
                                    for (Character character2 : party.getMembers()) {
                                        if (character1.getId() == character2.getId()) {
                                            containsCharacter = true;
                                            break;
                                        }
                                    }
                                    if (!containsCharacter) {
                                        allParty = false;
                                        break;
                                    }
                                }
                                if (allParty) {
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must get some players that are not in your party to join before you can start the fight!");
                                    return true;
                                }
                            }
                            fight.start();
                            fight.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "The fight has begun!");
                            fight.sendMessage(ChatColor.YELLOW + "It's " + fight.getNextTurn().getName() + "'s turn.");
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You cannot fight with yourself! Please get any others to join the fight first.");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You are not currently in a fight.");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You are not currently in a fight.");
                }
            } else if (args[0].equalsIgnoreCase("end")) {
                Fight fight = plugin.getActiveFight(characterPlugin.getActiveCharacter((Player) sender));
                if (fight != null) {
                    if (fight.getCharacters() != null) {
                        fight.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "The fight has ended!");
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You are not currently in a fight.");
                    }
                    fight.end();
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You are not part of a fight, so you may not end it.");
                }
            } else {
                sender.sendMessage(new String[] {plugin.getPrefix() + ChatColor.RED + "You must specify an argument.", ChatColor.RED + "If you are trying to create a fight, use /fight create", ChatColor.RED + "If you are trying to join someone else's fight, use /fight join [player]", ChatColor.RED + "If you are trying to end a fight, use /fight end"});
            }
        }
        return true;
    }

}
