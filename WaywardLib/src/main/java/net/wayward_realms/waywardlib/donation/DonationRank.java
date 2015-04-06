package net.wayward_realms.waywardlib.donation;

import net.wayward_realms.waywardlib.essentials.Kit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

/**
 * Represents a donation rankWaywardPlugin
 *
 */
public interface DonationRank extends ConfigurationSerializable {

    /**
     * Gets the name of the donation rank
     *
     * @return the name
     */
    public String getName();

    /**
     * Sets the name of the rank
     *
     * @param name the name to set
     */
    public void setName(String name);

    /**
     * Gets the kit for the donation rank
     *
     * @return the kit
     */
    public Kit getKit();

    /**
     * Sets the kit that can be obtained by this donation rank
     *
     * @param kit the kit to set
     */
    public void setKit(Kit kit);

    /**
     * Gets the money for the donation rank
     *
     * @return the money for the donation rank
     */
    public int getMoney();

    /**
     * Sets the money that can be obtained by this donation rank
     *
     * @param money the money to set
     */
    public void setMoney(int money);

    /**
     * Gets the levels for the donation rank
     *
     * @return the levels for the donation rank
     */
    public int getLevels();

    /**
     * Sets the levels that can be obtained by this donation rank
     *
     * @param levels the amount of levels to set
     */
    public void setLevels(int levels);

}
