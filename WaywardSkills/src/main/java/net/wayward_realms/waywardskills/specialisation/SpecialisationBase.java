package net.wayward_realms.waywardskills.specialisation;

import net.wayward_realms.waywardlib.skills.Specialisation;

import java.util.HashSet;
import java.util.Set;

public abstract class SpecialisationBase implements Specialisation {

    private Specialisation parent;
    private Set<Specialisation> children;

    public SpecialisationBase(Specialisation parent) {
        this.parent = parent;
        this.children = new HashSet<>();
    }

    @Override
    public Specialisation getParentSpecialisation() {
        return parent;
    }

    @Override
    public Set<Specialisation> getChildSpecialisations() {
        return children;
    }

    @Override
    public void addChildSpecialisation(Specialisation specialisation) {
        children.add(specialisation);
    }

}
