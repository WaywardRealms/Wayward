package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class ShieldDefenceSpecialisation extends SpecialisationBase {

    public ShieldDefenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Shield Defence";
    }

    @Override
    public int getTier() {
        return 4;
    }

}
