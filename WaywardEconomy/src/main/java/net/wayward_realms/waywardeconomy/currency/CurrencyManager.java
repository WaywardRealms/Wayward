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
    private Map<Integer, Map<Currency, Integer>> money = new HashMap<>();

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
        if (money.get(character.getId()) != null) {
            if (money.get(character.getId()).get(currency) != null) {
                return money.get(character.getId()).get(currency);
            }
        }
        return currency.getDefaultAmount();
    }

    public void setMoney(Character character, Currency currency, int amount) {
        if (money.get(character.getId()) == null) {
            money.put(character.getId(), new HashMap<Currency, Integer>());
        }
        money.get(character.getId()).put(currency, amount);
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
        File characterDirectory = new File(plugin.getDataFolder(), "characters");
        if (characterDirectory.exists()) {
            for (File characterFile : characterDirectory.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.getName().endsWith(".yml");
                }
            })) {
                YamlConfiguration characterConfig = new YamlConfiguration();
                try {
                    characterConfig.load(characterFile);
                } catch (IOException | InvalidConfigurationException exception) {
                    exception.printStackTrace();
                }
                money.put(Integer.parseInt(characterFile.getName().replace(".yml", "")), new HashMap<Currency, Integer>());
                for (String currencyName : characterConfig.getConfigurationSection("currencies").getKeys(false)) {
                    money.get(Integer.parseInt(characterFile.getName().replace(".yml", ""))).put(plugin.getCurrency(currencyName), characterConfig.getInt("currencies." + currencyName));
                }
            }
        }
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
        File characterDirectory = new File(plugin.getDataFolder(), "characters");
        if (!characterDirectory.isDirectory()) {
            characterDirectory.delete();
        }
        if (!characterDirectory.exists()) {
            characterDirectory.mkdir();
        }
        for (Map.Entry<Integer, Map<Currency, Integer>> entry : money.entrySet()) {
            YamlConfiguration characterConfig = new YamlConfiguration();
            Map<String, Integer> currencies = new HashMap<>();
            for (Map.Entry<Currency,Integer> currencyEntry : entry.getValue().entrySet()) {
                currencies.put(currencyEntry.getKey().getName(), currencyEntry.getValue());
            }
            characterConfig.set("currencies", currencies);
            try {
                characterConfig.save(new File(characterDirectory, entry.getKey() + ".yml"));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
