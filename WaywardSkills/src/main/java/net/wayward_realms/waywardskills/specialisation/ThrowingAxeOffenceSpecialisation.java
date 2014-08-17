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

}
