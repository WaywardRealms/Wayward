package net.wayward_realms.waywardskills.specialisation;

public class RootSpecialisation extends SpecialisationBase {

    public RootSpecialisation() {
        super(null);
        addChildSpecialisation(new MeleeSpecialisation(this));
        addChildSpecialisation(new RangedSpecialisation(this));
        addChildSpecialisation(new MagicSpecialisation(this));
        addChildSpecialisation(new MechanicsSpecialisation(this));
        addChildSpecialisation(new EscapeArtistSpecialisation(this));
        addChildSpecialisation(new LockpickSpecialisation(this));
        addChildSpecialisation(new ScribeSpecialisation(this));
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
