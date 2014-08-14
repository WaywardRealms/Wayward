package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class ArmedMeleeDefenceSpecialisation extends SpecialisationBase {

    public ArmedMeleeDefenceSpecialisation(Specialisation parent) {
        super(parent);
        addChildSpecialisation(new ShieldDefenceSpecialisation(this));
        addChildSpecialisation(new SwordDefenceSpecialisation(this));
        addChildSpecialisation(new ScytheDefenceSpecialisation(this));
        addChildSpecialisation(new MaceDefenceSpecialisation(this));
        addChildSpecialisation(new SpearDefenceSpecialisation(this));
        addChildSpecialisation(new StaffDefenceSpecialisation(this));
    }

    @Override
    public String getName() {
        return "Armed Melee Defence";
    }

    @Override
    public int getTier() {
        return 3;
    }

}
