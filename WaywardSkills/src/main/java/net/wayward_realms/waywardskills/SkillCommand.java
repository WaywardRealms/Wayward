package net.wayward_realms.waywardskills;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.death.DeathPlugin;
import net.wayward_realms.waywardlib.skills.Skill;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class SkillCommand implements CommandExecutor {

    private WaywardSkills plugin;

    public SkillCommand(WaywardSkills plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Skill skill = plugin.getSkill(args[0]);
                if (skill != null) {
                    RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                    if (characterPluginProvider != null) {
                        CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                        Character character = characterPlugin.getActiveCharacter(player);
                        RegisteredServiceProvider<DeathPlugin> deathPluginProvider = plugin.getServer().getServicesManager().getRegistration(DeathPlugin.class);
                        if (deathPluginProvider != null) {
                            DeathPlugin deathPlugin = deathPluginProvider.getProvider();
                            if (!deathPlugin.isUnconscious(character)) {
                                if (skill.canUse(character)) {
                                    if (plugin.getSkillManager().hasCooledDown(player, skill)) {
                                        if (skill.use(player)) {
                                            player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Used " + skill.getName() + "!");
                                            plugin.getSkillManager().setSkillCooldownTime(player, skill);
                                        }
                                    } else {
                                        player.sendMessage(ChatColor.RED + "That skill is still on cooldown for another " + plugin.getSkillManager().getCooldownTime(player, skill) + " seconds.");
                                    }
                                } else {
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You cannot use that skill.");
                                }
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You cannot cast skills while unconscious.");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "No death plugin detected!");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "No character plugin detected!");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That skill does not exist.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to cast skills.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Skill list: ");
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
                for (Skill skill : plugin.getSkills()) {
                    sender.sendMessage(ChatColor.GREEN + skill.getName());
                }
            } else {
                for (Skill skill : plugin.getSkills()) {
                    sender.sendMessage((skill.canUse(character) ? ChatColor.GREEN : ChatColor.RED) + skill.getName() + ChatColor.GRAY + (skill.getCoolDown() > 0 ? " (Cooldown: " + skill.getCoolDown() + "s)" : ""));
                }
            }
        }
        return true;
    }

}
