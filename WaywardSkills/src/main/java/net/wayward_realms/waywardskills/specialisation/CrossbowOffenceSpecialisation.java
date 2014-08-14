package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class CrossbowOffenceSpecialisation extends SpecialisationBase {

    public CrossbowOffenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Crossbow Offence";
    }

    @Override
    public int getTier() {
        return 4;
    }

}
