package net.wayward_realms.waywardevents;

import net.wayward_realms.waywardlib.character.*;
import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.events.EventCharacter;
import net.wayward_realms.waywardlib.events.EventCharacterTemplate;
import net.wayward_realms.waywardlib.skills.SkillType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EventCharacterCommand implements CommandExecutor {

    private WaywardEvents plugin;

    public EventCharacterCommand(WaywardEvents plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            if (sender.hasPermission("wayward.events.command.eventcharacter")) {
                if (args.length >= 1) {
                    if (args[0].equalsIgnoreCase("new")) {
                        characterPlugin.setActiveCharacter((Player) sender, plugin.createNewEventCharacter((Player) sender));
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Created a new event character. Make sure to set up your character information!");
                    } else if (args[0].equalsIgnoreCase("set")) {
                        if (args.length >= 2) {
                            net.wayward_realms.waywardlib.character.Character character = characterPlugin.getActiveCharacter((Player) sender);
                            if (character instanceof EventCharacter) {
                                EventCharacter eventCharacter = (EventCharacter) character;
                                if (args[1].equalsIgnoreCase("stat")) {
                                    if (args.length >= 4) {
                                        try {
                                            Stat stat = Stat.valueOf(args[2].toUpperCase());
                                            eventCharacter.setStatValue(stat, Integer.parseInt(args[3]));
                                            sender.sendMessage(ChatColor.GREEN + "Stat value set.");
                                        } catch (IllegalArgumentException exception) {
                                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " set stat [stat] [value]");
                                        }
                                    } else {
                                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " set stat [stat] [value]");
                                    }
                                } else if (args[1].equalsIgnoreCase("skillpoints") || args[1].equalsIgnoreCase("sp")) {
                                    if (args.length >= 4) {
                                        try {
                                            SkillType type = SkillType.valueOf(args[2].toUpperCase());
                                            eventCharacter.setSkillPoints(type, Integer.parseInt(args[3]));
                                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Skill points set.");
                                        } catch (IllegalArgumentException exception) {
                                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " set " + args[1] + "[skill type] [value]");
                                        }
                                    } else {
                                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " set " + args[1] + "[skill type] [value]");
                                    }
                                } else if (args[1].equalsIgnoreCase("health") || args[1].equalsIgnoreCase("hp")) {
                                    if (args.length >= 3) {
                                        try {
                                            eventCharacter.setMaxHealth(Double.parseDouble(args[2]));
                                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Health set.");
                                        } catch (NumberFormatException exception) {
                                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " set " + args[1] + " [value]");
                                        }
                                    } else {
                                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " set " + args[1] + " [value]");
                                    }
                                } else if (args[1].equalsIgnoreCase("mana")) {
                                    if (args.length >= 3) {
                                        try {
                                            eventCharacter.setMaxMana(Integer.parseInt(args[2]));
                                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Mana set.");
                                        } catch (NumberFormatException exception) {
                                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " set " + args[1] + " [value]");
                                        }
                                    } else {
                                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " set " + args[1] + " [value]");
                                    }
                                } else if (args[1].equalsIgnoreCase("race")) {
                                    if (args.length >= 3) {
                                        if (plugin.getRace(args[2]) == null) {
                                            plugin.addRace(new RaceImpl(plugin, args[2]));
                                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "New race '" + args[2] + "' created.");
                                        }
                                        Race race = plugin.getRace(args[2]);
                                        if (race != null) {
                                            character.setRace(race);
                                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Race set.");
                                        } else {
                                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Failed to create a new race.");
                                        }
                                    } else {
                                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a race.");
                                    }
                                } else {
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " set [skillpoints|stat|health|mana|race]");
                                }
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be using an event character.");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " set [skillpoints|stat|health|mana|race]");
                        }
                    } else if (args[0].equalsIgnoreCase("createtemplate")) {
                        Character character = characterPlugin.getActiveCharacter((Player) sender);
                        if (character instanceof EventCharacter) {
                            EventCharacter eventCharacter = (EventCharacter) character;
                            eventCharacter.createTemplate();
                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Template " + character.getName() + " created.");
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be using an event character.");
                        }
                    } else if (args[0].equalsIgnoreCase("assigntemplate")) {
                        if (args.length >= 2) {
                            Character character = characterPlugin.getActiveCharacter((Player) sender);
                            if (character instanceof EventCharacter) {
                                EventCharacter eventCharacter = (EventCharacter) character;
                                EventCharacterTemplate template = plugin.getEventCharacterTemplate(args[1].toLowerCase());
                                if (template != null) {
                                    eventCharacter.assignTemplate(template);
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Template " + args[1].toLowerCase() + ChatColor.GRAY + " (by " + template.getCreator().getName() + ")" + ChatColor.GREEN + " assigned.");
                                } else {
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Could not find a template by that name.");
                                }
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be using an event character.");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " createtemplate [name]");
                        }
                    } else if (args[0].equalsIgnoreCase("list")) {
                        Player player = (Player) sender;
                        if (args.length >= 2) {
                            if (plugin.getServer().getPlayer(args[1]) != null) {
                                player = plugin.getServer().getPlayer(args[1]);
                            }
                        }
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + player.getName() + "'s character list: ");
                        for (Character character : characterPlugin.getCharacters(player)) {
                            if (character != null && character instanceof EventCharacter) {
                                sender.sendMessage(ChatColor.GRAY + "[" + (character.isDead() ? ChatColor.RED : ChatColor.GREEN) + character.getId() + ChatColor.GRAY + "] " + character.getName() + " (" + (character.isDead() ? ChatColor.RED + "Dead" : ChatColor.GREEN + "Alive") + ChatColor.GRAY + ")");
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("listtemplates")) {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Templates: ");
                        for (EventCharacterTemplate template : plugin.getEventCharacterTemplates()) {
                            sender.sendMessage(ChatColor.GREEN + template.getName() + ChatColor.GRAY + " (by " + template.getCreator().getName() + ")");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " [new|set|list|createtemplate|assigntemplate|listtemplates]");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " [new|set|list|createtemplate|assigntemplate|listtemplates]");
                }
            }
        }
        return true;
    }
}
