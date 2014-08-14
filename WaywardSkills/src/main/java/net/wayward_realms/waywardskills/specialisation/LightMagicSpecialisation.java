package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class LightMagicSpecialisation extends SpecialisationBase {

    public LightMagicSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Light Magic";
    }

    @Override
    public int getTier() {
        return 4;
    }

}
