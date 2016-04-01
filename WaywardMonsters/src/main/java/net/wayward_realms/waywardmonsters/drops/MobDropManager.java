package net.wayward_realms.waywardmonsters.drops;

import net.wayward_realms.waywardmonsters.WaywardMonsters;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MobDropManager {

    private WaywardMonsters plugin;

    private FileConfiguration mobDropConfig = null;
    private File mobDropConfigFile = null;

    private Random random;

    public MobDropManager(WaywardMonsters plugin) {
        this.plugin = plugin;
        random = new Random();
    }

    private FileConfiguration getMobDropConfig() {
        if (mobDropConfig == null) {
            reloadMobDropConfig();
        }
        return mobDropConfig;
    }

    private void saveDefaultMobDropConfig() {
        if (mobDropConfigFile == null) {
            mobDropConfigFile = new File(plugin.getDataFolder(), "mob-drops.yml");
        }
        if (!mobDropConfigFile.exists()) {
            mobDropConfig = new YamlConfiguration();
            for (EntityType entityType : EntityType.values()) {
                mobDropConfig.set(entityType.toString(), getDefaultDrops(entityType));
            }
            try {
                mobDropConfig.save(mobDropConfigFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    private void reloadMobDropConfig() {
        if (mobDropConfigFile == null) {
            mobDropConfigFile = new File(plugin.getDataFolder(), "mob-drops.yml");
            if (!mobDropConfigFile.exists()) {
                saveDefaultMobDropConfig();
            }
        }
        mobDropConfig = YamlConfiguration.loadConfiguration(mobDropConfigFile);
    }

    public List<ItemStack> getDrops(EntityType entityType, int level) {
        List<ItemStack> drops = new ArrayList<>();
        for (MobDrop mobDrop : (List<MobDrop>) getMobDropConfig().getList(entityType.toString())) {
            if (mobDrop.getMinLevel() <= level && (mobDrop.getMaxLevel() == -1 || level <= mobDrop.getMaxLevel()) && random.nextInt(101) <= mobDrop.getChance()) {
                drops.add(mobDrop.getDrop());
            }
        }
        return drops;
    }

    private List<MobDrop> getDefaultDrops(EntityType entityType) {
        List<MobDrop> drops = new ArrayList<>();
        switch (entityType) {
            case DROPPED_ITEM:
                break;
            case EXPERIENCE_ORB:
                break;
            case LEASH_HITCH:
                break;
            case PAINTING:
                break;
            case ARROW:
                break;
            case SNOWBALL:
                break;
            case FIREBALL:
                break;
            case SMALL_FIREBALL:
                break;
            case ENDER_PEARL:
                break;
            case ENDER_SIGNAL:
                break;
            case THROWN_EXP_BOTTLE:
                break;
            case ITEM_FRAME:
                break;
            case WITHER_SKULL:
                break;
            case PRIMED_TNT:
                break;
            case FALLING_BLOCK:
                break;
            case FIREWORK:
                break;
            case MINECART_COMMAND:
                break;
            case BOAT:
                break;
            case MINECART:
                break;
            case MINECART_CHEST:
                break;
            case MINECART_FURNACE:
                break;
            case MINECART_TNT:
                break;
            case MINECART_HOPPER:
                break;
            case MINECART_MOB_SPAWNER:
                break;
            case CREEPER:
                for (int i = 0; i < 40; i++) {
                    drops.add(new MobDrop(i, i, 90, new ItemStack(Material.SULPHUR, (int) Math.ceil((double) i / 2D))));
                }
                break;
            case SKELETON:
                for (int i = 0; i < 40; i++) {
                    drops.add(new MobDrop(i, i, 50, new ItemStack(Material.ARROW, (int) Math.ceil((double) i / 2D))));
                    drops.add(new MobDrop(i, i, 50, new ItemStack(Material.BONE, (int) Math.ceil((double) i / 2D))));
                }
                drops.add(new MobDrop(5, -1, 5, new ItemStack(Material.BOW, 1)));
                break;
            case SPIDER:case CAVE_SPIDER:
                for (int i = 0; i < 40; i++) {
                    drops.add(new MobDrop(i, i, 75, new ItemStack(Material.STRING, (int) Math.ceil((double) i / 4D))));
                    drops.add(new MobDrop(i, i, 20, new ItemStack(Material.SPIDER_EYE, (int) Math.ceil((double) i / 8D))));
                    //ItemStack spiderVenom = new Potion(PotionType.POISON).toItemStack(i);
                    ItemStack spiderVenom = new ItemStack(Material.POTION, i);
                    PotionMeta potionMeta = (PotionMeta) spiderVenom.getItemMeta();
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 120, 1), false);
                    spiderVenom.setItemMeta(potionMeta);
                    ItemMeta spiderVenomMeta = spiderVenom.getItemMeta();
                    spiderVenomMeta.setDisplayName("Spider venom");
                    spiderVenom.setItemMeta(spiderVenomMeta);
                    drops.add(new MobDrop(i, i, 2, spiderVenom));
                }
                drops.add(new MobDrop(5, 10, 5, new ItemStack(Material.NETHER_STALK)));
                break;
            case GIANT:
                drops.add(new MobDrop(0, -1, 100, new ItemStack(Material.BONE, 64)));
                drops.add(new MobDrop(0, -1, 100, new ItemStack(Material.ROTTEN_FLESH, 64)));
                break;
            case ZOMBIE:
                drops.add(new MobDrop(0, -1, 90, new ItemStack(Material.ROTTEN_FLESH)));
                drops.add(new MobDrop(0, -1, 30, new ItemStack(Material.ROTTEN_FLESH)));
                break;
            case SLIME:
                for (int i = 0; i < 40; i++) {
                    drops.add(new MobDrop(i, i, 100, new ItemStack(Material.SLIME_BALL, i)));
                }
                break;
            case GHAST:
                for (int i = 0; i < 40; i++) {
                    drops.add(new MobDrop(i, i, 50, new ItemStack(Material.GHAST_TEAR, i)));
                    drops.add(new MobDrop(i, i, 50, new ItemStack(Material.SULPHUR, i)));
                }
                break;
            case PIG_ZOMBIE:
                drops.add(new MobDrop(0, 5, 90, new ItemStack(Material.GOLD_NUGGET)));
                drops.add(new MobDrop(5, 10, 80, new ItemStack(Material.GOLD_INGOT)));
                for (int i = 10; i < 40; i += 10) {
                    drops.add(new MobDrop(i, i + 10, i * 2, new ItemStack(Material.GOLD_SWORD)));
                }
                break;
            case ENDERMAN:
                drops.add(new MobDrop(0, -1, 100, new ItemStack(Material.ENDER_PEARL)));
                drops.add(new MobDrop(0, -1, 50, new ItemStack(Material.ENDER_PEARL)));
                drops.add(new MobDrop(20, -1, 20, new ItemStack(Material.EYE_OF_ENDER)));
                break;
            case SILVERFISH:
                ItemStack silverfishDroppings1 = new ItemStack(Material.PUMPKIN_SEEDS);
                ItemMeta meta1 = silverfishDroppings1.getItemMeta();
                meta1.setDisplayName("Silverfish droppings");
                silverfishDroppings1.setItemMeta(meta1);
                drops.add(new MobDrop(0, -1, 20, silverfishDroppings1));
                ItemStack silverfishDroppings2 = new ItemStack(Material.MELON_SEEDS);
                ItemMeta meta2 = silverfishDroppings2.getItemMeta();
                meta2.setDisplayName("Silverfish droppings");
                silverfishDroppings2.setItemMeta(meta2);
                drops.add(new MobDrop(0, -1, 20, silverfishDroppings2));
                ItemStack silverfishDroppings3 = new ItemStack(Material.SEEDS);
                ItemMeta meta3 = silverfishDroppings3.getItemMeta();
                meta3.setDisplayName("Silverfish droppings");
                silverfishDroppings3.setItemMeta(meta3);
                drops.add(new MobDrop(0, -1, 20, silverfishDroppings3));
                break;
            case BLAZE:
                for (int i = 0; i < 40; i++) {
                    drops.add(new MobDrop(i, i, 75, new ItemStack(Material.BLAZE_ROD)));
                }
                drops.add(new MobDrop(0, -1, 90, new ItemStack(Material.BLAZE_POWDER, 3)));
                break;
            case MAGMA_CUBE:
                drops.add(new MobDrop(0, -1, 90, new ItemStack(Material.MAGMA_CREAM, 5)));
                break;
            case ENDER_DRAGON:
                drops.add(new MobDrop(0, -1, 100, new ItemStack(Material.DRAGON_EGG)));
                break;
            case WITHER:
                //ItemStack witherVenom = new Potion(PotionType.INSTANT_DAMAGE, 2).toItemStack(5);
                ItemStack witherVenom = new ItemStack(Material.POTION);
                ItemMeta witherVenomMeta = witherVenom.getItemMeta();
                witherVenomMeta.setDisplayName("Wither venom");
                PotionMeta witherVenomPotionMeta = (PotionMeta) witherVenomMeta;
                witherVenomPotionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 120, 2), false);
                witherVenom.setItemMeta(witherVenomMeta);
                drops.add(new MobDrop(0, -1, 2, witherVenom));
                break;
            case BAT:
                ItemStack batFang = new ItemStack(Material.GHAST_TEAR);
                ItemMeta batFangMeta = batFang.getItemMeta();
                batFangMeta.setDisplayName("Bat fang");
                batFang.setItemMeta(batFangMeta);
                drops.add(new MobDrop(0, -1, 80, batFang));
                break;
            case WITCH:
                for (PotionEffectType potionEffectType : PotionEffectType.values()) {
                    ItemStack potion = new ItemStack(Material.POTION);
                    PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
                    potionMeta.addCustomEffect(new PotionEffect(potionEffectType, 120, 1), false);
                    potion.setItemMeta(potionMeta);
                    drops.add(new MobDrop(0, -1, 5, potion));
                }
                break;
            case PIG:
                drops.add(new MobDrop(0, -1, 90, new ItemStack(Material.PORK)));
                drops.add(new MobDrop(0, -1, 10, new ItemStack(Material.GRILLED_PORK)));
                break;
            case SHEEP:
                ItemStack lampChop = new ItemStack(Material.PORK, 1);
                ItemMeta lambChop = lampChop.getItemMeta();
                lambChop.setDisplayName("Lamb chop");
                lampChop.setItemMeta(lambChop);
                drops.add(new MobDrop(0, -1, 60, lampChop));
                drops.add(new MobDrop(0, -1, 100, new ItemStack(Material.WOOL, 5)));
                break;
            case COW:
                drops.add(new MobDrop(0, -1, 90, new ItemStack(Material.RAW_BEEF)));
                drops.add(new MobDrop(0, -1, 10, new ItemStack(Material.COOKED_BEEF)));
                break;
            case CHICKEN:
                drops.add(new MobDrop(0, -1, 90, new ItemStack(Material.RAW_CHICKEN)));
                drops.add(new MobDrop(0, -1, 10, new ItemStack(Material.COOKED_CHICKEN)));
                break;
            case SQUID:
                drops.add(new MobDrop(0, -1, 100, new ItemStack(Material.INK_SACK)));
                break;
            case WOLF:
                drops.add(new MobDrop(0, -1, 100, new ItemStack(Material.BONE)));
                break;
            case MUSHROOM_COW:
                drops.add(new MobDrop(0, -1, 50, new ItemStack(Material.BROWN_MUSHROOM, 5)));
                drops.add(new MobDrop(0, -1, 50, new ItemStack(Material.RED_MUSHROOM, 5)));
                break;
            case SNOWMAN:
                drops.add(new MobDrop(0, -1, 60, new ItemStack(Material.SNOW_BALL, 16)));
                drops.add(new MobDrop(0, -1, 30, new ItemStack(Material.SNOW_BLOCK)));
                drops.add(new MobDrop(0, -1, 20, new ItemStack(Material.PUMPKIN)));
                break;
            case OCELOT:
                drops.add(new MobDrop(0, -1, 90, new ItemStack(Material.RAW_FISH)));
                break;
            case IRON_GOLEM:
                drops.add(new MobDrop(0, -1, 100, new ItemStack(Material.IRON_INGOT, 8)));
                break;
            case HORSE:
                drops.add(new MobDrop(0, -1, 30, new ItemStack(Material.HAY_BLOCK)));
                break;
            case VILLAGER:
                drops.add(new MobDrop(0, -1, 20, new ItemStack(Material.EMERALD)));
                break;
            case ENDER_CRYSTAL:
                drops.add(new MobDrop(0, -1, 100, new ItemStack(Material.ENDER_PEARL, 32)));
                break;
            case SPLASH_POTION:
                break;
            case EGG:
                break;
            case FISHING_HOOK:
                break;
            case LIGHTNING:
                break;
            case WEATHER:
                break;
            case PLAYER:
                break;
            case COMPLEX_PART:
                break;
            case UNKNOWN:
                break;
        }
        return drops;
    }

}
