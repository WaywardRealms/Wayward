package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

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

}
