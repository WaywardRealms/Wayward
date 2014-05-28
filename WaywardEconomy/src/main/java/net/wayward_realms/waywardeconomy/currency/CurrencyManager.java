package net.wayward_realms.waywardeconomy.currency;

import net.wayward_realms.waywardeconomy.WaywardEconomy;
import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.economy.Currency;
import net.wayward_realms.waywardlib.util.YamlFileFilter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CurrencyManager {

    private WaywardEconomy plugin;

    public CurrencyManager(WaywardEconomy plugin) {
        this.plugin = plugin;
    }

    public Currency getPrimaryCurrency() {
        return getCurrency(plugin.getConfig().getString("currency.primary"));
    }

    public void setPrimaryCurrency(Currency primaryCurrency) {
        plugin.getConfig().set("currency.primary", primaryCurrency.getName());
        plugin.saveConfig();
    }

    public Collection<? extends Currency> getCurrencies() {
        Set<Currency> currencies = new HashSet<>();
        File currencyDirectory = new File(plugin.getDataFolder(), "currency");
        for (File file : currencyDirectory.listFiles(new YamlFileFilter())) {
            YamlConfiguration currencyConfig = YamlConfiguration.loadConfiguration(file);
            currencies.add((Currency) currencyConfig.get("currency"));
        }
        return currencies;
    }

    public void removeCurrency(Currency currency) {
        File currencyDirectory = new File(plugin.getDataFolder(), "currency");
        File currencyFile = new File(currencyDirectory, currency.getName() + ".yml");
        currencyFile.delete();
    }

    public void addCurrency(Currency currency) {
        File currencyDirectory = new File(plugin.getDataFolder(), "currency");
        File currencyFile = new File(currencyDirectory, currency.getName() + ".yml");
        YamlConfiguration currencyConfig = new YamlConfiguration();
        currencyConfig.set("currency", currency);
        try {
            currencyConfig.save(currencyFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public Currency getCurrency(String name) {
        File currencyDirectory = new File(plugin.getDataFolder(), "currency");
        File currencyFile = new File(currencyDirectory, name + ".yml");
        YamlConfiguration currencyConfig = YamlConfiguration.loadConfiguration(currencyFile);
        return (Currency) currencyConfig.get("currency");
    }

    public int getMoney(Character character, Currency currency) {
        File characterDirectory = new File(plugin.getDataFolder(), "characters");
        File characterFile = new File(characterDirectory, character.getId() + ".yml");
        if (characterFile.exists()) {
            YamlConfiguration characterSave = YamlConfiguration.loadConfiguration(characterFile);
            return characterSave.getInt("currencies." + currency.getName(), currency.getDefaultAmount());
        }
        return currency.getDefaultAmount();
    }

    public void setMoney(Character character, Currency currency, int amount) {
        File characterDirectory = new File(plugin.getDataFolder(), "characters");
        File characterFile = new File(characterDirectory, character.getId() + ".yml");
        YamlConfiguration characterSave = YamlConfiguration.loadConfiguration(characterFile);
        characterSave.set("currencies." + currency.getName(), amount);
        try {
            characterSave.save(characterFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void addMoney(Character character, Currency currency, int amount) {
        setMoney(character, currency, getMoney(character, currency) + amount);
    }

    public void transferMoney(Character takeFrom, Character giveTo, Currency currency, int amount) {
        addMoney(takeFrom, currency, -amount);
        addMoney(giveTo, currency, amount);
    }

    public void loadState() {
        File currencyDirectory = new File(plugin.getDataFolder(), "currency");
        if (!currencyDirectory.exists() || currencyDirectory.listFiles(new YamlFileFilter()).length == 0) {
            Currency currency = new CurrencyImpl();
            currency.setDefaultAmount(50);
            currency.setName("gc");
            currency.setNameSingular("Gold coin");
            currency.setNamePlural("Gold coins");
            currency.setRate(100);
            addCurrency(currency);
        }
    }

    public void saveState() {
    }
}
