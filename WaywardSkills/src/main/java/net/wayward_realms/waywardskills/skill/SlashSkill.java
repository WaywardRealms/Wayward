package net.wayward_realms.waywardskills.skill;

import net.wayward_realms.waywardlib.character.*;
import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.classes.Class;
import net.wayward_realms.waywardlib.classes.ClassesPlugin;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.Skill;
import net.wayward_realms.waywardlib.skills.SkillType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static net.wayward_realms.waywardlib.classes.Stat.MELEE_ATTACK;
import static net.wayward_realms.waywardlib.classes.Stat.MELEE_DEFENCE;

public class SlashSkill implements Skill {

    private String name = "Slash";
    private int coolDown = 30;
    private SkillType type = SkillType.MELEE_OFFENCE;

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
    public boolean use(Player player) {
        for (LivingEntity livingEntity : player.getWorld().getLivingEntities()) {
            if (player.getLocation().distanceSquared(livingEntity.getLocation()) <= 64) {
                livingEntity.damage(4D, player);
            }
        }
        return true;
    }

    @Override
    public boolean use(Fight fight, Combatant attacking, Combatant defending, ItemStack weapon) {
        return use(fight, (Character) attacking, (Character) defending, weapon);
    }

    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
		//Player attackingPlayer = attacking.getPlayer().getPlayer();
		//Player defendingPlayer = defending.getPlayer().getPlayer();
		//attackingPlayer.setVelocity(defendingPlayer.getLocation().toVector().subtract(attackingPlayer.getLocation().toVector()).setY(1));
        int attackerLevel = 0;
        RegisteredServiceProvider<ClassesPlugin> classesPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(ClassesPlugin.class);
        if (classesPluginProvider != null) {
            ClassesPlugin classesPlugin = classesPluginProvider.getProvider();
            attackerLevel = classesPlugin.getLevel(attacking);
        }
		Random random = new Random();
		int attack = attacking.getStatValue(MELEE_ATTACK);
		int defence = defending.getStatValue(MELEE_DEFENCE);
        double a = (2D * (double) attackerLevel + 10D) / 250D;
        double b = (double) attack / Math.max((double) defence, 1D);
        double power = 50D;
        double weaponModifier = 1D;
        if (weapon != null) {
            switch (weapon.getType()) {
                case WOOD_SWORD: weaponModifier = 1.1D; break;
                case STONE_SWORD: weaponModifier = 1.2D; break;
                case IRON_SWORD: weaponModifier = 1.3D; break;
                case DIAMOND_SWORD: weaponModifier = 1.5D; break;
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
                player.sendMessage(ChatColor.YELLOW + attacking.getName() + " slashed at " + defending.getName() + " dealing " + (Math.round(damage * 100D) / 100D) + " points of damage.");
            }
        }
        return true;
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
	public ItemStack getIcon() {
		ItemStack icon = new ItemStack(Material.IRON_SWORD, 1);
		ItemMeta iconMeta = icon.getItemMeta();
		iconMeta.setDisplayName("Slash");
		icon.setItemMeta(iconMeta);
		return icon;
	}

	public boolean canUse(Class clazz, int level) {
		return clazz.getSkillPointBonus(SkillType.MELEE_OFFENCE) * level >= 1;
	}

    @Override
    public boolean canUse(Combatant combatant) {
        return canUse((Character) combatant);
    }

    @Override
    public boolean canUse(Character character) {
		return character.getSkillPoints(SkillType.MELEE_OFFENCE) >= 1;
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
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("name", name);
        serialised.put("cooldown", coolDown);
        return serialised;
    }

    public static SlashSkill deserialize(Map<String, Object> serialised) {
        SlashSkill deserialised = new SlashSkill();
        deserialised.name = (String) serialised.get("name");
        deserialised.coolDown = (int) serialised.get("cooldown");
        return deserialised;
    }

}
