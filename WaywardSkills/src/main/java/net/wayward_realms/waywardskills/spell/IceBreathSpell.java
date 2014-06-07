package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardlib.skills.SpellBase;
import net.wayward_realms.waywardskills.WaywardSkills;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

public class IceBreathSpell extends SpellBase {

    private WaywardSkills plugin;

    public IceBreathSpell(WaywardSkills plugin) {
        this.plugin = plugin;
        setName("IceBreath");
        setManaCost(8);
        setCoolDown(90);
        setType(SkillType.MAGIC_NATURE);
    }

    @Override
    public boolean use(Player player) {
        Snowball snowball = player.launchProjectile(Snowball.class);
        snowball.setMetadata("isIceBreath", new FixedMetadataValue(plugin, true));
        return true;
    }

    @Override
    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        //TODO: Status effects - freeze other players
        if (attacking.getMana() >= getManaCost()) {
            if (attacking.getPlayer().isOnline()) {
                Player player = attacking.getPlayer().getPlayer();
                player.launchProjectile(Snowball.class);
                fight.sendMessage(ChatColor.YELLOW + attacking.getName() + " breathed an ice cold wind at " + defending.getName() + ", not freezing them because status effects aren't implemented yet. Sorry!");
            }
            return true;
        } else {
            fight.sendMessage(ChatColor.YELLOW + attacking.getName() + " breathed an ice cold wind, but nothing happened.");
        }
        return false;
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.SNOW_BALL);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Ice Breath");
        icon.setItemMeta(meta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.MAGIC_NATURE) >= 20;
    }

}
