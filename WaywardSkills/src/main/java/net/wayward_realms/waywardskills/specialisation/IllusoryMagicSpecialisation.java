package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;
import org.bukkit.inventory.ItemStack;

public class IllusoryMagicSpecialisation extends SpecialisationBase {

    public IllusoryMagicSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Illusory Magic";
    }

    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public boolean meetsDefenceRequirement(ItemStack item) {
        return true;
    }
}
