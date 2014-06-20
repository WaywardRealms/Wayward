package net.wayward_realms.waywardmonsters;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Party;
import net.wayward_realms.waywardlib.classes.ClassesPlugin;
import net.wayward_realms.waywardlib.economy.EconomyPlugin;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Collection;
import java.util.Random;

public class EntityDeathListener implements Listener {

        private WaywardMonsters plugin;

    public EntityDeathListener(WaywardMonsters plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            Player player = null;
            if (((EntityDamageByEntityEvent) event.getEntity().getLastDamageCause()).getDamager() instanceof Player) {
                player = (Player) (((EntityDamageByEntityEvent) event.getEntity().getLastDamageCause()).getDamager());
            }
            if (((EntityDamageByEntityEvent) event.getEntity().getLastDamageCause()).getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) ((EntityDamageByEntityEvent) event.getEntity().getLastDamageCause()).getDamager();
                if (projectile.getShooter() instanceof LivingEntity) {
                    LivingEntity shooter = (LivingEntity) ((Projectile) ((EntityDamageByEntityEvent) event.getEntity().getLastDamageCause()).getDamager()).getShooter();
                    if (shooter != null) {
                        if (shooter instanceof Player) {
                            player = (Player) shooter;
                        }
                    }
                }
            }
            Random random = new Random();
            if (player != null) {
                int exp;
                int money;
                int expScale = plugin.getConfig().getInt("experience-multiplier." + event.getEntityType().toString(), 0);
                int entityLevel = plugin.getEntityLevelManager().getEntityLevel(event.getEntity());
                exp = (int) Math.ceil(((double) entityLevel * (double) expScale));
                money = random.nextInt(100) < 5 ? random.nextInt(5) : 0;
                if (exp > 0) {
                    RegisteredServiceProvider<ClassesPlugin> classesPluginProvider = plugin.getServer().getServicesManager().getRegistration(ClassesPlugin.class);
                    if (classesPluginProvider != null) {
                        ClassesPlugin classesPlugin = classesPluginProvider.getProvider();
                        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                        if (characterPluginProvider != null) {
                            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                            Character character = characterPlugin.getActiveCharacter(player);
                            Party party = characterPlugin.getParty(character);
                            if (party != null) {
                                Collection<? extends Character> partyMembers = party.getMembers();
                                if (partyMembers.size() > 1) {
                                    exp = (int) Math.round((exp * 1.5D) / (double) partyMembers.size());
                                    for (Character partyMember : partyMembers) {
                                        if (partyMember.getPlayer().isOnline()) classesPlugin.giveExperience(partyMember, exp);
                                    }
                                } else {
                                    classesPlugin.giveExperience(player, exp);
                                }
                            } else {
                                classesPlugin.giveExperience(player, exp);
                            }
                        }
                    }
                }
                if (!(event.getEntity() instanceof Player)) {
                    event.getDrops().clear();
                    event.getDrops().addAll(plugin.getMobDropManager().getDrops(event.getEntity().getType(), entityLevel));
                }
                if (money > 0) {
                    RegisteredServiceProvider<EconomyPlugin> economyPluginProvider = plugin.getServer().getServicesManager().getRegistration(EconomyPlugin.class);
                    if (economyPluginProvider != null) {
                        EconomyPlugin economyPlugin = economyPluginProvider.getProvider();
                        ItemStack coins = new ItemStack(Material.GOLD_NUGGET, money);
                        ItemMeta coinMeta = coins.getItemMeta();
                        coinMeta.setDisplayName(economyPlugin.getPrimaryCurrency().getNameSingular());
                        coins.setItemMeta(coinMeta);
                        event.getDrops().add(coins);
                    }
                }
                event.setDroppedExp(0);
            } else {
                if (!(event.getEntity() instanceof Player)) {
                    event.getDrops().clear();
                    event.getDrops().addAll(plugin.getMobDropManager().getDrops(event.getEntity().getType(), plugin.getEntityLevel(event.getEntity())));
                }
            }
        } else {
            if (!(event.getEntity() instanceof Player)) {
                event.getDrops().clear();
                event.getDrops().addAll(plugin.getMobDropManager().getDrops(event.getEntity().getType(), plugin.getEntityLevel(event.getEntity())));
            }
        }
    }

}
