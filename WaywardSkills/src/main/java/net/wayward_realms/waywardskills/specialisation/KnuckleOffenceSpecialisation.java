package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class KnuckleOffenceSpecialisation extends SpecialisationBase {

    public KnuckleOffenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Knuckle Offence";
    }

    @Override
    public int getTier() {
        return 4;
    }

}
