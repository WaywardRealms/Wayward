package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.classes.ClassesPlugin;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardlib.skills.SkillsPlugin;
import net.wayward_realms.waywardlib.skills.Spell;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static net.wayward_realms.waywardlib.classes.Stat.MAGIC_ATTACK;
import static net.wayward_realms.waywardlib.classes.Stat.MAGIC_DEFENCE;

public class IceboltSpell implements Spell {

    private String name = "Icebolt";
    private int manaCost = 10;
    private int coolDown = 0;
    private SkillType type = SkillType.MAGIC_OFFENCE;

    @Override
    public boolean use(Player player) {
        Plugin plugin = Bukkit.getServer().getServicesManager().getRegistration(SkillsPlugin.class).getProvider();
        scheduleLaunches(plugin, player, 0L, 5L, 10L, 15L, 20L, 25L, 30L, 35L, 40L, 45L, 50L);
        return true;
    }

    @Override
    public boolean use(Fight fight, Combatant attacking, Combatant defending, ItemStack weapon) {
        return use(fight, (Character) attacking, (Character) defending, weapon);
    }

    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        Player attackingPlayer = attacking.getPlayer().getPlayer();
        if (attacking.getMana() >= 10) {
            attackingPlayer.launchProjectile(Snowball.class);
            Random random = new Random();
            int attackerLevel = 0;
            RegisteredServiceProvider<ClassesPlugin> classesPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(ClassesPlugin.class);
            if (classesPluginProvider != null) {
                ClassesPlugin classesPlugin = classesPluginProvider.getProvider();
                attackerLevel = classesPlugin.getLevel(attacking);
            }
            int attack = attacking.getStatValue(MAGIC_ATTACK);
            int defence = defending.getStatValue(MAGIC_DEFENCE);
            double a = (2D * (double) attackerLevel + 10D) / 250D;
            double b = (double) attack / Math.max((double) defence, 1D);
            double power = 55D;
            double weaponModifier = 1D;
            if (weapon != null) {
                switch (weapon.getType()) {
                    case STICK: weaponModifier = 1.1D; break;
                    case BLAZE_ROD: weaponModifier = 1.5D; break;
                }
            }
            boolean critical = random.nextInt(100) < 2;
            double modifier = (critical ? 2D : 1D) * weaponModifier * (((double) random.nextInt(15) + 85D) / 100D);
            int damage = (int) Math.round((a * b * power) + 2D * modifier);
            defending.setHealth(defending.getHealth() - damage);
            defending.getPlayer().getPlayer().setHealth(Math.max(defending.getHealth(), 0D));
            for (Character character : fight.getCharacters()) {
                if (character.getPlayer().isOnline()) {
                    Player player = character.getPlayer().getPlayer();
                    if (critical) {
                        player.sendMessage(ChatColor.YELLOW + "Critical hit!");
                    }
                    player.sendMessage(ChatColor.YELLOW + attacking.getName() + " launched an icebolt at " + defending.getName() + " dealing " + (Math.round(damage * 100D) / 100D) + " points of damage.");
                }
            }
            return true;
        } else {
            for (Character character : fight.getCharacters()) {
                if (character.getPlayer().isOnline()) {
                    Player player = character.getPlayer().getPlayer();
                    player.sendMessage(ChatColor.YELLOW + attacking.getName() + " tried to form an icebolt, but did not have enough mana.");
                }
            }
        }
        return false;
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.SNOW_BALL);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Icebolt");
        icon.setItemMeta(meta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.MAGIC_OFFENCE) >= 20;
    }

    @Override
    public boolean canUse(Combatant combatant) {
        return canUse((Character) combatant);
    }

    @Override
    public boolean canUse(OfflinePlayer player) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            return canUse(characterPlugin.getActiveCharacter(player));
        }
        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public SkillType getType() {
        return type;
    }

    @Override
    public void setType(SkillType type) {
        this.type = type;
    }

    @Override
    public int getCoolDown() {
        return coolDown;
    }

    @Override
    public void setCoolDown(int coolDown) {
        this.coolDown = coolDown;
    }

    @Override
    public int getManaCost() {
        return manaCost;
    }

    @Override
    public void setManaCost(int cost) {
        this.manaCost = cost;
    }

    private void scheduleLaunches(final Plugin plugin, final Player player, long... delays) {
        for (long delay : delays) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

                @Override
                public void run() {
                    Snowball snowball = player.launchProjectile(Snowball.class);
                    snowball.setMetadata("isIcebolt", new FixedMetadataValue(plugin, true));
                }

            }, delay);
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("name", name);
        serialised.put("mana-cost", manaCost);
        serialised.put("cooldown", coolDown);
        return serialised;
    }

    public static IceboltSpell deserialize(Map<String, Object> serialised) {
        IceboltSpell deserialised = new IceboltSpell();
        deserialised.name = (String) serialised.get("name");
        deserialised.manaCost = (int) serialised.get("mana-cost");
        deserialised.coolDown = (int) serialised.get("cooldown");
        return deserialised;
    }

}
