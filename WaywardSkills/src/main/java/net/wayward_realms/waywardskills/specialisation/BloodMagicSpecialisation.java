package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;
import org.bukkit.inventory.ItemStack;

public class BloodMagicSpecialisation extends SpecialisationBase {

    public BloodMagicSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Blood Magic";
    }

    @Override
    public int getTier() {
        return 3;
    }

    @Override
    public boolean meetsAttackRequirement(ItemStack item) {
        return true;
    }

    @Override
    public boolean meetsDefenceRequirement(ItemStack item) {
        return true;
    }
}
