package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class SwordDefenceSpecialisation extends SpecialisationBase {

    public SwordDefenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Sword Defence";
    }

    @Override
    public int getTier() {
        return 4;
    }

}
