package net.wayward_realms.waywardeconomy.auction;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.economy.Auction;
import net.wayward_realms.waywardlib.economy.Bid;
import net.wayward_realms.waywardlib.economy.Currency;
import net.wayward_realms.waywardlib.economy.EconomyPlugin;
import net.wayward_realms.waywardlib.util.serialisation.SerialisableLocation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.*;

public class AuctionImpl implements Auction {

    private ItemStack item;
    private Currency currency;
    private Location location;
    private Character character;
    private List<Bid> bids = new ArrayList<>();
    private int minimumBidIncrement;
    private boolean biddingOpen;

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public void setItem(ItemStack item) {
        this.item = item;
    }

    @Override
    public Currency getCurrency() {
        return currency;
    }

    @Override
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public Character getCharacter() {
        return character;
    }

    @Override
    public void setCharacter(Character character) {
        this.character = character;
    }

    @Override
    public Bid getHighestBid() {
        return bids.isEmpty() ? null : bids.get(bids.size() - 1);
    }

    @Override
    public Collection<? extends Bid> getBids() {
        return bids;
    }

    @Override
    public void removeBid(Bid bid) {
        bids.remove(bid);
    }

    @Override
    public void addBid(Bid bid) {
        bids.add(bid);
    }

    @Override
    public int getMinimumBidIncrement() {
        return minimumBidIncrement;
    }

    @Override
    public void setMinimumBidIncrement(int minBidIncrement) {
        this.minimumBidIncrement = minBidIncrement;
    }

    @Override
    public void openBidding() {
        this.biddingOpen = true;
    }

    @Override
    public void closeBidding() {
        this.biddingOpen = false;
        EconomyPlugin plugin = Bukkit.getServicesManager().getRegistration(EconomyPlugin.class).getProvider();
        plugin.transferMoney(getHighestBid().getCharacter(), getCharacter(), getHighestBid().getAmount());
        getHighestBid().getCharacter().getPlayer().getPlayer().getInventory().addItem(getItem());
        getHighestBid().getCharacter().getPlayer().getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "You won " + (getCharacter().isNameHidden() ? ChatColor.MAGIC + getCharacter().getName() + ChatColor.RESET : getCharacter().getName()) + "'s auction. Here is your " + getItem().getType().toString().toLowerCase().replace('_', ' '));
    }

    @Override
    public boolean isBiddingOpen() {
        return biddingOpen;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("item", item);
        serialised.put("currency", currency.getName());
        serialised.put("location", new SerialisableLocation(location));
        serialised.put("character", character.getId());
        serialised.put("bids", bids);
        serialised.put("minimum-bid-increment", minimumBidIncrement);
        return serialised;
    }

    @SuppressWarnings("unchecked")
    public static AuctionImpl deserialize(Map<String, Object> serialised) {
        EconomyPlugin plugin = Bukkit.getServicesManager().getRegistration(EconomyPlugin.class).getProvider();
        RegisteredServiceProvider<CharacterPlugin> characterProvider = Bukkit.getServicesManager().getRegistration(CharacterPlugin.class);
        AuctionImpl deserialised = new AuctionImpl();
        deserialised.item = (ItemStack) serialised.get("item");
        deserialised.currency = plugin.getCurrency((String) serialised.get("currency"));
        deserialised.location = ((SerialisableLocation) serialised.get("location")).toLocation();
        if (characterProvider != null) {
            deserialised.character = characterProvider.getProvider().getCharacter((int) serialised.get("character"));
        }
        deserialised.bids = (List<Bid>) serialised.get("bids");
        deserialised.minimumBidIncrement = (int) serialised.get("minimum-bid-increment");
        return deserialised;
    }
}
