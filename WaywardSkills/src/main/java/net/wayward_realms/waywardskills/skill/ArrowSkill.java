package net.wayward_realms.waywardskills.skill;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
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
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static net.wayward_realms.waywardlib.classes.Stat.RANGED_ATTACK;
import static net.wayward_realms.waywardlib.classes.Stat.RANGED_DEFENCE;

public class ArrowSkill implements Skill {

    private String name = "Arrow";
    private SkillType type = SkillType.RANGED_OFFENCE;
    private int coolDown = 5;

	@Override
	public ItemStack getIcon() {
		ItemStack icon = new ItemStack(Material.ARROW, 1);
		ItemMeta iconMeta = icon.getItemMeta();
		iconMeta.setDisplayName("Arrow");
		icon.setItemMeta(iconMeta);
		return icon;
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
    public boolean use(Player player) {
        boolean containsBow = false;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item.getType() == Material.BOW) {
                containsBow = true;
                break;
            }
        }
        if (containsBow) {
            if (player.getInventory().containsAtLeast(new ItemStack(Material.ARROW), 1)) {
                player.launchProjectile(Arrow.class);
                player.getInventory().removeItem(new ItemStack(Material.ARROW), new ItemStack(Material.FERMENTED_SPIDER_EYE));
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "You require an arrow.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "You require a bow.");
        }
        return false;
    }

    @Override
    public boolean use(Fight fight, Combatant attacking, Combatant defending, ItemStack weapon) {
        return use(fight, (Character) attacking, (Character) defending, weapon);
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

    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
		Player attackingPlayer = attacking.getPlayer().getPlayer();
		attackingPlayer.launchProjectile(org.bukkit.entity.Arrow.class);
        int attackerLevel = 0;
        RegisteredServiceProvider<ClassesPlugin> classesPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(ClassesPlugin.class);
        if (classesPluginProvider != null) {
            ClassesPlugin classesPlugin = classesPluginProvider.getProvider();
            attackerLevel = classesPlugin.getLevel(attacking);
        }
        Random random = new Random();
		int attack = attacking.getStatValue(RANGED_ATTACK);
		int defence = defending.getStatValue(RANGED_DEFENCE);
        double a = (2D * (double) attackerLevel + 10D) / 250D;
        double b = (double) attack / Math.max((double) defence, 1D);
        double power = 50D;
        double weaponModifier = 1D;
        if (weapon != null) {
            switch (weapon.getType()) {
                case BOW: weaponModifier = 1.5D; break;
            }
        }
        boolean critical = random.nextInt(100) < 2;
        double modifier = (critical ? 2D : 1D) * weaponModifier * (((double) random.nextInt(15) + 85D) / 100D);
        int damage = (int) Math.round((a * b * power) + 2D * modifier);
        defending.setHealth(defending.getHealth() - damage);
        defending.getPlayer().getPlayer().setHealth(Math.max(defending.getHealth(), 0D));
        if (critical) {
            fight.sendMessage(ChatColor.YELLOW + "Critical hit!");
        }
        fight.sendMessage(ChatColor.YELLOW + attacking.getName() + " shot an arrow at " + defending.getName() + " dealing " + (Math.round(damage * 100D) / 100D) + " points of damage.");
        return true;
	}

	public boolean canUse(Class clazz, int level) {
		return clazz.getSkillPointBonus(SkillType.RANGED_OFFENCE) * level >= 1;
	}

    @Override
    public boolean canUse(Combatant combatant) {
        return canUse((Character) combatant);
    }

    @Override
	public boolean canUse(Character character) {
		return character.getSkillPoints(SkillType.RANGED_OFFENCE) >= 1;
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

    public static ArrowSkill deserialize(Map<String, Object> serialised) {
        ArrowSkill deserialised = new ArrowSkill();
        deserialised.name = (String) serialised.get("name");
        deserialised.coolDown = (int) serialised.get("cooldown");
        return deserialised;
    }

}
