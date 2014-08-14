package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class MagicOffenceSpecialisation extends SpecialisationBase {

    public MagicOffenceSpecialisation(Specialisation parent) {
        super(parent);
        addChildSpecialisation(new ElementalMagicSpecialisation(this));
        addChildSpecialisation(new BloodMagicSpecialisation(this));
    }

    @Override
    public String getName() {
        return "Magic Offence";
    }

    @Override
    public int getTier() {
        return 2;
    }

}
