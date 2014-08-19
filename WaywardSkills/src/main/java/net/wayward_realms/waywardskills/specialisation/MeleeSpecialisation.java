package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class MeleeSpecialisation extends SpecialisationBase {

    public MeleeSpecialisation(Specialisation parent) {
        super(parent);
        addChildSpecialisation(new MeleeOffenceSpecialisation(this));
        addChildSpecialisation(new MeleeDefenceSpecialisation(this));
    }

    @Override
    public String getName() {
        return "Melee";
    }

    @Override
    public int getTier() {
        return 1;
    }

}
