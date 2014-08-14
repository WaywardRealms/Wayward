package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

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

}
