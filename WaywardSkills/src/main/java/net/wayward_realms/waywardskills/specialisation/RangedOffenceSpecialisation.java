package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class RangedOffenceSpecialisation extends SpecialisationBase {

    public RangedOffenceSpecialisation(Specialisation parent) {
        super(parent);
        addChildSpecialisation(new BowOffenceSpecialisation(this));
        addChildSpecialisation(new ThrowingAxeOffenceSpecialisation(this));
        addChildSpecialisation(new ThrowingKnifeOffenceSpecialisation(this));
        addChildSpecialisation(new DartOffenceSpecialisation(this));
    }

    @Override
    public String getName() {
        return "Ranged Offence";
    }

    @Override
    public int getTier() {
        return 2;
    }

}
