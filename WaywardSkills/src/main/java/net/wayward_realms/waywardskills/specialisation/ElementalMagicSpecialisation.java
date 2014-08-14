package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class ElementalMagicSpecialisation extends SpecialisationBase {

    public ElementalMagicSpecialisation(Specialisation parent) {
        super(parent);
        addChildSpecialisation(new FireMagicSpecialisation(this));
        addChildSpecialisation(new WaterMagicSpecialisation(this));
        addChildSpecialisation(new LightningMagicSpecialisation(this));
        addChildSpecialisation(new WindMagicSpecialisation(this));
        addChildSpecialisation(new LightMagicSpecialisation(this));
        addChildSpecialisation(new DarkMagicSpecialisation(this));
    }

    @Override
    public String getName() {
        return "Elemental Magic";
    }

    @Override
    public int getTier() {
        return 3;
    }

}
