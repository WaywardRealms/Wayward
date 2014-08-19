package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;
import org.bukkit.inventory.ItemStack;

public class MagicDefenceSpecialisation extends SpecialisationBase {

    public MagicDefenceSpecialisation(Specialisation parent) {
        super(parent);
        addChildSpecialisation(new ShieldingMagicSpecialisation(this));
        addChildSpecialisation(new RegenerativeMagicSpecialisation(this));
    }

    @Override
    public String getName() {
        return "Magic Defence";
    }

    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public boolean meetsDefenceRequirement(ItemStack item) {
        return true;
    }

}
