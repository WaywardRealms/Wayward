package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.classes.ClassesPlugin;
import net.wayward_realms.waywardlib.classes.Class;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardlib.skills.Spell;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static net.wayward_realms.waywardlib.classes.Stat.MAGIC_ATTACK;
import static net.wayward_realms.waywardlib.classes.Stat.MAGIC_DEFENCE;

public class FireballSpell implements Spell {

    private String name = "Fireball";
    private int manaCost = 5;
    private int coolDown = 15;
    private SkillType type = SkillType.MAGIC_OFFENCE;

	@Override
	public ItemStack getIcon() {
		ItemStack icon = new ItemStack(Material.BLAZE_POWDER, 1);
		ItemMeta iconMeta = icon.getItemMeta();
		iconMeta.setDisplayName("Fireball");
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
    public boolean use(Player player) {
        player.launchProjectile(Fireball.class);
        return true;
    }

    @Override
    public boolean use(Fight fight, Combatant attacking, Combatant defending, ItemStack weapon) {
        return use(fight, (Character) attacking, (Character) defending, weapon);
    }

	public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
		Player attackingPlayer = attacking.getPlayer().getPlayer();
        if (attacking.getMana() >= 5) {
            attackingPlayer.launchProjectile(org.bukkit.entity.Fireball.class);
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
            double power = 50D;
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
                    player.sendMessage(ChatColor.YELLOW + attacking.getName() + " launched a fireball at " + defending.getName() + " dealing " + (Math.round(damage * 100D) / 100D) + " points of damage.");
                }
            }
            return true;
        } else {
            for (Character character : fight.getCharacters()) {
                if (character.getPlayer().isOnline()) {
                    Player player = character.getPlayer().getPlayer();
                    player.sendMessage(ChatColor.YELLOW + attacking.getName() + " tried to form a fireball, but did not have enough mana.");
                }
            }
        }
		return false;
	}

	public boolean canUse(Class clazz, int level) {
		return clazz.getSkillPointBonus(SkillType.MAGIC_OFFENCE) * level >= 30;
	}

    @Override
    public boolean canUse(Combatant combatant) {
        return canUse((Character) combatant);
    }

	public boolean canUse(Character character) {
		return character.getSkillPoints(SkillType.MAGIC_OFFENCE) >= 30;
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
    public int getManaCost() {
        return manaCost;
    }

    @Override
    public void setManaCost(int cost) {
        this.manaCost = cost;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("name", name);
        serialised.put("mana-cost", manaCost);
        serialised.put("cooldown", coolDown);
        return serialised;
    }

    public static FireballSpell deserialize(Map<String, Object> serialised) {
        FireballSpell deserialised = new FireballSpell();
        deserialised.name = (String) serialised.get("name");
        deserialised.manaCost = (int) serialised.get("mana-cost");
        deserialised.coolDown = (int) serialised.get("cooldown");
        return deserialised;
    }

}
