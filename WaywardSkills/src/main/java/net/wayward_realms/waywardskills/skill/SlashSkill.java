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

public class SlashSkill extends AttackSkillBase {

    public SlashSkill() {
        setName("Slash");
        setCoolDown(30);
        setType(SkillType.MELEE_OFFENCE);
        setCriticalChance(2);
        setHitChance(95);
        setAttackStat(Stat.MELEE_ATTACK);
        setDefenceStat(Stat.MELEE_DEFENCE);
        setPower(50);
    }

    @Override
    public boolean use(Player player) {
        for (LivingEntity livingEntity : player.getWorld().getLivingEntities()) {
            if (livingEntity == player) continue;
            if (player.getLocation().distanceSquared(livingEntity.getLocation()) <= 64) {
                livingEntity.damage(4D, player);
            }
        }
        return true;
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
        return attacking.getName() + " slashed at " + defending.getName() + " dealing " + damage + " points of damage.";
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

}
