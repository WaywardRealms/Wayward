package net.wayward_realms.waywardcombat;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.skills.SkillsPlugin;
import net.wayward_realms.waywardlib.skills.Specialisation;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

public class InventoryClickListener implements Listener {

    private WaywardCombat plugin;

    public InventoryClickListener(WaywardCombat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRollInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equalsIgnoreCase("Hand")) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null
                    && event.getCurrentItem().hasItemMeta()
                    && event.getCurrentItem().getItemMeta().hasDisplayName()
                    && event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Onhand")) {
                RollContext context = plugin.getRollContext((Player) event.getWhoClicked());
                context.setOnHand(true);
                plugin.setRollContext((Player) event.getWhoClicked(), context);
                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                RegisteredServiceProvider<SkillsPlugin> skillsPluginProvider = plugin.getServer().getServicesManager().getRegistration(SkillsPlugin.class);
                if (characterPluginProvider != null && skillsPluginProvider != null) {
                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                    SkillsPlugin skillsPlugin = skillsPluginProvider.getProvider();
                    event.getWhoClicked().closeInventory();
                    event.getWhoClicked().openInventory(plugin.getSpecialisationInventory("Specialisation", skillsPlugin.getRootSpecialisation(), characterPlugin.getActiveCharacter((Player) event.getWhoClicked())));
                }
            } else if (event.getCurrentItem() != null
                    && event.getCurrentItem().hasItemMeta()
                    && event.getCurrentItem().getItemMeta().hasDisplayName()
                    && event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Offhand")) {
                RollContext context = plugin.getRollContext((Player) event.getWhoClicked());
                context.setOnHand(false);
                plugin.setRollContext((Player) event.getWhoClicked(), context);
                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                RegisteredServiceProvider<SkillsPlugin> skillsPluginProvider = plugin.getServer().getServicesManager().getRegistration(SkillsPlugin.class);
                if (characterPluginProvider != null && skillsPluginProvider != null) {
                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                    SkillsPlugin skillsPlugin = skillsPluginProvider.getProvider();
                    event.getWhoClicked().closeInventory();
                    event.getWhoClicked().openInventory(plugin.getSpecialisationInventory("Specialisation", skillsPlugin.getRootSpecialisation(), characterPlugin.getActiveCharacter((Player) event.getWhoClicked())));
                }
            }
        } else if (event.getInventory().getTitle().equalsIgnoreCase("Specialisation")) {
            RegisteredServiceProvider<SkillsPlugin> skillsPluginProvider = plugin.getServer().getServicesManager().getRegistration(SkillsPlugin.class);
            if (skillsPluginProvider != null) {
                SkillsPlugin skillsPlugin = skillsPluginProvider.getProvider();
                event.setCancelled(true);
                if (event.getCurrentItem().getType() == Material.WOOL) {
                    if (event.getCurrentItem().getDurability() == (short) 5) {
                        Specialisation specialisation = skillsPlugin.getSpecialisation(event.getCurrentItem().getItemMeta().getDisplayName());
                        RollContext context = plugin.getRollContext((Player) event.getWhoClicked());
                        context.setSpecialisation(specialisation);
                        plugin.roll((Player) event.getWhoClicked(), context.getRoll());
                        plugin.setRollContext((Player) event.getWhoClicked(), null);
                        event.getWhoClicked().closeInventory();
                    } else {
                        Specialisation specialisation = skillsPlugin.getSpecialisation(event.getCurrentItem().getItemMeta().getDisplayName());
                        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                        if (characterPluginProvider != null) {
                            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                            Character character = characterPlugin.getActiveCharacter((Player) event.getWhoClicked());
                            event.getWhoClicked().closeInventory();
                            event.getWhoClicked().openInventory(plugin.getSpecialisationInventory("Specialisation", specialisation, character));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamageInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equalsIgnoreCase("Hand [d]")) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null
                    && event.getCurrentItem().hasItemMeta()
                    && event.getCurrentItem().getItemMeta().hasDisplayName()
                    && event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Onhand")) {
                DamageContext context = plugin.getDamageContext((Player) event.getWhoClicked());
                context.setOnHand(true);
                plugin.setDamageContext((Player) event.getWhoClicked(), context);
                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                RegisteredServiceProvider<SkillsPlugin> skillsPluginProvider = plugin.getServer().getServicesManager().getRegistration(SkillsPlugin.class);
                if (characterPluginProvider != null && skillsPluginProvider != null) {
                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                    SkillsPlugin skillsPlugin = skillsPluginProvider.getProvider();
                    event.getWhoClicked().closeInventory();
                    event.getWhoClicked().openInventory(plugin.getSpecialisationInventory("Specialisation [d]", skillsPlugin.getRootSpecialisation(), characterPlugin.getActiveCharacter((Player) event.getWhoClicked())));
                }
            } else if (event.getCurrentItem() != null
                    && event.getCurrentItem().hasItemMeta()
                    && event.getCurrentItem().getItemMeta().hasDisplayName()
                    && event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Offhand")) {
                DamageContext context = plugin.getDamageContext((Player) event.getWhoClicked());
                context.setOnHand(false);
                plugin.setDamageContext((Player) event.getWhoClicked(), context);
                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                RegisteredServiceProvider<SkillsPlugin> skillsPluginProvider = plugin.getServer().getServicesManager().getRegistration(SkillsPlugin.class);
                if (characterPluginProvider != null && skillsPluginProvider != null) {
                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                    SkillsPlugin skillsPlugin = skillsPluginProvider.getProvider();
                    event.getWhoClicked().closeInventory();
                    event.getWhoClicked().openInventory(plugin.getSpecialisationInventory("Specialisation [d]", skillsPlugin.getRootSpecialisation(), characterPlugin.getActiveCharacter((Player) event.getWhoClicked())));
                }
            }
        } else if (event.getInventory().getTitle().equalsIgnoreCase("Specialisation [d]")) {
            RegisteredServiceProvider<SkillsPlugin> skillsPluginProvider = plugin.getServer().getServicesManager().getRegistration(SkillsPlugin.class);
            if (skillsPluginProvider != null) {
                SkillsPlugin skillsPlugin = skillsPluginProvider.getProvider();
                event.setCancelled(true);
                if (event.getCurrentItem().getType() == Material.WOOL) {
                    if (event.getCurrentItem().getDurability() == (short) 5) {
                        Specialisation specialisation = skillsPlugin.getSpecialisation(event.getCurrentItem().getItemMeta().getDisplayName());
                        DamageContext context = plugin.getDamageContext((Player) event.getWhoClicked());
                        context.setSpecialisation(specialisation);
                        int damage = plugin.roll((Player) event.getWhoClicked(), context.getRoll());
                        context.getDefending().getPlayer().getPlayer().damage(damage);
                        context.getDefending().setHealth(context.getDefending().getPlayer().getPlayer().getHealth());
                        for (Player player : event.getWhoClicked().getWorld().getPlayers()) {
                            if (player.getLocation().distanceSquared(event.getWhoClicked().getLocation()) <= 64) {
                                player.sendMessage(ChatColor.YELLOW + ((Player) event.getWhoClicked()).getDisplayName() + " dealt " + damage + " damage to " + (context.getDefending().isNameHidden() ? ChatColor.MAGIC : "") + context.getDefending().getName());
                            }
                        }
                        plugin.setDamageContext((Player) event.getWhoClicked(), null);
                        event.getWhoClicked().closeInventory();
                    } else {
                        Specialisation specialisation = skillsPlugin.getSpecialisation(event.getCurrentItem().getItemMeta().getDisplayName());
                        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                        if (characterPluginProvider != null) {
                            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                            Character character = characterPlugin.getActiveCharacter((Player) event.getWhoClicked());
                            event.getWhoClicked().closeInventory();
                            event.getWhoClicked().openInventory(plugin.getSpecialisationInventory("Specialisation [d]", specialisation, character));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamageCalculationInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equalsIgnoreCase("Hand [dc]")) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null
                    && event.getCurrentItem().hasItemMeta()
                    && event.getCurrentItem().getItemMeta().hasDisplayName()
                    && event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Onhand")) {
                DamageCalculationContext context = plugin.getDamageCalculationContext((Player) event.getWhoClicked());
                context.setOnHand(true);
                plugin.setDamageCalculationContext((Player) event.getWhoClicked(), context);
                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                RegisteredServiceProvider<SkillsPlugin> skillsPluginProvider = plugin.getServer().getServicesManager().getRegistration(SkillsPlugin.class);
                if (characterPluginProvider != null && skillsPluginProvider != null) {
                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                    SkillsPlugin skillsPlugin = skillsPluginProvider.getProvider();
                    event.getWhoClicked().closeInventory();
                    event.getWhoClicked().openInventory(plugin.getSpecialisationInventory("Specialisation [dc]", skillsPlugin.getRootSpecialisation(), characterPlugin.getActiveCharacter((Player) event.getWhoClicked())));
                }
            } else if (event.getCurrentItem() != null
                    && event.getCurrentItem().hasItemMeta()
                    && event.getCurrentItem().getItemMeta().hasDisplayName()
                    && event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Offhand")) {
                DamageCalculationContext context = plugin.getDamageCalculationContext((Player) event.getWhoClicked());
                context.setOnHand(false);
                plugin.setDamageCalculationContext((Player) event.getWhoClicked(), context);
                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                RegisteredServiceProvider<SkillsPlugin> skillsPluginProvider = plugin.getServer().getServicesManager().getRegistration(SkillsPlugin.class);
                if (characterPluginProvider != null && skillsPluginProvider != null) {
                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                    SkillsPlugin skillsPlugin = skillsPluginProvider.getProvider();
                    event.getWhoClicked().closeInventory();
                    event.getWhoClicked().openInventory(plugin.getSpecialisationInventory("Specialisation [dc]", skillsPlugin.getRootSpecialisation(), characterPlugin.getActiveCharacter((Player) event.getWhoClicked())));
                }
            }
        } else if (event.getInventory().getTitle().equalsIgnoreCase("Specialisation [dc]")) {
            RegisteredServiceProvider<SkillsPlugin> skillsPluginProvider = plugin.getServer().getServicesManager().getRegistration(SkillsPlugin.class);
            if (skillsPluginProvider != null) {
                SkillsPlugin skillsPlugin = skillsPluginProvider.getProvider();
                event.setCancelled(true);
                if (event.getCurrentItem().getType() == Material.WOOL) {
                    if (event.getCurrentItem().getDurability() == (short) 5) {
                        Specialisation specialisation = skillsPlugin.getSpecialisation(event.getCurrentItem().getItemMeta().getDisplayName());
                        DamageCalculationContext context = plugin.getDamageCalculationContext((Player) event.getWhoClicked());
                        context.setSpecialisation(specialisation);
                        int damage = plugin.roll((Player) event.getWhoClicked(), context.getRoll());
                        for (Player player : event.getWhoClicked().getWorld().getPlayers()) {
                            if (player.getLocation().distanceSquared(event.getWhoClicked().getLocation()) <= 64) {
                                player.sendMessage(ChatColor.YELLOW + ((Player) event.getWhoClicked()).getDisplayName() + " dealt " + damage + " damage");
                            }
                        }
                        plugin.setDamageCalculationContext((Player) event.getWhoClicked(), null);
                        event.getWhoClicked().closeInventory();
                    } else {
                        Specialisation specialisation = skillsPlugin.getSpecialisation(event.getCurrentItem().getItemMeta().getDisplayName());
                        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                        if (characterPluginProvider != null) {
                            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                            Character character = characterPlugin.getActiveCharacter((Player) event.getWhoClicked());
                            event.getWhoClicked().closeInventory();
                            event.getWhoClicked().openInventory(plugin.getSpecialisationInventory("Specialisation [dc]", specialisation, character));
                        }
                    }
                }
            }
        }
    }

//    @EventHandler
//    public void onInventoryClick(InventoryClickEvent event) {
//        CharacterPlugin characterPlugin = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class).getProvider();
//        if (event.getInventory().getTitle().equals("Skill type")) {
//            event.setCancelled(true);
//            if (event.getSlot() >= 0 && event.getSlot() <= 12) {
//                Set<Skill> skills = new HashSet<>();
//                RegisteredServiceProvider<SkillsPlugin> skillsPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(SkillsPlugin.class);
//                if (skillsPluginProvider != null) {
//                    SkillsPlugin skillsPlugin = skillsPluginProvider.getProvider();
//                    skills.addAll(skillsPlugin.getSkills());
//                    skills.addAll(skillsPlugin.getSpells());
//                }
//                Set<Skill> skillsToRemove = new HashSet<>();
//                for (Skill skill : skills) {
//                    if (!skill.canUse((Player) event.getWhoClicked())) {
//                        skillsToRemove.add(skill);
//                    }
//                }
//                skills.removeAll(skillsToRemove);
//                event.getWhoClicked().closeInventory();
//                FightImpl fight = (FightImpl) plugin.getActiveFight(characterPlugin.getActiveCharacter((Player) event.getWhoClicked()));
//                fight.showSkillOptions((Player) event.getWhoClicked(), skills);
//            }
//        } else if (event.getInventory().getTitle().equals("Skill")) {
//            event.setCancelled(true);
//            RegisteredServiceProvider<SkillsPlugin> skillsPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(SkillsPlugin.class);
//            if (skillsPluginProvider != null) {
//                SkillsPlugin skillsPlugin = skillsPluginProvider.getProvider();
//                for (Skill skill : skillsPlugin.getSkills()) {
//                    if (skill.getIcon().equals(event.getCurrentItem())) {
//                        Character attacking = characterPlugin.getActiveCharacter((Player) event.getWhoClicked());
//                        FightImpl fight = (FightImpl) plugin.getActiveFight(attacking);
//                        Turn turn = fight.getActiveTurn();
//                        turn.setSkill(skill);
//                        event.getWhoClicked().closeInventory();
//                        ((Player) event.getWhoClicked()).sendMessage(new String[] {plugin.getPrefix() + ChatColor.GREEN + "Current turn:",
//                                (turn.getSkill() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Skill: " + (turn.getSkill() != null ? ChatColor.GREEN + turn.getSkill().getName() : ChatColor.RED + "NOT CHOSEN - use /turn skill to choose"),
//                                (turn.getDefender() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Target: " + (turn.getDefender() != null ? ChatColor.GREEN + (((Character) turn.getDefender()).isNameHidden() ? ChatColor.MAGIC + turn.getDefender().getName() + ChatColor.RESET : turn.getDefender().getName()) + ChatColor.GREEN + " (" + ((Character) turn.getDefender()).getPlayer().getName() + "'s character)" : ChatColor.RED + "NOT CHOSEN - use /turn target to choose"),
//                                (turn.getWeapon() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Weapon: " + (turn.getWeapon() != null ? ChatColor.GREEN + turn.getWeapon().getType().toString() : ChatColor.RED + "NOT CHOSEN - use /turn weapon to choose"),
//                                (turn.getSkill() != null && turn.getDefender() != null && turn.getWeapon() != null ? ChatColor.GREEN + "Ready to make a move! Use /turn complete to complete your turn." : ChatColor.RED + "There are still some options you must set before completing your turn.")});
//                        break;
//                    }
//                }
//                for (Spell spell : skillsPlugin.getSpells()) {
//                    if (spell.getIcon().equals(event.getCurrentItem())) {
//                        Character attacking = characterPlugin.getActiveCharacter((Player) event.getWhoClicked());
//                        FightImpl fight = (FightImpl) plugin.getActiveFight(attacking);
//                        Turn turn = fight.getActiveTurn();
//                        turn.setSkill(spell);
//                        event.getWhoClicked().closeInventory();
//                        ((Player) event.getWhoClicked()).sendMessage(new String[] {plugin.getPrefix() + ChatColor.GREEN + "Current turn:",
//                                (turn.getSkill() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Skill: " + (turn.getSkill() != null ? ChatColor.GREEN + turn.getSkill().getName() : ChatColor.RED + "NOT CHOSEN - use /turn skill to choose"),
//                                (turn.getDefender() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Target: " + (turn.getDefender() != null ? ChatColor.GREEN + (((Character) turn.getDefender()).isNameHidden() ? ChatColor.MAGIC + turn.getDefender().getName() + ChatColor.RESET : turn.getDefender().getName()) + ChatColor.GREEN + " (" + ((Character) turn.getDefender()).getPlayer().getName() + "'s character)" : ChatColor.RED + "NOT CHOSEN - use /turn target to choose"),
//                                (turn.getWeapon() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Weapon: " + (turn.getWeapon() != null ? ChatColor.GREEN + turn.getWeapon().getType().toString() : ChatColor.RED + "NOT CHOSEN - use /turn weapon to choose"),
//                                (turn.getSkill() != null && turn.getDefender() != null && turn.getWeapon() != null ? ChatColor.GREEN + "Ready to make a move! Use /turn complete to complete your turn." : ChatColor.RED + "There are still some options you must set before completing your turn.")});
//                        break;
//                    }
//                }
//            }
//
//        } else if (event.getInventory().getTitle().equals("Target")) {
//            event.setCancelled(true);
//            Character character = characterPlugin.getActiveCharacter((Player) event.getWhoClicked());
//            FightImpl fight = (FightImpl) plugin.getActiveFight(character);
//            Turn turn = fight.getActiveTurn();
//            turn.setDefender(characterPlugin.getCharacter(Integer.parseInt(event.getCurrentItem().getItemMeta().getLore().get(0))));
//            event.getWhoClicked().closeInventory();
//            ((Player) event.getWhoClicked()).sendMessage(new String[] {plugin.getPrefix() + ChatColor.GREEN + "Current turn:",
//                    (turn.getSkill() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Skill: " + (turn.getSkill() != null ? ChatColor.GREEN + turn.getSkill().getName() : ChatColor.RED + "NOT CHOSEN - use /turn skill to choose"),
//                    (turn.getDefender() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Target: " + (turn.getDefender() != null ? ChatColor.GREEN + (((Character) turn.getDefender()).isNameHidden() ? ChatColor.MAGIC + turn.getDefender().getName() + ChatColor.RESET : turn.getDefender().getName()) + ChatColor.GREEN + " (" + ((Character) turn.getDefender()).getPlayer().getName() + "'s character)" : ChatColor.RED + "NOT CHOSEN - use /turn target to choose"),
//                    (turn.getWeapon() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Weapon: " + (turn.getWeapon() != null ? ChatColor.GREEN + turn.getWeapon().getType().toString() : ChatColor.RED + "NOT CHOSEN - use /turn weapon to choose"),
//                    (turn.getSkill() != null && turn.getDefender() != null && turn.getWeapon() != null ? ChatColor.GREEN + "Ready to make a move! Use /turn complete to complete your turn." : ChatColor.RED + "There are still some options you must set before completing your turn.")});
//        } else if (event.getInventory().getTitle().equals("Weapon")) {
//            event.setCancelled(true);
//            Character character = characterPlugin.getActiveCharacter((Player) event.getWhoClicked());
//            FightImpl fight = (FightImpl) plugin.getActiveFight(character);
//            Turn turn = fight.getActiveTurn();
//            turn.setWeapon(event.getCurrentItem());
//            event.getWhoClicked().closeInventory();
//            ((Player) event.getWhoClicked()).sendMessage(new String[] {plugin.getPrefix() + ChatColor.GREEN + "Current turn:",
//                    (turn.getSkill() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Skill: " + (turn.getSkill() != null ? ChatColor.GREEN + turn.getSkill().getName() : ChatColor.RED + "NOT CHOSEN - use /turn skill to choose"),
//                    (turn.getDefender() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Target: " + (turn.getDefender() != null ? ChatColor.GREEN + (((Character) turn.getDefender()).isNameHidden() ? ChatColor.MAGIC + turn.getDefender().getName() + ChatColor.RESET : turn.getDefender().getName()) + ChatColor.GREEN + " (" + ((Character) turn.getDefender()).getPlayer().getName() + "'s character)" : ChatColor.RED + "NOT CHOSEN - use /turn target to choose"),
//                    (turn.getWeapon() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Weapon: " + (turn.getWeapon() != null ? ChatColor.GREEN + turn.getWeapon().getType().toString() : ChatColor.RED + "NOT CHOSEN - use /turn weapon to choose"),
//                    (turn.getSkill() != null && turn.getDefender() != null && turn.getWeapon() != null ? ChatColor.GREEN + "Ready to make a move! Use /turn complete to complete your turn." : ChatColor.RED + "There are still some options you must set before completing your turn.")});
//        }
//    }

}
