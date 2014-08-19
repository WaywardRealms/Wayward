package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

public class MagicSpecialisation extends SpecialisationBase {

    public MagicSpecialisation(Specialisation parent) {
        super(parent);
        addChildSpecialisation(new MagicOffenceSpecialisation(this));
        addChildSpecialisation(new MagicDefenceSpecialisation(this));
        addChildSpecialisation(new IllusoryMagicSpecialisation(this));
        addChildSpecialisation(new SummoningMagicSpecialisation(this));
        addChildSpecialisation(new NecromancySpecialisation(this));
        addChildSpecialisation(new EnchantmentMagicSpecialisation(this));
        addChildSpecialisation(new ManipulationMagicSpecialisation(this));
        addChildSpecialisation(new TransmutationMagicSpecialisation(this));
        addChildSpecialisation(new BuffSpecialisation(this));
        addChildSpecialisation(new NatureMagicSpecialisation(this));
        addChildSpecialisation(new SwordMagicSpecialisation(this));
    }

    @Override
    public String getName() {
        return "Magic";
    }

    @Override
    public int getTier() {
        return 1;
    }

}
