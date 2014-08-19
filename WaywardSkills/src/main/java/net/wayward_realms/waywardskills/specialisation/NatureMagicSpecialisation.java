package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class NatureMagicSpecialisation extends SpecialisationBase {

    public NatureMagicSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Nature Magic";
    }

    @Override
    public int getTier() {
        return 2;
    }

}
