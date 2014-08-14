package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class ManipulationMagicSpecialisation extends SpecialisationBase {

    public ManipulationMagicSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Manipulation Magic";
    }

    @Override
    public int getTier() {
        return 2;
    }

}
