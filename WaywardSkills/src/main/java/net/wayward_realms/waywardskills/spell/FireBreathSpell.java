package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.combat.StatusEffect;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardlib.skills.SpellBase;
import net.wayward_realms.waywardskills.WaywardSkills;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

public class FireBreathSpell extends SpellBase {

    private WaywardSkills plugin;

    public FireBreathSpell(WaywardSkills plugin) {
        this.plugin = plugin;
        setName("FireBreath");
        setType(SkillType.MAGIC_OFFENCE);
        setCoolDown(15);
        setManaCost(5);
    }

    @Override
    public boolean use(Player player) {
        SmallFireball fireball = player.launchProjectile(SmallFireball.class);
        fireball.setMetadata("isFireBreath", new FixedMetadataValue(plugin, true));
        return true;
    }

    @Override
    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        if (attacking.getMana() >= getManaCost()) {
            if (attacking.getPlayer().isOnline()) {
                Player player = attacking.getPlayer().getPlayer();
                player.launchProjectile(Snowball.class);
                fight.setStatusTurns(attacking, StatusEffect.BURNED, 5);
                fight.sendMessage(ChatColor.YELLOW + (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " scorched " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()) + ChatColor.YELLOW + " with burning hot breath.");
            }
            return true;
        } else {
            fight.sendMessage(ChatColor.YELLOW + (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " breathed a burning hot wind, but nothing happened.");
        }
        return false;
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.FIREBALL);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Fire Breath");
        icon.setItemMeta(meta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.MAGIC_OFFENCE) >= 5;
    }

}
