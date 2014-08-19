package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class BuffSpecialisation extends SpecialisationBase {

    public BuffSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Buff";
    }

    @Override
    public int getTier() {
        return 2;
    }
}
