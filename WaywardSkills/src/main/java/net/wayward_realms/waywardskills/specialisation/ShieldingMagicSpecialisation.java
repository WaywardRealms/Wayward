package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class ShieldingMagicSpecialisation extends SpecialisationBase {

    public ShieldingMagicSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Shielding Magic";
    }

    @Override
    public int getTier() {
        return 3;
    }

}
