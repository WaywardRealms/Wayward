package net.wayward_realms.waywardeconomy.currency;


import net.wayward_realms.waywardlib.economy.Currency;

import java.util.HashMap;
import java.util.Map;

public class CurrencyImpl implements Currency {

    private String name;
    private String nameSingular;
    private String namePlural;
    private int rate;
    private int defaultAmount;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getNameSingular() {
        return nameSingular;
    }

    @Override
    public void setNameSingular(String name) {
        this.nameSingular = name;
    }

    @Override
    public String getNamePlural() {
        return namePlural;
    }

    @Override
    public void setNamePlural(String name) {
        this.namePlural = name;
    }

    @Override
    public int convert(int amount, Currency currency) {
        return (amount * getRate()) / currency.getRate();
    }

    @Override
    public int getRate() {
        return rate;
    }

    @Override
    public void setRate(int rate) {
        this.rate = rate;
    }

    @Override
    public int getDefaultAmount() {
        return defaultAmount;
    }

    @Override
    public void setDefaultAmount(int amount) {
        this.defaultAmount = amount;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("name", name);
        serialised.put("name-singular", nameSingular);
        serialised.put("name-plural", namePlural);
        serialised.put("rate", rate);
        serialised.put("default-amount", defaultAmount);
        return serialised;
    }

    public static CurrencyImpl deserialize(Map<String, Object> serialised) {
        CurrencyImpl deserialised = new CurrencyImpl();
        deserialised.name = (String) serialised.get("name");
        deserialised.nameSingular = (String) serialised.get("name-singular");
        deserialised.namePlural = (String) serialised.get("name-plural");
        deserialised.rate = (int) serialised.get("rate");
        deserialised.defaultAmount = (int) serialised.get("default-amount");
        return deserialised;
    }

}
