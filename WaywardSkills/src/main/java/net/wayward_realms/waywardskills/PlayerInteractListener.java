package net.wayward_realms.waywardskills;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.death.DeathPlugin;
import net.wayward_realms.waywardlib.skills.Skill;
import net.wayward_realms.waywardlib.skills.Spell;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

public class PlayerInteractListener implements Listener {

    private WaywardSkills plugin;

    public PlayerInteractListener(WaywardSkills plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getPlayer().getItemInHand() != null) {
                if (plugin.getSpellManager().getBoundSpell(event.getPlayer(), event.getPlayer().getItemInHand().getType()) != null) {
                    Player player = event.getPlayer();
                    Spell spell = plugin.getSpellManager().getBoundSpell(event.getPlayer(), event.getPlayer().getItemInHand().getType());
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
                                            spell.use(player);
                                            player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Used " + spell.getName() + "!");
                                            character.setMana(character.getMana() - spell.getManaCost());
                                            player.sendMessage(ChatColor.GRAY + "Mana: " + character.getMana() + "/" + character.getMaxMana());
                                            plugin.getSpellManager().setSpellCooldownTime(player, spell);
                                        } else {
                                            player.sendMessage(ChatColor.RED + "That spell is still on cooldown for another " + plugin.getSpellManager().getCooldownTime(player, spell) + " seconds.");
                                        }
                                    } else {
                                        player.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have enough mana for that spell.");
                                        player.sendMessage(plugin.getPrefix() + ChatColor.RED + "You have " + ChatColor.YELLOW + character.getMana() + "/" + character.getMaxMana() + ChatColor.RED + " mana and " + ChatColor.YELLOW + spell.getName() + ChatColor.RED + " uses " + ChatColor.YELLOW + spell.getManaCost());
                                    }
                                } else {
                                    player.sendMessage(plugin.getPrefix() + ChatColor.RED + "You cannot use that spell.");
                                }
                            } else {
                                player.sendMessage(plugin.getPrefix() + ChatColor.RED + "You cannot cast spells while unconscious.");
                            }
                        } else {
                            player.sendMessage(plugin.getPrefix() + ChatColor.RED + "No death plugin detected!");
                        }
                    } else {
                        player.sendMessage(plugin.getPrefix() + ChatColor.RED + "No character plugin detected!");
                    }
                }
                if (plugin.getSkillManager().getBoundSkill(event.getPlayer(), event.getPlayer().getItemInHand().getType()) != null) {
                    Player player = event.getPlayer();
                    Skill skill = plugin.getSkillManager().getBoundSkill(event.getPlayer(), event.getPlayer().getItemInHand().getType());
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
                                        skill.use(player);
                                        player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Used " + skill.getName() + "!");
                                        plugin.getSkillManager().setSkillCooldownTime(player, skill);
                                    } else {
                                        player.sendMessage(ChatColor.RED + "That skill is still on cooldown for another " + plugin.getSkillManager().getCooldownTime(player, skill) + " seconds.");
                                    }
                                } else {
                                    player.sendMessage(plugin.getPrefix() + ChatColor.RED + "You cannot use that skill.");
                                }
                            } else {
                                player.sendMessage(plugin.getPrefix() + ChatColor.RED + "You cannot cast skills while unconscious.");
                            }
                        } else {
                            player.sendMessage(plugin.getPrefix() + ChatColor.RED + "No death plugin detected!");
                        }
                    } else {
                        player.sendMessage(plugin.getPrefix() + ChatColor.RED + "No character plugin detected!");
                    }
                }
            }
        }
    }

}
