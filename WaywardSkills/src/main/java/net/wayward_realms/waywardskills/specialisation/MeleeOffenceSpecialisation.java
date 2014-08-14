package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class MeleeOffenceSpecialisation extends SpecialisationBase {

    public MeleeOffenceSpecialisation(Specialisation parent) {
        super(parent);
        addChildSpecialisation(new ArmedMeleeOffenceSpecialisation(this));
        addChildSpecialisation(new UnarmedMeleeOffenceSpecialisation(this));
    }

    @Override
    public String getName() {
        return "Melee Offence";
    }

    @Override
    public int getTier() {
        return 2;
    }

}
