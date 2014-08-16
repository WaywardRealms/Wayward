package net.wayward_realms.waywardlib.skills;

import org.bukkit.inventory.ItemStack;

public abstract class SpellBase extends SkillBase implements Spell {

    private int manaCost;

    @Override
    public int getManaCost() {
        return manaCost;
    }

    @Override
    public void setManaCost(int cost) {
        this.manaCost = cost;
    }

    public boolean hasScroll(net.wayward_realms.waywardlib.character.Character character) {
        for (ItemStack scroll : character.getEquipment().getScrolls()) {
            if (scroll.hasItemMeta()) {
                if (scroll.getItemMeta().hasLore()) {
                    String spellName = scroll.getItemMeta().getLore().get(0).substring(2, scroll.getItemMeta().getLore().get(0).length());
                    if (spellName.equalsIgnoreCase(getName())) return true;
                }
            }
        }
        return false;
    }

}
