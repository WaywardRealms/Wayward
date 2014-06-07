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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

public class ArrowSkill extends AttackSkillBase {

    public ArrowSkill() {
        setName("Arrow");
        setCoolDown(5);
        setType(SkillType.RANGED_OFFENCE);
        setAttackStat(Stat.RANGED_ATTACK);
        setDefenceStat(Stat.RANGED_DEFENCE);
        setCriticalChance(2);
        setHitChance(95);
        setPower(50);
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.ARROW, 1);
        ItemMeta iconMeta = icon.getItemMeta();
        iconMeta.setDisplayName("Arrow");
        icon.setItemMeta(iconMeta);
        return icon;
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
    public void animate(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        attacking.getPlayer().getPlayer().launchProjectile(Arrow.class);
    }

    @Override
    public double getWeaponModifier(ItemStack weapon) {
        return getRangedWeaponModifier(weapon);
    }

    @Override
    public String getFightUseMessage(Character attacking, Character defending, double damage) {
        return attacking.getName() + " shot an arrow at " + defending.getName() + " dealing " + damage + " points of damage.";
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

}
