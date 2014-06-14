package net.wayward_realms.waywardlib.combat;

/**
 * Represents a status effect
 */
public enum StatusEffect {

    /**
     * Causes poison damage over time
     */
    POISON,

    /**
     * Causes the victim to be unable to move, preventing use of melee or ranged skills
     */
    PARALYSIS,

    /**
     * Causes burn damage over time
     */
    BURNED,

    /**
     * Causes the victim to be frozen solid, unable to move
     */
    FROZEN,

    /**
     * Causes the victim's attempted move to change randomly, including random skill/spell use and random targets
     */
    CONFUSED,

    /**
     * Causes the victim to be unable to attack, wakes if hit by a melee or ranged attack
     */
    ASLEEP,

    /**
     * Causes hit chance to decrease dramatically
     */
    BLIND,

    /**
     * Causes the character to be knocked out after four turns
     */
    DOOM,

    /**
     * Causes inability to cast spells
     */
    SILENCED

}
