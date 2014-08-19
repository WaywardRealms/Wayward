package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;
import org.bukkit.inventory.ItemStack;

public class ShortbowOffenceSpecialisation extends SpecialisationBase {

    public ShortbowOffenceSpecialisation(Specialisation parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "Shortbow Offence";
    }

    @Override
    public int getTier() {
        return 0;
    }

    @Override
    public boolean meetsAttackRequirement(ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().toLowerCase().contains("shortbow");
    }

    @Override
    public int getDamageRollBonus(ItemStack item) {
        if (item != null) {
            if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().toLowerCase().contains("shortbow")) return 5;
        }
        return 0;
    }
}