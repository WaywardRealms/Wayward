package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.TemporaryStatModification;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.SpellBase;
import net.wayward_realms.waywardskills.WaywardSkills;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShieldBarrierSpell extends SpellBase {

    private WaywardSkills plugin;

    public ShieldBarrierSpell(WaywardSkills plugin) {
        this.plugin = plugin;
        setName("ShieldBarrier");
        setManaCost(20);
        setCoolDown(120);
    }

    @Override
    public boolean use(Player player) {
        World world = player.getWorld();
        int px = player.getLocation().getBlockX();
        int py = player.getLocation().getBlockY();
        int pz = player.getLocation().getBlockZ();
        for (int x = px - 4; x < px + 4; x++) {
            for (int y = py - 4; y < py + 4; y++) {
                for (int z = pz - 4; z < pz + 4; z++) {
                    int dx = px - x;
                    int dy = py - y;
                    int dz = pz - z;
                    int dxsq = dx * dx;
                    int dysq = dy * dy;
                    int dzsq = dz * dz;
                    int distsq = dxsq + dysq + dzsq;
                    if (distsq > 9 && distsq < 25) {
                        final Block block = world.getBlockAt(x, y, z);
                        final Material currentMaterial = block.getType();
                        block.setType(Material.GLASS);
                        long duration = 200L; // Duration ticks
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                block.setType(currentMaterial);
                            }
                        }, duration);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean use(Fight fight, Character attacking, final Character defending, ItemStack weapon) {
        if (attacking.getMana() >= getManaCost()) {
            defending.addTemporaryStatModification(TemporaryStatModification.MELEE_DEFENCE_UP_50);
            defending.addTemporaryStatModification(TemporaryStatModification.RANGED_DEFENCE_UP_50);
            defending.addTemporaryStatModification(TemporaryStatModification.MAGIC_DEFENCE_DOWN_50);
            fight.scheduleTask(new Runnable() {
                @Override
                public void run() {
                    defending.removeTemporaryStatModification(TemporaryStatModification.MELEE_DEFENCE_UP_50);
                    defending.removeTemporaryStatModification(TemporaryStatModification.RANGED_DEFENCE_UP_50);
                    defending.removeTemporaryStatModification(TemporaryStatModification.MAGIC_DEFENCE_UP_50);
                }
            }, 3);
            use(attacking.getPlayer().getPlayer());
            fight.sendMessage(ChatColor.YELLOW + (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " created a shield barrier over " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()));
            return true;
        } else {
            fight.sendMessage(ChatColor.YELLOW + (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " attempted to create a shield barrier, but did not have enough mana.");
            return false;
        }
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.GLASS);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Shield Barrier");
        icon.setItemMeta(meta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return hasScroll(character) && plugin.getSpecialisationValue(character, plugin.getSpecialisation("Shielding Magic")) >= 45;
    }

    @Override
    public String getDescription() {
        return "Prevents your party taking damage for 1 turn";
    }

}
