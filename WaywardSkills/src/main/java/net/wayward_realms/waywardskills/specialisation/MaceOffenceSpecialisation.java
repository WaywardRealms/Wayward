package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class MaceOffenceSpecialisation extends SpecialisationBase {

    public MaceOffenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Mace Offence";
    }

    @Override
    public int getTier() {
        return 4;
    }

}
