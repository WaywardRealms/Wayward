package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;
import org.bukkit.inventory.ItemStack;

public class SummoningMagicSpecialisation extends SpecialisationBase {

    public SummoningMagicSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Summoning Magic";
    }

    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public boolean meetsAttackRequirement(ItemStack item) {
        return true;
    }

}
