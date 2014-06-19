package net.wayward_realms.waywardcharacters;

import net.wayward_realms.waywardlib.character.Party;
import net.wayward_realms.waywardlib.character.Character;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyCommand implements CommandExecutor {

    private WaywardCharacters plugin;

    public PartyCommand(WaywardCharacters plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("invite")) {
                    if (args.length > 1) {
                        Player player = plugin.getServer().getPlayer(args[1]);
                        if (player != null) {
                            Party party = plugin.getParty(plugin.getActiveCharacter((Player) sender));
                            if (party == null) party = new PartyImpl(plugin, plugin.getActiveCharacter((Player) sender));
                            party.invite(plugin.getActiveCharacter(player));
                            player.sendMessage(new String[] {
                                    plugin.getPrefix() + ChatColor.GREEN + "You have been invited to " + sender.getName() + "/" + ((Player) sender).getDisplayName() + "'s party.",
                                    ChatColor.GREEN + "Use /party join " + sender.getName() + " to accept."
                            });
                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Invited " + player.getName() + "/" + player.getDisplayName() + " to your party.");
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player is not online.");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a player to invite.");
                    }
                } else if (args[0].equalsIgnoreCase("join")) {
                    if (args.length > 1) {
                        if (plugin.getParty(plugin.getActiveCharacter((Player) sender)) == null) {
                            Player player = plugin.getServer().getPlayer(args[1]);
                            if (player != null) {
                                Party party = plugin.getParty(plugin.getActiveCharacter(player));
                                if (party != null) {
                                    int senderCharId = plugin.getActiveCharacter((Player) sender).getId();
                                    boolean found = false;
                                    for (Character character : party.getInvitees()) {
                                        if (character.getId() == senderCharId) {
                                            party.addMember(character);
                                            found = true;
                                            break;
                                        }
                                    }
                                    if (found) {
                                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Joined " + player.getName() + "/" + player.getDisplayName() + "'s party.");
                                    } else {
                                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You have not been invited to that player's party.");
                                    }
                                } else {
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player does not have a party.");
                                }
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That player is not online.");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You are already in a party.");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a player.");
                    }
                } else if (args[0].equalsIgnoreCase("members")) {
                    Party party = plugin.getParty(plugin.getActiveCharacter((Player) sender));
                    if (party != null) {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Members:");
                        for (Character character : party.getMembers()) {
                            sender.sendMessage(ChatColor.GRAY + character.getPlayer().getName() + "/" + character.getName());
                        }
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Invited:");
                        for (Character character : party.getInvitees()) {
                            sender.sendMessage(ChatColor.GRAY + character.getPlayer().getName() + "/" + character.getName());
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You are not currently in a party, use /party invite [player] to invite people to your party.");
                    }
                } else if (args[0].equalsIgnoreCase("leave")) {
                    Character character = plugin.getActiveCharacter((Player) sender);
                    Party party = plugin.getParty(character);
                    if (party != null) {
                        party.removeMember(character);
                        party.sendMessage(plugin.getPrefix() + ChatColor.RED + sender.getName() + "/" + ((Player) sender).getDisplayName() + " left the party.");
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Left your party.");
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You are not in a party.");
                    }
                } else if (args[0].equalsIgnoreCase("disband")) {
                    Party party = plugin.getParty(plugin.getActiveCharacter((Player) sender));
                    if (party != null) {
                        party.sendMessage(plugin.getPrefix() + ChatColor.RED + "Party disbanded.");
                        plugin.removeParty(party);
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " [join|leave|invite|members|disband]");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + "[join|leave|invite|members|disband]");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
        }
        return true;
    }

}
