package net.wayward_realms.waywardlib.donation;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a donator claims moneyWaywardPlugin
 *
 */
public class DonatorClaimMoneyEvent extends DonatorEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private int money;
    private boolean cancel;

    public DonatorClaimMoneyEvent(final Player donator, int money) {
        super(donator);
        this.money = money;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the money being given to the donator
     *
     * @return the amount of money being given to the donator
     */
    public int getMoney() {
        return money;
    }

    /**
     * Sets the amount of money to be given to the donator
     *
     * @param money the amount of money
     */
    public void setMoney(int money) {
        this.money = money;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

}
