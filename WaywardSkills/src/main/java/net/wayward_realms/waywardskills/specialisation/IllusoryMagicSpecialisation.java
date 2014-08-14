package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class IllusoryMagicSpecialisation extends SpecialisationBase {

    public IllusoryMagicSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Illusory Magic";
    }

    @Override
    public int getTier() {
        return 2;
    }

}
