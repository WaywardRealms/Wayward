package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;
import org.bukkit.inventory.ItemStack;

public class ShieldDefenceSpecialisation extends SpecialisationBase {

    public ShieldDefenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Shield Defence";
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public boolean meetsDefenceRequirement(ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().toLowerCase().contains("shield");
    }

}
