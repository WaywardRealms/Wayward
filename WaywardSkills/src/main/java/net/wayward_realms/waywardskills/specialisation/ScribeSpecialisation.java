package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class ScribeSpecialisation extends SpecialisationBase {

    public ScribeSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Scribe";
    }

    @Override
    public int getTier() {
        return 1;
    }

}
