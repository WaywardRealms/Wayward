package net.wayward_realms.waywardeconomy.auction;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.economy.Bid;

import java.util.*;

public class BidImpl implements Bid {

    private final Character character;
    private final int amount;
    private final Calendar time;

    public BidImpl(Character character, int amount) {
        this(character, amount, Calendar.getInstance());
    }

    public BidImpl(Character character, int amount, Calendar time) {
        this.character = character;
        this.amount = amount;
        this.time = time;
    }

    @Override
    public Character getCharacter() {
        return character;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public Date getTime() {
        return time.getTime();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("character", character.getId());
        serialised.put("amount", amount);
        serialised.put("time", time.getTimeInMillis());
        return serialised;
    }

    public static BidImpl deserialize(Map<String, Object> serialised) {
        Calendar time = new GregorianCalendar();
        time.setTimeInMillis((long) serialised.get("time"));
        return new BidImpl((Character) serialised.get("character"), (int) serialised.get("amount"), time);
    }

}
