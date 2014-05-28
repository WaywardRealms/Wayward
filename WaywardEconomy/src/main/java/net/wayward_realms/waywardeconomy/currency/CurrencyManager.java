package net.wayward_realms.waywardeconomy.currency;

import net.wayward_realms.waywardeconomy.WaywardEconomy;
import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.economy.Currency;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CurrencyManager {

    private WaywardEconomy plugin;
    private Currency primaryCurrency;
    private Map<String, Currency> currencies = new HashMap<>();

    public CurrencyManager(WaywardEconomy plugin) {
        this.plugin = plugin;
    }

    public Currency getPrimaryCurrency() {
        return primaryCurrency;
    }

    public void setPrimaryCurrency(Currency primaryCurrency) {
        this.primaryCurrency = primaryCurrency;
    }

    public Collection<? extends Currency> getCurrencies() {
        return currencies.values();
    }

    public void removeCurrency(Currency currency) {
        currencies.remove(currency.getName());
    }

    public void addCurrency(Currency currency) {
        currencies.put(currency.getName(), currency);
    }

    public Currency getCurrency(String name) {
        return currencies.get(name);
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
        if (currencyDirectory.exists()) {
            for (File currencyFile : currencyDirectory.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.getName().endsWith(".yml");
                }
            })) {
                YamlConfiguration currencyConfig = new YamlConfiguration();
                try {
                    currencyConfig.load(currencyFile);
                } catch (IOException | InvalidConfigurationException exception) {
                    exception.printStackTrace();
                }
                Currency currency = (Currency) currencyConfig.get("currency");
                currencies.put(currency.getName(), currency);
            }
        }
        if (currencies.isEmpty()) {
            Currency currency = new CurrencyImpl();
            currency.setDefaultAmount(50);
            currency.setName("gc");
            currency.setNameSingular("Gold coin");
            currency.setNamePlural("Gold coins");
            currency.setRate(100);
            currencies.put(currency.getName(), currency);
        }
        setPrimaryCurrency(getCurrency(plugin.getConfig().getString("currency.primary")));
    }

    public void saveState() {
        File currencyDirectory = new File(plugin.getDataFolder(), "currency");
        if (!currencyDirectory.isDirectory()) {
            currencyDirectory.delete();
        }
        if (!currencyDirectory.exists()) {
            currencyDirectory.mkdir();
        }
        for (Currency currency : currencies.values()) {
            YamlConfiguration currencyConfig = new YamlConfiguration();
            currencyConfig.set("currency", currency);
            try {
                currencyConfig.save(new File(currencyDirectory, currency.getName() + ".yml"));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
