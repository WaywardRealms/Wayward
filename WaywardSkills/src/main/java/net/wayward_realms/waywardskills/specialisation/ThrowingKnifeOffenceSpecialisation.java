package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ThrowingKnifeOffenceSpecialisation extends SpecialisationBase {

    public ThrowingKnifeOffenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Throwing Knife Offence";
    }

    @Override
    public int getTier() {
        return 3;
    }

    @Override
    public boolean meetsAttackRequirement(ItemStack item) {
        return item != null && (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().toLowerCase().contains("knife"));
    }

    @Override
    public int getDamageRollBonus(ItemStack item) {
        if (item != null) {
            if (item.getType() == Material.WOOD_SWORD) return 5;
            if (item.getType() == Material.STONE_SWORD) return 6;
            if (item.getType() == Material.GOLD_SWORD) return 5;
            if (item.getType() == Material.IRON_SWORD) return 7;
            if (item.getType() == Material.DIAMOND_SWORD) return 8;
        }
        return 0;
    }

}
