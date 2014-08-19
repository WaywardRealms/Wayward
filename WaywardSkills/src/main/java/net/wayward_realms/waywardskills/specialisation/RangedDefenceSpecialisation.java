package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class RangedDefenceSpecialisation extends SpecialisationBase {

    public RangedDefenceSpecialisation(Specialisation parent) {
        super(parent);
        addChildSpecialisation(new NimbleSpecialisation(this));
        addChildSpecialisation(new HealingSpecialisation(this));
        addChildSpecialisation(new ShieldDefenceSpecialisation(this));
    }

    @Override
    public String getName() {
        return "Ranged Defence";
    }

    @Override
    public int getTier() {
        return 2;
    }

}
