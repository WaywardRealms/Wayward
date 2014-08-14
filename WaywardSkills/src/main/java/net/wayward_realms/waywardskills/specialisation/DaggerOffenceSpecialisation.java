package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class DaggerOffenceSpecialisation extends SpecialisationBase {

    public DaggerOffenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Dagger Offence";
    }

    @Override
    public int getTier() {
        return 4;
    }

}
