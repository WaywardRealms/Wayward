package net.wayward_realms.waywardlib.donation;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

/**
 * Called when a donator claims items
 * @author Lucariatias
 *
 */
public class DonatorClaimItemsEvent extends DonatorEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private Collection<ItemStack> items;
    private boolean cancel;

    public DonatorClaimItemsEvent(final Player donator, Collection<ItemStack> items) {
        super(donator);
        this.items = items;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the items being given to the donator
     *
     * @return a collection containing the items being given to the donator
     */
    public Collection<ItemStack> getItems() {
        return items;
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
