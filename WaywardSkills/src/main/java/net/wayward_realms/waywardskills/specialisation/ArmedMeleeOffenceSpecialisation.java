package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class ArmedMeleeOffenceSpecialisation extends SpecialisationBase {

    public ArmedMeleeOffenceSpecialisation(Specialisation parent) {
        super(parent);
        addChildSpecialisation(new SwordOffenceSpecialisation(this));
        addChildSpecialisation(new ScytheOffenceSpecialisation(this));
        addChildSpecialisation(new MaceOffenceSpecialisation(this));
        addChildSpecialisation(new FlailOffenceSpecialisation(this));
        addChildSpecialisation(new DaggerOffenceSpecialisation(this));
        addChildSpecialisation(new SpearOffenceSpecialisation(this));
        addChildSpecialisation(new StaffOffenceSpecialisation(this));
        addChildSpecialisation(new KnuckleOffenceSpecialisation(this));
        addChildSpecialisation(new ClawOffenceSpecialisation(this));
    }

    @Override
    public String getName() {
        return "Armed Melee Offence";
    }

    @Override
    public int getTier() {
        return 3;
    }

}
