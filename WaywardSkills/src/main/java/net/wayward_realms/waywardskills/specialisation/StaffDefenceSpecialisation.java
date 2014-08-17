package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;
import org.bukkit.inventory.ItemStack;

public class StaffDefenceSpecialisation extends SpecialisationBase {

    public StaffDefenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Staff Defence";
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public boolean meetsDefenceRequirement(ItemStack item) {
        return item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().toLowerCase().contains("staff");
    }

}
