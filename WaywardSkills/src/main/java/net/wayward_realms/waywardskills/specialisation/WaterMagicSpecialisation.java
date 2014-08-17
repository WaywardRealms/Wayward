package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;
import org.bukkit.inventory.ItemStack;

public class WaterMagicSpecialisation extends SpecialisationBase {

    public WaterMagicSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Water Magic";
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public boolean meetsAttackRequirement(ItemStack item) {
        return true;
    }

}
