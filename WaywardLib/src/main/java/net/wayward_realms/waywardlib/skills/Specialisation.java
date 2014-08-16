package net.wayward_realms.waywardlib.skills;

import java.util.Set;

/**
 * Represents a specialisation
 *
 */
public interface Specialisation {

    /**
     * Gets the name of the specialisation
     *
     * @return the name of the specialisation
     */
    public String getName();

    public Set<Specialisation> getParentSpecialisations();

    /**
     * Gets the child specialisations
     *
     * @return the child specialisations
     */
    public Set<Specialisation> getChildSpecialisations();

    /**
     * Gets the specialisation tier
     * High tier specialisations are near the root of the tree and give percentage buffs to each of their children, but gain less when tiered up.
     * Low tier specialisations are further from the root of the tree, and have less children, but gain more when tiered up.
     *
     * @return the tier
     */
    public int getTier();

    /**
     * Adds a parent specialisation
     *
     * @param specialisation the specialisation to add
     */
    public void addParentSpecialisation(Specialisation specialisation);

    /**
     * Adds a child specialisation
     *
     * @param specialisation the specialisation to add
     */
    public void addChildSpecialisation(Specialisation specialisation);
}
