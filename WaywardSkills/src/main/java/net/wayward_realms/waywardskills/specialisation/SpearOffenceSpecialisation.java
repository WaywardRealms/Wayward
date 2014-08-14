package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class SpearOffenceSpecialisation extends SpecialisationBase {

    public SpearOffenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Spear Offence";
    }

    @Override
    public int getTier() {
        return 4;
    }

}
