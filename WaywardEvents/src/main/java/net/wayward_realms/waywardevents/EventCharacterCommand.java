package net.wayward_realms.waywardevents;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.events.EventCharacter;
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

    private CharacterPlugin characterPlugin;

    public EventCharacterCommand(WaywardEvents plugin) {
        this.plugin = plugin;
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            this.characterPlugin = characterPluginProvider.getProvider();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.events.command.eventcharacter")) {
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("new")) {
                    characterPlugin.setActiveCharacter((Player) sender, plugin.createNewEventCharacter((Player) sender));
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Created a new event character. Make sure to set up your character information!");
                } else if (args[0].equalsIgnoreCase("set")) {
                    if (args.length >= 2) {
                        Character character = characterPlugin.getActiveCharacter((Player) sender);
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
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " set [skillpoints|stat] [skill type|stat] [value]");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be using an event character in order to set skill points or stats.");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " set [skillpoints|stat] [skill type|stat] [value]");
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
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " [new|set|list]");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " [new|set|list]");
            }
        }
        return true;
    }
}
