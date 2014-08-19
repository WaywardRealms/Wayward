package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class UnarmedMeleeDefenceSpecialisation extends SpecialisationBase {

    public UnarmedMeleeDefenceSpecialisation(Specialisation parent) {
        super(parent);
        addChildSpecialisation(new NimbleSpecialisation(this));
        addChildSpecialisation(new HealingSpecialisation(this));
        addChildSpecialisation(new UnarmedBlockingSpecialisation(this));
    }

    @Override
    public String getName() {
        return "Unarmed Melee Defence";
    }

    @Override
    public int getTier() {
        return 3;
    }

}
