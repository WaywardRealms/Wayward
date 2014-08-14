package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class EnchantmentMagicSpecialisation extends SpecialisationBase {

    public EnchantmentMagicSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Enchantment Magic";
    }

    @Override
    public int getTier() {
        return 2;
    }

}
