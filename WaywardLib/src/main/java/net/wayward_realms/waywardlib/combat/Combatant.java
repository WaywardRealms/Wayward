package net.wayward_realms.waywardlib.combat;

/**
 * Represents any participant in a fightWaywardPlugin
 *
 */
public interface Combatant {

    /**
     * Gets the name of the combatant
     *
     * @return the name
     */
    public String getName();

    /**
     * Gets the health of the combatant
     *
     * @return the health
     */
    public double getHealth();

    /**
     * Sets the health of the combatant
     *
     * @param health the health to set
     */
    public void setHealth(double health);

    /**
     * Gets the max health of the combatant
     *
     * @return the max health
     */
    public double getMaxHealth();

    /**
     * Gets whether the combatant is dead or not
     *
     * @return true if the character is dead, otherwise false
     */
    public boolean isDead();

    /**
     * Sets whether the combatant is dead or not
     *
     * @param dead Whether the combatant should be dead or not
     */
    public void setDead(boolean dead);

}
