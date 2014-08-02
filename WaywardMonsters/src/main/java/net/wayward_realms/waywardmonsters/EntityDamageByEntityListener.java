package net.wayward_realms.waywardmonsters;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Party;
import net.wayward_realms.waywardlib.classes.Stat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EntityDamageByEntityListener implements Listener {

    private WaywardMonsters plugin;

    public EntityDamageByEntityListener(WaywardMonsters plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        LivingEntity attacker = null;
        LivingEntity defender = null;
        Stat attackStat = null;
        Stat defenceStat = null;
        int attack = 0;
        int defence = 0;
        if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if (projectile.getShooter() instanceof LivingEntity) {
                attacker = (LivingEntity) projectile.getShooter();
                if (projectile instanceof Arrow) {
                    attackStat = Stat.RANGED_ATTACK;
                    defenceStat = Stat.RANGED_DEFENCE;
                } else {
                    attackStat = Stat.MAGIC_ATTACK;
                    defenceStat = Stat.MAGIC_DEFENCE;
                }
            }
        } else if (event.getDamager() instanceof LivingEntity) {
            attacker = (LivingEntity) event.getDamager();
            attackStat = Stat.MELEE_ATTACK;
            defenceStat = Stat.MELEE_DEFENCE;
        } else {
            attackStat = Stat.MAGIC_ATTACK;
            defenceStat = Stat.MAGIC_DEFENCE;
        }
        if (event.getEntity() instanceof LivingEntity) {
            defender = (LivingEntity) event.getEntity();
        }
        if (attacker != null) {
            if (attacker instanceof Player) {
                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                if (characterPluginProvider != null) {
                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                    attack = characterPlugin.getActiveCharacter((Player) attacker).getStatValue(attackStat);
                }
            } else {
                attack = plugin.getEntityLevelManager().getEntityStatValue(attacker);
            }
        }
        if (defender != null) {
            if (defender instanceof Player) {
                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                if (characterPluginProvider != null) {
                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                    defence = characterPlugin.getActiveCharacter((Player) defender).getStatValue(defenceStat);
                }
            } else {
                defence = plugin.getEntityLevelManager().getEntityStatValue(defender);
            }
        }
        if (attacker != null && defender != null && attacker instanceof Player && defender instanceof Player) {
            RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
            if (characterPluginProvider != null) {
                CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                Character attackingCharacter = characterPlugin.getActiveCharacter((Player) attacker);
                Character defendingCharacter = characterPlugin.getActiveCharacter((Player) defender);
                Party party = characterPlugin.getParty(attackingCharacter);
                if (party != null) {
                    for (Character character : party.getMembers()) {
                        if (character.getId() == defendingCharacter.getId()) {
                            event.setCancelled(true);
                            ((Player) attacker).sendMessage(ChatColor.RED + "You cannot attack members of your party!");
                            return;
                        }
                    }
                }
            }
        }
        if (attacker != null && defender != null && (defender instanceof Monster || defender instanceof Player)) {
            event.setDamage((Math.ceil((((attack + 1D) / 2D) + 5D) * event.getDamage())) / (defence + 1D) / 2D);
        }
        if (defender != null) {
            if (defender instanceof Monster) {
                defender.setCustomName("Lv" + plugin.getEntityLevelManager().getEntityLevel(defender) + " " + defender.getType().toString().toLowerCase().replace('_', ' ') + " - " + Math.round(defender.getHealth() * 100D) / 100D + "HP");
                defender.setCustomNameVisible(true);
            }
        }
    }

}
