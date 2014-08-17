package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;
import org.bukkit.inventory.ItemStack;

public class MagicOffenceSpecialisation extends SpecialisationBase {

    public MagicOffenceSpecialisation(Specialisation parent) {
        super(parent);
        addChildSpecialisation(new ElementalMagicSpecialisation(this));
        addChildSpecialisation(new BloodMagicSpecialisation(this));
    }

    @Override
    public String getName() {
        return "Magic Offence";
    }

    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public boolean meetsAttackRequirement(ItemStack item) {
        return true;
    }

}
