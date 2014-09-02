package net.wayward_realms.waywardskills.skill;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.AttackSkillBase;
import net.wayward_realms.waywardlib.skills.Stat;
import net.wayward_realms.waywardskills.WaywardSkills;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

public class RazorShotSkill extends AttackSkillBase {

    private WaywardSkills plugin;

    public RazorShotSkill(WaywardSkills plugin) {
        this.plugin = plugin;
        setName("RazorShot");
        setCoolDown(60);
        setAttackStat(Stat.RANGED_ATTACK);
        setDefenceStat(Stat.RANGED_DEFENCE);
        setCriticalChance(15);
        setHitChance(98);
        setPower(75);
    }

    @Override
    public void animate(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        Player player = attacking.getPlayer().getPlayer();
        player.launchProjectile(Arrow.class);
    }

    @Override
    public double getWeaponModifier(ItemStack weapon) {
        return getRangedWeaponModifier(weapon);
    }

    @Override
    public String getFightUseMessage(Character attacking, Character defending, double damage) {
        return (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " shot a razor-edged arrow at " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()) + ChatColor.YELLOW + " dealing " + damage + " points of damage.";
    }

    @Override
    public boolean use(Player player) {
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
            if (player.getInventory().containsAtLeast(new ItemStack(Material.ARROW), 1)) {
                Arrow arrow = player.launchProjectile(Arrow.class);
                arrow.setMetadata("isRazorShot", new FixedMetadataValue(plugin, true));
                player.getInventory().removeItem(new ItemStack(Material.ARROW));
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
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.ARROW);
        ItemMeta iconMeta = icon.getItemMeta();
        iconMeta.setDisplayName("Razor Shot");
        icon.setItemMeta(iconMeta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return false;
    }

    @Override
    public String getDescription() {
        return "If your ranged attack roll is greater than your opponent's ranged defence roll, 3 damage is dealt to your opponent every turn for 5 turns";
    }

    @Override
    public String getSpecialisationInfo() {
        return ChatColor.GRAY + "75 Bow Offence points required";
    }

}
