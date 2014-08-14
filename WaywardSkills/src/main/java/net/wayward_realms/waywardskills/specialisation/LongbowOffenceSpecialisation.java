package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class LongbowOffenceSpecialisation extends SpecialisationBase {

    public LongbowOffenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Longbow Offence";
    }

    @Override
    public int getTier() {
        return 4;
    }

}
