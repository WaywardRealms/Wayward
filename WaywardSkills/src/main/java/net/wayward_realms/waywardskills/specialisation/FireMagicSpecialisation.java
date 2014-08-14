package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class FireMagicSpecialisation extends SpecialisationBase {

    public FireMagicSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Fire Magic";
    }

    @Override
    public int getTier() {
        return 4;
    }

}
