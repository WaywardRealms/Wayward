package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardlib.skills.SpellBase;
import net.wayward_realms.waywardskills.WaywardSkills;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CloakOfShadowsSpell extends SpellBase {

    private WaywardSkills plugin;

    public CloakOfShadowsSpell(WaywardSkills plugin) {
        this.plugin = plugin;
        setName("CloakOfShadows");
        setCoolDown(300);
        setManaCost(50);
        setType(SkillType.MAGIC_ILLUSION);
    }

    @Override
    public boolean use(final Player player) {
        for (Player player1 : player.getWorld().getPlayers()) {
            player1.hidePlayer(player);
            if (player.getLocation().distanceSquared(player1.getLocation()) <= 256) player1.sendMessage(ChatColor.BLUE + player.getDisplayName() + " disappeared into the shadows.");
        }
        player.getWorld().playEffect(player.getLocation(), Effect.SMOKE, 0);
        player.sendMessage(ChatColor.GREEN + "You cloak yourself in shadows, no one can see you for 15 seconds.");
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Player player1 : player.getWorld().getPlayers()) {
                    player1.showPlayer(player);
                    if (player.getLocation().distanceSquared(player1.getLocation()) <= 256) player1.sendMessage(ChatColor.BLUE + player.getDisplayName() + " stepped out from the shadows.");
                }
                player.getWorld().playEffect(player.getLocation(), Effect.SMOKE, 0);
                player.sendMessage(ChatColor.RED + "You step out from the shadows, people can see you once again.");
            }
        }, 300L);
        return true;
    }

    @Override
    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        //TODO Status effects, requires #37
        return false;
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Cloak of Shadows");
        icon.setItemMeta(meta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.MAGIC_ILLUSION) >= 45;
    }

}
