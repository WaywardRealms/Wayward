package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class MaceDefenceSpecialisation extends SpecialisationBase {

    public MaceDefenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Mace Defence";
    }

    @Override
    public int getTier() {
        return 4;
    }

}
