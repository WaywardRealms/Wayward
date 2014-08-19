package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class LockpickSpecialisation extends SpecialisationBase {

    public LockpickSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Lockpick";
    }

    @Override
    public int getTier() {
        return 1;
    }

}
