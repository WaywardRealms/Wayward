package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class SwordMagicSpecialisation extends SpecialisationBase {

    public SwordMagicSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Sword Magic";
    }

    @Override
    public int getTier() {
        return 2;
    }

}
