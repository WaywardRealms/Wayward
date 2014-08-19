package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.combat.StatusEffect;
import net.wayward_realms.waywardlib.skills.AttackSpellBase;
import net.wayward_realms.waywardlib.skills.SkillsPlugin;
import net.wayward_realms.waywardskills.WaywardSkills;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.EnumMap;
import java.util.Map;

import static net.wayward_realms.waywardlib.skills.Stat.MAGIC_ATTACK;
import static net.wayward_realms.waywardlib.skills.Stat.MAGIC_DEFENCE;

public class IceboltSpell extends AttackSpellBase {

    private WaywardSkills plugin;

    public IceboltSpell(WaywardSkills plugin) {
        this.plugin = plugin;
        setName("Icebolt");
        setManaCost(10);
        setCoolDown(0);
        setPower(55);
        setCriticalChance(2);
        setAttackStat(MAGIC_ATTACK);
        setDefenceStat(MAGIC_DEFENCE);
    }

    @Override
    public boolean use(Player player) {
        Plugin plugin = Bukkit.getServer().getServicesManager().getRegistration(SkillsPlugin.class).getProvider();
        scheduleLaunches(plugin, player, 0L, 5L, 10L, 15L, 20L, 25L, 30L, 35L, 40L, 45L, 50L);
        return true;
    }

    @Override
    public void animate(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        attacking.getPlayer().getPlayer().launchProjectile(Snowball.class);
    }

    @Override
    public double getWeaponModifier(ItemStack weapon) {
        return getMagicWeaponModifier(weapon);
    }

    @Override
    public String getFightUseMessage(Character attacking, Character defending, double damage) {
        return (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " launched an icebolt at " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()) + ChatColor.YELLOW + " dealing " + (Math.round(damage * 100D) / 100D) + " points of damage.";
    }

    @Override
    public String getFightFailManaMessage(Character attacking, Character defending) {
        return (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " tried to form an icebolt, but did not have enough mana.";
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
        return hasScroll(character) && plugin.getSpecialisationValue(character, plugin.getSpecialisation("Water Magic")) >= 20;
    }

    @Override
    public String getDescription() {
        return "Deal damage to one target equal to half of the difference between your magic attack stat rolled 5 times and your target's magic defence rolled 5 times";
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
    public Map<StatusEffect, Integer> getStatusEffects() {
        Map<StatusEffect, Integer> statusEffects = new EnumMap<>(StatusEffect.class);
        statusEffects.put(StatusEffect.FROZEN, 3);
        return statusEffects;
    }

    @Override
    public int getStatusEffectChance(StatusEffect statusEffect) {
        return 10;
    }

    @Override
    public String getSpecialisationInfo() {
        return ChatColor.GRAY + "20 Water Magic points required";
    }

}
