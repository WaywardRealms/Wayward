package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class ScytheOffenceSpecialisation extends SpecialisationBase {

    public ScytheOffenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Sythe Offence";
    }

    @Override
    public int getTier() {
        return 4;
    }

}
