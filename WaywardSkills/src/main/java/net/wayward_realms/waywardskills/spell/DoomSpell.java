package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.combat.StatusEffect;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardlib.skills.SpellBase;
import net.wayward_realms.waywardskills.WaywardSkills;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DoomSpell extends SpellBase {

    private WaywardSkills plugin;

    public DoomSpell(WaywardSkills plugin) {
        this.plugin = plugin;
        setName("Doom");
        setCoolDown(240);
        setManaCost(60);
        setType(SkillType.MAGIC_OFFENCE);
    }

    @Override
    public boolean use(final Player player) {
        for (final LivingEntity entity : player.getWorld().getLivingEntities()) {
            if (entity == player) continue;
            if (entity.getLocation().distanceSquared(player.getLocation()) > 256) continue;
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(player, entity, EntityDamageEvent.DamageCause.MAGIC, 9999D);
                    plugin.getServer().getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {
                        if (event.getEntity() instanceof LivingEntity) {
                            ((LivingEntity) event.getEntity()).damage(event.getDamage(), event.getDamager());
                            event.getEntity().setLastDamageCause(event);
                        }
                    }
                }
            }, 400L);
        }
        return true;
    }

    @Override
    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        if (attacking.getMana() >= getManaCost()) {
            for (Character character : fight.getCharacters()) {
                fight.setStatusTurns(character, StatusEffect.DOOM, 5);
            }
            fight.sendMessage(ChatColor.YELLOW + attacking.getName() + " doomed " + defending.getName() + ", causing them to become unconscious in 5 turns.");
            return true;
        } else {
            fight.sendMessage(ChatColor.YELLOW + (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " attempted to cast blink, but did not have enough mana.");
        }
        return false;
    }

    @Override
    public ItemStack getIcon(){
        ItemStack icon = new ItemStack(Material.FIREBALL);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Doom");
        icon.setItemMeta(meta);
        return icon;
    }

    @Override
    public boolean canUse(Character character)  {
        return character.getSkillPoints(SkillType.MAGIC_OFFENCE) >= 80;
    }

    @Override
    public String getDescription()  {
        return "Dooms the opponent, causing them to become unconscious in 5 turns.";}
}
