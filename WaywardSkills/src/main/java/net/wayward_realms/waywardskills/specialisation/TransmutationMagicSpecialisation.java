package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class TransmutationMagicSpecialisation extends SpecialisationBase {

    public TransmutationMagicSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Transmutation Magic";
    }

    @Override
    public int getTier() {
        return 2;
    }
}
