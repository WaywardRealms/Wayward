package net.wayward_realms.waywardmonsters;

import net.wayward_realms.waywardlib.economy.EconomyPlugin;
import org.bukkit.Bukkit;
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

import java.util.Random;

public class EntityDeathListener implements Listener {

    private WaywardClasses plugin;

    public EntityDeathListener(WaywardClasses plugin) {
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
                int expScale = 0;
                int exp;
                int money;
                switch (event.getEntityType()) {
                    case CREEPER:
                        expScale = 3;
                        break;
                    case SKELETON:
                        expScale = 3;
                        break;
                    case SPIDER:
                        expScale = 3;
                        break;
                    case GIANT:
                        expScale = 100;
                        break;
                    case ZOMBIE:
                        expScale = 2;
                        break;
                    case SLIME:
                        expScale = 2;
                        break;
                    case GHAST:
                        expScale = 10;
                        break;
                    case PIG_ZOMBIE:
                        expScale = 5;
                        break;
                    case ENDERMAN:
                        expScale = 5;
                        break;
                    case CAVE_SPIDER:
                        expScale = 3;
                        break;
                    case SILVERFISH:
                        expScale = 3;
                        break;
                    case BLAZE:
                        expScale = 5;
                        break;
                    case MAGMA_CUBE:
                        expScale = 2;
                        break;
                    case ENDER_DRAGON:
                        expScale = 1000;
                        break;
                    case WITHER:
                        expScale = 1000;
                        break;
                    case BAT:
                        expScale = 1;
                        break;
                    case WITCH:
                        expScale = 5;
                        break;
                    case PIG:
                        expScale = 1;
                        break;
                    case SHEEP:
                        expScale = 1;
                        break;
                    case COW:
                        expScale = 1;
                        break;
                    case CHICKEN:
                        expScale = 1;
                        break;
                    case SQUID:
                        expScale = 1;
                        break;
                    case WOLF:
                        expScale = 2;
                        break;
                    case MUSHROOM_COW:
                        expScale = 1;
                        break;
                    case SNOWMAN:
                        expScale = 1;
                        break;
                    case OCELOT:
                        expScale = 1;
                        break;
                    case IRON_GOLEM:
                        expScale = 1;
                        break;
                    case HORSE:
                        expScale = 1;
                        break;
                    case VILLAGER:
                        expScale = 1;
                        break;
                    case ENDER_CRYSTAL:
                        expScale = 4;
                        break;
                    case PLAYER:
                        expScale = 30;
                        break;
                }
                exp = (int) Math.ceil(((double) plugin.getEntityLevelManager().getEntityLevel(event.getEntity()) * (double) expScale));
                money = random.nextInt(100) < 5 ? random.nextInt(5) : 0;
                if (exp > 0) {
                    plugin.giveExperience(player, exp);
                }
                if (money > 0) {
                    RegisteredServiceProvider<EconomyPlugin> economyPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(EconomyPlugin.class);
                    if (economyPluginProvider != null) {
                        EconomyPlugin economyPlugin = economyPluginProvider.getProvider();
                        ItemStack coins = new ItemStack(Material.GOLD_NUGGET, money);
                        ItemMeta coinMeta = coins.getItemMeta();
                        coinMeta.setDisplayName(economyPlugin.getPrimaryCurrency().getNameSingular());
                        coins.setItemMeta(coinMeta);
                        event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), coins);
                    }
                }
                event.setDroppedExp(0);
            }
        }
    }

}
