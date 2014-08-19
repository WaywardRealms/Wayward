package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ScytheOffenceSpecialisation extends SpecialisationBase {

    public ScytheOffenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Sythe Offence";
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public boolean meetsAttackRequirement(ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().toLowerCase().contains("scythe");
    }

    @Override
    public int getDamageRollBonus(ItemStack item) {
        if (item != null) {
            if (item.getType() == Material.WOOD_HOE) return 5;
            if (item.getType() == Material.STONE_HOE) return 6;
            if (item.getType() == Material.GOLD_HOE) return 5;
            if (item.getType() == Material.IRON_HOE) return 7;
            if (item.getType() == Material.DIAMOND_HOE) return 8;
        }
        return 0;
    }
}
