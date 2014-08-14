package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class LightningMagicSpecialisation extends SpecialisationBase {

    public LightningMagicSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Lightning Magic";
    }

    @Override
    public int getTier() {
        return 4;
    }

}
