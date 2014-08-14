package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class HammerDefenceSpecialisation extends SpecialisationBase {

    public HammerDefenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Hammer Defence";
    }

    @Override
    public int getTier() {
        return 4;
    }

}
