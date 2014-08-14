package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class NimbleSpecialisation extends SpecialisationBase {

    public NimbleSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Nimble";
    }

    @Override
    public int getTier() {
        return 4;
    }

}
