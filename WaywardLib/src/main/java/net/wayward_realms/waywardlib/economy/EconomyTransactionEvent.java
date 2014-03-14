package net.wayward_realms.waywardlib.economy;

import net.wayward_realms.waywardlib.character.Character;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when money is transferred from one player to another
 * @author Lucariatias
 *
 */
public class EconomyTransactionEvent extends EconomyEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final net.wayward_realms.waywardlib.character.Character from;
    private final Character to;
    private int amount;
    private boolean cancel;

    public EconomyTransactionEvent(final Character from, final Character to, int amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the character giving the money
     *
     * @return the character giving the money in the transaction
     */
    public Character getFrom() {
        return from;
    }

    /**
     * Gets the character recieving the money
     *
     * @return the character recieving the money in the transaction
     */
    public Character getTo() {
        return to;
    }

    /**
     * Gets the amount of money being transferred
     *
     * @return the amount of money being transferred
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets the amount of money being transferred
     *
     * @param amount the amount of money to set
     */
    public void setAmount(int amount) {
        this.amount = amount;
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
