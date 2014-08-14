package net.wayward_realms.waywardskills.specialisation;

public class RootSpecialisation extends SpecialisationBase {

    public RootSpecialisation() {
        super(null);
        addChildSpecialisation(new MeleeSpecialisation(this));
        addChildSpecialisation(new RangedSpecialisation(this));
        addChildSpecialisation(new MagicSpecialisation(this));
        addChildSpecialisation(new MechanicsSpecialisation(this));
    }

    @Override
    public String getName() {
        return "Root";
    }

    @Override
    public int getTier() {
        return 0;
    }

}
