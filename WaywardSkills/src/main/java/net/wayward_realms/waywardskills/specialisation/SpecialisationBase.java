package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public abstract class SpecialisationBase implements Specialisation {

    private Set<Specialisation> parents;
    private Set<Specialisation> children;

    public SpecialisationBase(Specialisation parent) {
        this.parents = new HashSet<>();
        if (parent != null) parents.add(parent);
        this.children = new HashSet<>();
    }

    @Override
    public Set<Specialisation> getParentSpecialisations() {
        return parents;
    }

    @Override
    public Set<Specialisation> getChildSpecialisations() {
        return children;
    }

    @Override
    public void addChildSpecialisation(Specialisation specialisation) {
        children.add(specialisation);
    }

    @Override
    public void addParentSpecialisation(Specialisation specialisation) {
        parents.add(specialisation);
    }

    @Override
    public boolean meetsAttackRequirement(ItemStack item) {
        return false;
    }

    @Override
    public boolean meetsDefenceRequirement(ItemStack item) {
        return false;
    }

    @Override
    public int getDamageRollBonus(ItemStack item) {
        return 0;
    }



}
