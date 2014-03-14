package net.wayward_realms.waywardlib.combat;

/**
 * Represents a combat AIWaywardPlugin
 *
 */
public interface AI extends Combatant {

    /**
     * Processes a turn, setting all fields accordingly for the AI
     *
     * @param turn the turn to process
     */
    public void processTurn(Turn turn);

}
