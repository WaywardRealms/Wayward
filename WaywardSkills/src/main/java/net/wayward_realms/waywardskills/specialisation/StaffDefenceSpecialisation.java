package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class StaffDefenceSpecialisation extends SpecialisationBase {

    public StaffDefenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Staff Defence";
    }

    @Override
    public int getTier() {
        return 4;
    }
}
