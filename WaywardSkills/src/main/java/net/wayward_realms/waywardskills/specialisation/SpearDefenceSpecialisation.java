package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class SpearDefenceSpecialisation extends SpecialisationBase {

    public SpearDefenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Spear Defence";
    }

    @Override
    public int getTier() {
        return 4;
    }

}
