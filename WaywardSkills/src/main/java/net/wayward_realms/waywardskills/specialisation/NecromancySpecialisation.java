package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class NecromancySpecialisation extends SpecialisationBase {

    public NecromancySpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Necromancy";
    }

    @Override
    public int getTier() {
        return 2;
    }

}
