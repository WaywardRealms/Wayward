package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class FlailOffenceSpecialisation extends SpecialisationBase {

    public FlailOffenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Flail Offence";
    }

    @Override
    public int getTier() {
        return 4;
    }

}
