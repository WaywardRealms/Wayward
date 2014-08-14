package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class WindMagicSpecialisation extends SpecialisationBase {

    public WindMagicSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Wind Magic";
    }

    @Override
    public int getTier() {
        return 4;
    }

}
