package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class MechanicsSpecialisation extends SpecialisationBase {

    public MechanicsSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Mechanics";
    }

    @Override
    public int getTier() {
        return 1;
    }

}
