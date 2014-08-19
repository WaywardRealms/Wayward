package net.wayward_realms.waywardlib.character;

import net.wayward_realms.waywardlib.skills.Stat;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a temporary modification to a stat
 */
public class TemporaryStatModification implements ConfigurationSerializable {
    
    public static final TemporaryStatModification MELEE_ATTACK_UP_25 = new TemporaryStatModification(Stat.MELEE_ATTACK, 1.25F);
    public static final TemporaryStatModification MELEE_ATTACK_UP_50 = new TemporaryStatModification(Stat.MELEE_ATTACK, 1.50F);
    public static final TemporaryStatModification MELEE_ATTACK_UP_75 = new TemporaryStatModification(Stat.MELEE_ATTACK, 1.75F);
    public static final TemporaryStatModification MELEE_ATTACK_UP_100 = new TemporaryStatModification(Stat.MELEE_ATTACK, 2.00F);
    
    public static final TemporaryStatModification MELEE_ATTACK_DOWN_25 = new TemporaryStatModification(Stat.MELEE_ATTACK, 0.75F);
    public static final TemporaryStatModification MELEE_ATTACK_DOWN_50 = new TemporaryStatModification(Stat.MELEE_ATTACK, 0.50F);
    public static final TemporaryStatModification MELEE_ATTACK_DOWN_75 = new TemporaryStatModification(Stat.MELEE_ATTACK, 0.25F);
    public static final TemporaryStatModification MELEE_ATTACK_DOWN_100 = new TemporaryStatModification(Stat.MELEE_ATTACK, 0.00F);
    
    public static final TemporaryStatModification MELEE_DEFENCE_UP_25 = new TemporaryStatModification(Stat.MELEE_DEFENCE, 1.25F);
    public static final TemporaryStatModification MELEE_DEFENCE_UP_50 = new TemporaryStatModification(Stat.MELEE_DEFENCE, 1.50F);
    public static final TemporaryStatModification MELEE_DEFENCE_UP_75 = new TemporaryStatModification(Stat.MELEE_DEFENCE, 1.75F);
    public static final TemporaryStatModification MELEE_DEFENCE_UP_100 = new TemporaryStatModification(Stat.MELEE_DEFENCE, 2.00F);
    
    public static final TemporaryStatModification MELEE_DEFENCE_DOWN_25 = new TemporaryStatModification(Stat.MELEE_DEFENCE, 0.75F);
    public static final TemporaryStatModification MELEE_DEFENCE_DOWN_50 = new TemporaryStatModification(Stat.MELEE_DEFENCE, 0.50F);
    public static final TemporaryStatModification MELEE_DEFENCE_DOWN_75 = new TemporaryStatModification(Stat.MELEE_DEFENCE, 0.25F);
    public static final TemporaryStatModification MELEE_DEFENCE_DOWN_100 = new TemporaryStatModification(Stat.MELEE_DEFENCE, 0.00F);

    public static final TemporaryStatModification RANGED_ATTACK_UP_25 = new TemporaryStatModification(Stat.RANGED_ATTACK, 1.25F);
    public static final TemporaryStatModification RANGED_ATTACK_UP_50 = new TemporaryStatModification(Stat.RANGED_ATTACK, 1.50F);
    public static final TemporaryStatModification RANGED_ATTACK_UP_75 = new TemporaryStatModification(Stat.RANGED_ATTACK, 1.75F);
    public static final TemporaryStatModification RANGED_ATTACK_UP_100 = new TemporaryStatModification(Stat.RANGED_ATTACK, 2.00F);

    public static final TemporaryStatModification RANGED_ATTACK_DOWN_25 = new TemporaryStatModification(Stat.RANGED_ATTACK, 0.75F);
    public static final TemporaryStatModification RANGED_ATTACK_DOWN_50 = new TemporaryStatModification(Stat.RANGED_ATTACK, 0.50F);
    public static final TemporaryStatModification RANGED_ATTACK_DOWN_75 = new TemporaryStatModification(Stat.RANGED_ATTACK, 0.25F);
    public static final TemporaryStatModification RANGED_ATTACK_DOWN_100 = new TemporaryStatModification(Stat.RANGED_ATTACK, 0.00F);

    public static final TemporaryStatModification RANGED_DEFENCE_UP_25 = new TemporaryStatModification(Stat.RANGED_DEFENCE, 1.25F);
    public static final TemporaryStatModification RANGED_DEFENCE_UP_50 = new TemporaryStatModification(Stat.RANGED_DEFENCE, 1.50F);
    public static final TemporaryStatModification RANGED_DEFENCE_UP_75 = new TemporaryStatModification(Stat.RANGED_DEFENCE, 1.75F);
    public static final TemporaryStatModification RANGED_DEFENCE_UP_100 = new TemporaryStatModification(Stat.RANGED_DEFENCE, 2.00F);

    public static final TemporaryStatModification RANGED_DEFENCE_DOWN_25 = new TemporaryStatModification(Stat.RANGED_DEFENCE, 0.75F);
    public static final TemporaryStatModification RANGED_DEFENCE_DOWN_50 = new TemporaryStatModification(Stat.RANGED_DEFENCE, 0.50F);
    public static final TemporaryStatModification RANGED_DEFENCE_DOWN_75 = new TemporaryStatModification(Stat.RANGED_DEFENCE, 0.25F);
    public static final TemporaryStatModification RANGED_DEFENCE_DOWN_100 = new TemporaryStatModification(Stat.RANGED_DEFENCE, 0.00F);

    public static final TemporaryStatModification MAGIC_ATTACK_UP_25 = new TemporaryStatModification(Stat.MAGIC_ATTACK, 1.25F);
    public static final TemporaryStatModification MAGIC_ATTACK_UP_50 = new TemporaryStatModification(Stat.MAGIC_ATTACK, 1.50F);
    public static final TemporaryStatModification MAGIC_ATTACK_UP_75 = new TemporaryStatModification(Stat.MAGIC_ATTACK, 1.75F);
    public static final TemporaryStatModification MAGIC_ATTACK_UP_100 = new TemporaryStatModification(Stat.MAGIC_ATTACK, 2.00F);

    public static final TemporaryStatModification MAGIC_ATTACK_DOWN_25 = new TemporaryStatModification(Stat.MAGIC_ATTACK, 0.75F);
    public static final TemporaryStatModification MAGIC_ATTACK_DOWN_50 = new TemporaryStatModification(Stat.MAGIC_ATTACK, 0.50F);
    public static final TemporaryStatModification MAGIC_ATTACK_DOWN_75 = new TemporaryStatModification(Stat.MAGIC_ATTACK, 0.25F);
    public static final TemporaryStatModification MAGIC_ATTACK_DOWN_100 = new TemporaryStatModification(Stat.MAGIC_ATTACK, 0.00F);

    public static final TemporaryStatModification MAGIC_DEFENCE_UP_25 = new TemporaryStatModification(Stat.MAGIC_DEFENCE, 1.25F);
    public static final TemporaryStatModification MAGIC_DEFENCE_UP_50 = new TemporaryStatModification(Stat.MAGIC_DEFENCE, 1.50F);
    public static final TemporaryStatModification MAGIC_DEFENCE_UP_75 = new TemporaryStatModification(Stat.MAGIC_DEFENCE, 1.75F);
    public static final TemporaryStatModification MAGIC_DEFENCE_UP_100 = new TemporaryStatModification(Stat.MAGIC_DEFENCE, 2.00F);

    public static final TemporaryStatModification MAGIC_DEFENCE_DOWN_25 = new TemporaryStatModification(Stat.MAGIC_DEFENCE, 0.75F);
    public static final TemporaryStatModification MAGIC_DEFENCE_DOWN_50 = new TemporaryStatModification(Stat.MAGIC_DEFENCE, 0.50F);
    public static final TemporaryStatModification MAGIC_DEFENCE_DOWN_75 = new TemporaryStatModification(Stat.MAGIC_DEFENCE, 0.25F);
    public static final TemporaryStatModification MAGIC_DEFENCE_DOWN_100 = new TemporaryStatModification(Stat.MAGIC_DEFENCE, 0.00F);

    public static final TemporaryStatModification SPEED_UP_25 = new TemporaryStatModification(Stat.SPEED, 1.25F);
    public static final TemporaryStatModification SPEED_UP_50 = new TemporaryStatModification(Stat.SPEED, 1.50F);
    public static final TemporaryStatModification SPEED_UP_75 = new TemporaryStatModification(Stat.SPEED, 1.75F);
    public static final TemporaryStatModification SPEED_UP_100 = new TemporaryStatModification(Stat.SPEED, 2.00F);

    public static final TemporaryStatModification SPEED_DOWN_25 = new TemporaryStatModification(Stat.SPEED, 0.75F);
    public static final TemporaryStatModification SPEED_DOWN_50 = new TemporaryStatModification(Stat.SPEED, 0.50F);
    public static final TemporaryStatModification SPEED_DOWN_75 = new TemporaryStatModification(Stat.SPEED, 0.25F);
    public static final TemporaryStatModification SPEED_DOWN_100 = new TemporaryStatModification(Stat.SPEED, 0.00F);

    private Stat stat;
    private float multiplier;

    public TemporaryStatModification(Stat stat, float multiplier) {
        this.stat = stat;
        this.multiplier = multiplier;
    }

    /**
     * Gets the stat to which this temporary stat buff applies
     *
     * @return the stat
     */
    public Stat getStat() {
        return stat;
    }

    /**
     * Sets the stat to which this temporary stat buff applies
     *
     * @param stat the stat to set
     */
    public void setStat(Stat stat) {
        this.stat = stat;
    }

    /**
     * Gets the multiplier to the stat
     *
     * @return the multiplier
     */
    public float getMultiplier() {
        return multiplier;
    }

    /**
     * Sets the multiplier to the stat
     *
     * @param multiplier the multiplier to set
     */
    public void setMultiplier(float multiplier) {
        this.multiplier = multiplier;
    }

    /**
     * Applies the stat buff to a stat
     * 
     * @param stat the stat to apply to
     * @param original the original value of the stat
     * @return the modified stat
     */
    public int apply(Stat stat, int original) {
        if (stat == getStat()) {
            return Math.round(original * getMultiplier());
        } else {
            return original;
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("stat", getStat().toString());
        serialised.put("multiplier", getMultiplier());
        return serialised;
    }

    public static TemporaryStatModification deserialize(Map<String, Object> serialised) {
        return new TemporaryStatModification(Stat.valueOf((String) serialised.get("stat")), (float) serialised.get("multiplier"));
    }

}
