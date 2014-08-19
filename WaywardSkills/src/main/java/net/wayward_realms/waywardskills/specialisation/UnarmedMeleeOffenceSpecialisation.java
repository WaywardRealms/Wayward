package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class UnarmedMeleeOffenceSpecialisation extends SpecialisationBase {

    public UnarmedMeleeOffenceSpecialisation(Specialisation parent) {
        super(parent);
        addChildSpecialisation(new FisticuffsOffenceSpecialisation(this));
    }

    @Override
    public String getName() {
        return "Unarmed Melee Offence";
    }

    @Override
    public int getTier() {
        return 3;
    }

}
