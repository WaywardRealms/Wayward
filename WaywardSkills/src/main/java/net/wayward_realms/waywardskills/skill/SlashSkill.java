package net.wayward_realms.waywardskills.skill;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.AttackSkillBase;
import net.wayward_realms.waywardlib.skills.Stat;
import net.wayward_realms.waywardskills.WaywardSkills;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SlashSkill extends AttackSkillBase {

    private WaywardSkills plugin;

    public SlashSkill(WaywardSkills plugin) {
        this.plugin = plugin;
        setName("Slash");
        setCoolDown(30);
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
                EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(player, livingEntity, EntityDamageEvent.DamageCause.ENTITY_ATTACK, 4D);
                Bukkit.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    if (event.getEntity() instanceof LivingEntity) {
                        ((LivingEntity) event.getEntity()).damage(event.getDamage(), event.getDamager());
                        event.getEntity().setLastDamageCause(event);
                    }
                }
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
        return (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " slashed at " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()) + ChatColor.YELLOW + " dealing " + damage + " points of damage.";
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.IRON_SWORD, 1);
        ItemMeta iconMeta = icon.getItemMeta();
        iconMeta.setDisplayName("Slash");
        icon.setItemMeta(iconMeta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return plugin.getSpecialisationValue(character, plugin.getSpecialisation("Sword Offence")) >= 3;
    }

    @Override
    public String getDescription() {
        return "Deal damage equal to the difference between your melee attack roll and your opponent's melee defence roll";
    }

    @Override
    public String getSpecialisationInfo() {
        return ChatColor.GRAY + "3 Sword Offence points required";
    }

}
