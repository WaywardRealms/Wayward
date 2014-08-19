package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class RangedSpecialisation extends SpecialisationBase {

    public RangedSpecialisation(Specialisation parent) {
        super(parent);
        addChildSpecialisation(new RangedOffenceSpecialisation(this));
        addChildSpecialisation(new RangedDefenceSpecialisation(this));
    }

    @Override
    public String getName() {
        return "Ranged";
    }

    @Override
    public int getTier() {
        return 1;
    }

}
