package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class HealingSpecialisation extends SpecialisationBase {

    public HealingSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Healing";
    }

    @Override
    public int getTier() {
        return 4;
    }

}
