package net.wayward_realms.waywardskills.skill;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.AttackSkillBase;
import net.wayward_realms.waywardlib.skills.SkillsPlugin;
import net.wayward_realms.waywardlib.skills.Stat;
import net.wayward_realms.waywardskills.WaywardSkills;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

public class MultiShotSkill extends AttackSkillBase {

    private WaywardSkills plugin;

    public MultiShotSkill(WaywardSkills plugin) {
        this.plugin = plugin;
        setName("MultiShot");
        setCriticalChance(30);
        setCriticalMultiplier(1.1D);
        setHitChance(95);
        setPower(20);
        setCoolDown(20);
        setAttackStat(Stat.RANGED_ATTACK);
        setDefenceStat(Stat.RANGED_DEFENCE);
    }

    @Override
    public void animate(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        final Player player = attacking.getPlayer().getPlayer();
        Plugin plugin = Bukkit.getServer().getServicesManager().getRegistration(SkillsPlugin.class).getProvider();
        for (int i = 0; i < 5; i++) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    player.launchProjectile(Arrow.class);
                    player.getInventory().removeItem(new ItemStack(Material.ARROW) );
                }
            }, i * 10);
        }
    }

    @Override
    public double getWeaponModifier(ItemStack weapon) {
        return getRangedWeaponModifier(weapon);
    }

    @Override
    public String getFightUseMessage(Character attacking, Character defending, double damage) {
        return (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " launched multiple arrows at " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()) + ChatColor.YELLOW + ", dealing " + damage + " damage.";
    }

    @Override
    public boolean use(final Player player) {
        boolean containsBow = false;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                if (item.getType() == Material.BOW) {
                    containsBow = true;
                    break;
                }
            }
        }
        if (containsBow) {
            if (player.getInventory().containsAtLeast(new ItemStack(Material.ARROW), 5)) {
                Plugin plugin = Bukkit.getServer().getServicesManager().getRegistration(SkillsPlugin.class).getProvider();
                for (int i = 0; i < 5; i++) {
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            player.launchProjectile(Arrow.class);
                            player.getInventory().removeItem(new ItemStack(Material.ARROW) );
                        }
                    }, i * 10);
                }
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "You require at least 5 arrows.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "You require a bow to launch a multishot.");
        }
        return false;
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.BOW);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("MultiShot");
        icon.setItemMeta(meta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return plugin.getSpecialisationValue(character, plugin.getSpecialisation("Bow Offence")) >= 20;
    }

    @Override
    public String getDescription() {
        return "Deals one fifth of the difference between your ranged attack roll and your target's ranged defence roll five times";
    }

    @Override
    public List<String> getSpecialisationInfo() {
        return Arrays.asList(ChatColor.GRAY + "20 Bow Offence points required");
    }

}
