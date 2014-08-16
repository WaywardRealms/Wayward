package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class ProfessionSpecialisation extends SpecialisationBase {

    public ProfessionSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Profession";
    }

    @Override
    public int getTier() {
        return 1;
    }

}
