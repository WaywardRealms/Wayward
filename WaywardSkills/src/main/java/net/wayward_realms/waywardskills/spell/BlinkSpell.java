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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BlinkSpell extends SpellBase {

    private WaywardSkills plugin;

    public BlinkSpell(WaywardSkills plugin) {
        this.plugin = plugin;
        setName("Blink");
        setCoolDown(90);
        setManaCost(30);
        setType(SkillType.MAGIC_ILLUSION);
    }

    @Override

    public boolean use(final Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, 0), true);
        for (int i = 0; i < 6; i++) {
            final int finalI = i;
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    if (finalI % 2 == 0){
                        for (Player player1 : plugin.getServer().getOnlinePlayers()) {
                            player1.hidePlayer(player);
                        }
                    } else {
                        for (Player player1 : plugin.getServer().getOnlinePlayers()) {
                            player1.showPlayer(player);
                        }
                    }
                }
            }, i * 100L);
        }
        return true;
    }

    @Override
    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        if (attacking.getMana() >= getManaCost()) {
            for (Character character : fight.getCharacters()) {
                fight.setStatusTurns(character, StatusEffect.BLIND, 2);
            }
            fight.sendMessage(ChatColor.YELLOW + (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " rapidly blinks, evading everyone's attacks for 2 turns");
            return true;
        } else {
            fight.sendMessage(ChatColor.YELLOW + (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " attempted to cast blink, but did not have enough mana.");
        }
        return false;
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.GLASS);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Blink");
        icon.setItemMeta(meta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.MAGIC_ILLUSION) >= 30;
    }

    @Override
    public String getDescription() {
        return "Attacks miss you for 3 turns";
    }
}
