package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class HammerOffenceSpecialisation extends SpecialisationBase {

    public HammerOffenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Hammer Offence";
    }

    @Override
    public int getTier() {
        return 4;
    }

}
