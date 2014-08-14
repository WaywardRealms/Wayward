package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class ScytheDefenceSpecialisation extends SpecialisationBase {

    public ScytheDefenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Scythe Defence";
    }

    @Override
    public int getTier() {
        return 4;
    }

}
