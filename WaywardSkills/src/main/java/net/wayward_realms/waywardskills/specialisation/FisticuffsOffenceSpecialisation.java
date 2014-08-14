package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class FisticuffsOffenceSpecialisation extends SpecialisationBase {

    public FisticuffsOffenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Fisticuffs Offence";
    }

    @Override
    public int getTier() {
        return 4;
    }

}
