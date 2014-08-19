package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ThrowingAxeOffenceSpecialisation extends SpecialisationBase {
    public ThrowingAxeOffenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Throwing Axe Offence";
    }

    @Override
    public int getTier() {
        return 3;
    }

    @Override
    public boolean meetsAttackRequirement(ItemStack item) {
        return item != null && (item.getType() == Material.WOOD_AXE || item.getType() == Material.STONE_AXE || item.getType() == Material.IRON_AXE || item.getType() == Material.GOLD_AXE || item.getType() == Material.DIAMOND_AXE);
    }

    @Override
    public int getDamageRollBonus(ItemStack item) {
        if (item != null) {
            if (item.getType() == Material.WOOD_AXE) return 5;
            if (item.getType() == Material.STONE_AXE) return 6;
            if (item.getType() == Material.GOLD_AXE) return 5;
            if (item.getType() == Material.IRON_AXE) return 7;
            if (item.getType() == Material.DIAMOND_AXE) return 8;
        }
        return 0;
    }

}