package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class DarkMagicSpecialisation extends SpecialisationBase {

    public DarkMagicSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Dark Magic";
    }

    @Override
    public int getTier() {
        return 4;
    }

}
