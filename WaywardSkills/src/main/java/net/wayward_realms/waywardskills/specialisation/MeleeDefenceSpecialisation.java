package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class MeleeDefenceSpecialisation extends SpecialisationBase {

    public MeleeDefenceSpecialisation(Specialisation parent) {
        super(parent);
        addChildSpecialisation(new ArmedMeleeDefenceSpecialisation(this));
        addChildSpecialisation(new UnarmedMeleeDefenceSpecialisation(this));
    }

    @Override
    public String getName() {
        return "Melee Defence";
    }

    @Override
    public int getTier() {
        return 2;
    }

}
