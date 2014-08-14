package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class UnarmedBlockingSpecialisation extends SpecialisationBase {

    public UnarmedBlockingSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Unarmed Blocking";
    }

    @Override
    public int getTier() {
        return 4;
    }

}
