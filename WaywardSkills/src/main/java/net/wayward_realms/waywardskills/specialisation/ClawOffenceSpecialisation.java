package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class ClawOffenceSpecialisation extends SpecialisationBase {

    public ClawOffenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Claw Offence";
    }

    @Override
    public int getTier() {
        return 4;
    }

}
