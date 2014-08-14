package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class DartOffenceSpecialisation extends SpecialisationBase {

    public DartOffenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Dart Offence";
    }

    @Override
    public int getTier() {
        return 3;
    }

}
