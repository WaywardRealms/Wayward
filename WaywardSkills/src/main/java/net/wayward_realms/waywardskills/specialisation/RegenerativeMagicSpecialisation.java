package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class RegenerativeMagicSpecialisation extends SpecialisationBase {

    public RegenerativeMagicSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Regenerative Magic";
    }

    @Override
    public int getTier() {
        return 3;
    }

}
