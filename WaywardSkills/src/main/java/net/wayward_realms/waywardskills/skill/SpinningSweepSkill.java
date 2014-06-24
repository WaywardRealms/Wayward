package net.wayward_realms.waywardskills.skill;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.AttackSkillBase;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardlib.util.vector.Vector3D;
import net.wayward_realms.waywardlib.util.vector.VectorUtils;
import net.wayward_realms.waywardskills.WaywardSkills;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpinningSweepSkill extends AttackSkillBase {

    private WaywardSkills plugin;

    public SpinningSweepSkill(WaywardSkills plugin) {
        this.plugin = plugin;
        setName("SpinningSweep");
        setType(SkillType.MELEE_OFFENCE);
        setCoolDown(30);
        setAttackStat(Stat.MELEE_ATTACK);
        setDefenceStat(Stat.MELEE_DEFENCE);
        setPower(10D);
        setHitChance(100);
        setCriticalMultiplier(10D);
        setCriticalChance(1);
    }

    @Override
    public void animate(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        final Player attackingPlayer = attacking.getPlayer().getPlayer();
        for (int i = 0; i < 360; i += 5) {
            final int theta = i;
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    Location adjustedLocation = new Location(attackingPlayer.getLocation().getWorld(), attackingPlayer.getLocation().getX(), attackingPlayer.getLocation().getY(), attackingPlayer.getLocation().getZ(), theta, attackingPlayer.getLocation().getPitch());
                    attackingPlayer.teleport(adjustedLocation);
                }
            }, i * 2);
        }
    }

    @Override
    public double getWeaponModifier(ItemStack weapon) {
        return getMeleeWeaponModifier(weapon);
    }

    @Override
    public String getFightUseMessage(Character attacking, Character defending, double damage) {
        return (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " slashed in circles towards " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()) + ChatColor.YELLOW + ", dealing " + damage + " damage.";
    }

    @Override
    public boolean use(final Player player) {
        for (int i = 0; i < 360; i += 10) {
            final int theta = i;
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    Location adjustedLocation = new Location(player.getLocation().getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), theta, player.getLocation().getPitch());
                    player.teleport(adjustedLocation);
                    Location observerPos = player.getEyeLocation();
                    Vector3D observerDir = new Vector3D(observerPos.getDirection());
                    Vector3D observerStart = new Vector3D(observerPos);
                    Vector3D observerEnd = observerStart.add(observerDir.multiply(5));
                    for (LivingEntity target : player.getWorld().getLivingEntities()) {
                        Vector3D targetPos = new Vector3D(target.getLocation());
                        Vector3D minimum = targetPos.add(-0.5, 0, -0.5);
                        Vector3D maximum = targetPos.add(0.5, 1.67, 0.5);
                        if (target != player && VectorUtils.hasIntersection(observerStart, observerEnd, minimum, maximum)) {
                            player.teleport(target);
                            target.damage(10D, player);
                        }
                    }
                }
            }, i * 2 / 10);
        }
        return true;
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.STONE_SWORD, 1);
        ItemMeta iconMeta = icon.getItemMeta();
        iconMeta.setDisplayName("Spinning Sweep");
        icon.setItemMeta(iconMeta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.MELEE_OFFENCE) >= 10;
    }
}
