package net.wayward_realms.waywardskills;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.death.DeathPlugin;
import net.wayward_realms.waywardlib.skills.Spell;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class SpellCommand implements CommandExecutor {

    private WaywardSkills plugin;

    public SpellCommand(WaywardSkills plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Spell spell = plugin.getSpell(args[0]);
                if (spell != null) {
                    RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                    if (characterPluginProvider != null) {
                        CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                        Character character = characterPlugin.getActiveCharacter(player);
                        RegisteredServiceProvider<DeathPlugin> deathPluginProvider = plugin.getServer().getServicesManager().getRegistration(DeathPlugin.class);
                        if (deathPluginProvider != null) {
                            DeathPlugin deathPlugin = deathPluginProvider.getProvider();
                            if (!deathPlugin.isUnconscious(character)) {
                                if (spell.canUse(character)) {
                                    if (character.getMana() >= spell.getManaCost()) {
                                        if (plugin.getSpellManager().hasCooledDown(player, spell)) {
                                            if (spell.use(player)) {
                                                player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Used " + spell.getName() + "!");
                                                character.setMana(character.getMana() - spell.getManaCost());
                                                player.sendMessage(ChatColor.GRAY + "Mana: " + character.getMana() + "/" + character.getMaxMana());
                                                plugin.getSpellManager().setSpellCooldownTime(player, spell);
                                            }
                                        } else {
                                            player.sendMessage(ChatColor.RED + "That spell is still on cooldown for another " + plugin.getSpellManager().getCooldownTime(player, spell) + " seconds.");
                                        }
                                    } else {
                                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have enough mana for that spell.");
                                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You have " + ChatColor.YELLOW + character.getMana() + "/" + character.getMaxMana() + ChatColor.RED + " mana and " + ChatColor.YELLOW + spell.getName() + ChatColor.RED + " uses " + ChatColor.YELLOW + spell.getManaCost());
                                    }
                                } else {
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You cannot use that spell.");
                                }
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You cannot cast spells while unconscious.");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "No death plugin detected!");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "No character plugin detected!");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That spell does not exist.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to cast spells.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Spell list: ");
            Character character = null;
            if (sender instanceof Player) {
                Player player = (Player) sender;
                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                if (characterPluginProvider != null) {
                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                    character = characterPlugin.getActiveCharacter(player);
                }
            }
            if (character == null) {
                for (Spell spell : plugin.getSpells()) {
                    sender.sendMessage(ChatColor.GREEN + spell.getName());
                }
            } else {
                for (Spell spell : plugin.getSpells()) {
                    sender.sendMessage((spell.canUse(character) ? ChatColor.GREEN : ChatColor.RED) + spell.getName() + ChatColor.GRAY + " (Mana cost: " + spell.getManaCost() + ")" + (spell.getCoolDown() > 0 ? " (Cooldown: " + spell.getCoolDown() + "s)" : ""));
                }
            }
        }
        return true;
    }

}
