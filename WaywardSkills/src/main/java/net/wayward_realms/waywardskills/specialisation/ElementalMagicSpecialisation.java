package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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

    @Override
    public boolean meetsAttackRequirement(ItemStack item) {
        return true;
    }

    @Override
    public int getDamageRollBonus(ItemStack item) {
        if (item != null) {
            if (item.getType() == Material.STICK) return 4;
            if (item.getType() == Material.BLAZE_ROD) return 8;
        }
        return 0;
    }

}
