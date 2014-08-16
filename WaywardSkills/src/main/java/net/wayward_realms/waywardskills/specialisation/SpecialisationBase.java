package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

import java.util.HashSet;
import java.util.Set;

public abstract class SpecialisationBase implements Specialisation {

    private Set<Specialisation> parents;
    private Set<Specialisation> children;

    public SpecialisationBase(Specialisation parent) {
        this.parents = new HashSet<>();
        parents.add(parent);
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
}
