package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class SwordOffenceSpecialisation extends SpecialisationBase {

    public SwordOffenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Sword Offence";
    }

    @Override
    public int getTier() {
        return 4;
    }

}
