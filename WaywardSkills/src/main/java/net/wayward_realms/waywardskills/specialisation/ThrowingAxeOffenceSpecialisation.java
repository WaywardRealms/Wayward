package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

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
}
