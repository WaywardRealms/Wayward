package net.wayward_realms.waywardskills.skill;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.classes.Class;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.AttackSkillBase;
import net.wayward_realms.waywardlib.skills.SkillType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.HashMap;
import java.util.Map;

public class StabSkill extends AttackSkillBase {

    public StabSkill() {
        setName("Stab");
        setCoolDown(60);
        setType(SkillType.MELEE_OFFENCE);
        setAttackStat(Stat.MELEE_ATTACK);
        setDefenceStat(Stat.MELEE_DEFENCE);
        setPower(40);
        setHitChance(90);
        setCriticalChance(20);
        setCriticalMultiplier(1.5D);
    }

    @Override
    public boolean use(Player player) {
        for (LivingEntity livingEntity : player.getWorld().getLivingEntities()) {
            if (player.hasLineOfSight(livingEntity)) {
                player.teleport(livingEntity);
                livingEntity.damage(10D, player);
            }
        }
        return true;
    }

    @Override
    public boolean use(Fight fight, Combatant attacking, Combatant defending, ItemStack weapon) {
        return use(fight, (Character) attacking, (Character) defending, weapon);
    }

    @Override
    public void animate(Fight fight, Character attacking, Character defending, ItemStack weapon) {

    }

    @Override
    public double getWeaponModifier(ItemStack weapon) {
        return getMeleeWeaponModifier(weapon);
    }

    @Override
    public String getFightUseMessage(Character attacking, Character defending, double damage) {
        return attacking.getName() + " stabbed at " + defending.getName() + " dealing " + (Math.round(damage * 100D) / 100D) + " points of damage.";
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.IRON_SWORD, 1);
        ItemMeta iconMeta = icon.getItemMeta();
        iconMeta.setDisplayName("Stab");
        icon.setItemMeta(iconMeta);
        return icon;
    }

    public boolean canUse(Class clazz, int level) {
        return clazz.getSkillPointBonus(SkillType.MELEE_OFFENCE) * level >= 5;
    }

    @Override
    public boolean canUse(Combatant combatant) {
        return canUse((Character) combatant);
    }

    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.MELEE_OFFENCE) >= 5;
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
        serialised.put("name", getName());
        serialised.put("cooldown", getCoolDown());
        return serialised;
    }

    public static StabSkill deserialize(Map<String, Object> serialised) {
        StabSkill deserialised = new StabSkill();
        deserialised.setName((String) serialised.get("name"));
        deserialised.setCoolDown((int) serialised.get("cooldown"));
        return deserialised;
    }

}
