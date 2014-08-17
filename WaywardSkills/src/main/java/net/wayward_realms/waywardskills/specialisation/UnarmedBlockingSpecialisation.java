package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class UnarmedBlockingSpecialisation extends SpecialisationBase {

    public UnarmedBlockingSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Unarmed Blocking";
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public boolean meetsDefenceRequirement(ItemStack item) {
        return item == null || item.getType() == Material.AIR;
    }
}
