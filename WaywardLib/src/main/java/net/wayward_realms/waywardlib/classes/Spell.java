package net.wayward_realms.waywardlib.classes;

public interface Spell extends Skill {

    /**
     * Gets the mana cost
     *
     * @return the mana cost for the spell
     */
    public int getManaCost();

    /**
     * Sets the mana cost
     *
     * @param cost the cost to set
     */
    public void setManaCost(int cost);

}
