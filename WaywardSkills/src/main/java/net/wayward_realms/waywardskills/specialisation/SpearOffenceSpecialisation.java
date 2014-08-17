package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;
import org.bukkit.inventory.ItemStack;

public class SpearOffenceSpecialisation extends SpecialisationBase {

    public SpearOffenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Spear Offence";
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public boolean meetsAttackRequirement(ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().toLowerCase().contains("spear");
    }
}
