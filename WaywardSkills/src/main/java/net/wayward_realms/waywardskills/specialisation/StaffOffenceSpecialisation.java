package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class StaffOffenceSpecialisation extends SpecialisationBase {

    public StaffOffenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Staff Offence";
    }

    @Override
    public int getTier() {
        return 4;
    }

}
