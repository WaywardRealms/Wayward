package net.wayward_realms.waywardeconomy;

import net.wayward_realms.waywardeconomy.auction.AuctionImpl;
import net.wayward_realms.waywardeconomy.auction.AuctionManager;
import net.wayward_realms.waywardeconomy.auction.BidImpl;
import net.wayward_realms.waywardeconomy.currency.CurrencyImpl;
import net.wayward_realms.waywardeconomy.currency.CurrencyManager;
import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.economy.Auction;
import net.wayward_realms.waywardlib.economy.Currency;
import net.wayward_realms.waywardlib.economy.EconomyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WaywardEconomy extends JavaPlugin implements EconomyPlugin {

    private CharacterPlugin characterPlugin;

    private CurrencyManager currencyManager;
    private AuctionManager auctionManager;

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(AuctionImpl.class);
        ConfigurationSerialization.registerClass(BidImpl.class);
        ConfigurationSerialization.registerClass(CurrencyImpl.class);
        saveDefaultConfig();
        currencyManager = new CurrencyManager(this);
        auctionManager = new AuctionManager(this);
        getCommand("money").setExecutor(new MoneyCommand(this));
        getCommand("wallet").setExecutor(new WalletCommand(this));
        getCommand("auction").setExecutor(new AuctionCommand(this));
        getCommand("bid").setExecutor(new BidCommand(this));
        registerListeners(new SignChangeListener(this), new PlayerInteractListener(this), new InventoryClickListener(this), new InventoryCloseListener(this));
    }

    @Override
    public void onDisable() {
        saveState();
    }

    public CharacterPlugin getCharacterPlugin() {
        return characterPlugin;
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            this.getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    @Override
    public String getPrefix() {
        //return "" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "|" + ChatColor.RESET + ChatColor.BLUE + "WaywardEconomy" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "| " + ChatColor.RESET;
        return "";
    }

    @Override
    public void loadState() {
        RegisteredServiceProvider<CharacterPlugin> characterProvider = getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterProvider != null) {
            characterPlugin = characterProvider.getProvider();
        }
        currencyManager.loadState();
        auctionManager.loadState();
    }

    @Override
    public void saveState() {
        currencyManager.saveState();
        auctionManager.saveState();
    }

    @Override
    public int getMoney(OfflinePlayer player) {
        CharacterPlugin characterPlugin = getServer().getServicesManager().getRegistration(CharacterPlugin.class).getProvider();
        return getMoney(characterPlugin.getActiveCharacter(player));
    }

    @Override
    public void setMoney(OfflinePlayer player, int amount) {
        CharacterPlugin characterPlugin = getServer().getServicesManager().getRegistration(CharacterPlugin.class).getProvider();
        setMoney(characterPlugin.getActiveCharacter(player), amount);
    }

    @Override
    public void addMoney(OfflinePlayer player, int amount) {
        setMoney(player, getMoney(player) + amount);
    }

    @Override
    public void transferMoney(OfflinePlayer takeFrom, OfflinePlayer giveTo, int amount) {
        setMoney(takeFrom, getMoney(takeFrom) - amount);
        setMoney(giveTo, getMoney(giveTo) + amount);
    }

    @Override
    public int getMoney(Character character) {
        return getMoney(character, getPrimaryCurrency());
    }

    @Override
    public void setMoney(Character character, int amount) {
        setMoney(character, getPrimaryCurrency(), amount);
    }

    @Override
    public void addMoney(Character character, int amount) {
        setMoney(character, getMoney(character) + amount);
    }

    @Override
    public void transferMoney(Character takeFrom, Character giveTo, int amount) {
        setMoney(takeFrom, getMoney(takeFrom) - amount);
        setMoney(giveTo, getMoney(giveTo) + amount);
    }

    @Override
    public Currency getPrimaryCurrency() {
        return currencyManager.getPrimaryCurrency();
    }

    @Override
    public void setPrimaryCurrency(Currency currency) {
        currencyManager.setPrimaryCurrency(currency);
    }

    @Override
    public Collection<? extends Currency> getCurrencies() {
        return currencyManager.getCurrencies();
    }

    @Override
    public Currency getCurrency(String name) {
        return currencyManager.getCurrency(name);
    }

    @Override
    public void removeCurrency(Currency currency) {
        currencyManager.removeCurrency(currency);
    }

    @Override
    public void addCurrency(Currency currency) {
        currencyManager.addCurrency(currency);
    }

    @Override
    public Collection<? extends Auction> getAuctions() {
        return auctionManager.getAuctions();
    }

    @Override
    public void addAuction(Auction auction) {
        auctionManager.addAuction(auction);
    }

    @Override
    public void removeAuction(Auction auction) {
        auctionManager.removeAuction(auction);
    }

    @Override
    public Auction getAuction(Character character) {
        return auctionManager.getAuction(character);
    }

    @Override
    public int getMoney(OfflinePlayer player, Currency currency) {
        RegisteredServiceProvider<CharacterPlugin> characterProvider = getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterProvider != null) {
            return getMoney(characterProvider.getProvider().getActiveCharacter(player), currency);
        }
        return 0;
    }

    @Override
    public void setMoney(OfflinePlayer player, Currency currency, int amount) {
        RegisteredServiceProvider<CharacterPlugin> characterProvider = getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterProvider != null) {
            setMoney(characterProvider.getProvider().getActiveCharacter(player), currency, amount);
        }
    }

    @Override
    public void addMoney(OfflinePlayer player, Currency currency, int amount) {
        RegisteredServiceProvider<CharacterPlugin> characterProvider = getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterProvider != null) {
            addMoney(characterProvider.getProvider().getActiveCharacter(player), currency, amount);
        }
    }

    @Override
    public void transferMoney(OfflinePlayer takeFrom, OfflinePlayer giveTo, Currency currency, int amount) {
        RegisteredServiceProvider<CharacterPlugin> characterProvider = getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterProvider != null) {
            CharacterPlugin characterPlugin = characterProvider.getProvider();
            transferMoney(characterPlugin.getActiveCharacter(takeFrom), characterPlugin.getActiveCharacter(giveTo), currency, amount);
        }
    }

    @Override
    public int getMoney(Character character, Currency currency) {
        return currencyManager.getMoney(character, currency);
    }

    @Override
    public void setMoney(Character character, Currency currency, int amount) {
        currencyManager.setMoney(character, currency, amount);
    }

    @Override
    public void addMoney(Character character, Currency currency, int amount) {
        currencyManager.addMoney(character, currency, amount);
    }

    @Override
    public void transferMoney(Character takeFrom, Character giveTo, Currency currency, int amount) {
        currencyManager.transferMoney(takeFrom, giveTo, currency, amount);
    }

    public Character[] getRichestCharacters() {
        File richestFile = new File(getDataFolder(), "richest.yml");
        YamlConfiguration richestSave = YamlConfiguration.loadConfiguration(richestFile);
        List<Integer> richestIds = richestSave.getIntegerList("richest");
        Character[] richestCharacters = new Character[5];
        int i = 0;
        for (int id : richestIds) {
            if (i < 5) richestCharacters[i] = characterPlugin.getCharacter(id); else break;
            i++;
        }
        return richestCharacters;
    }

    public void checkRichest(Character check) {
        File richestFile = new File(getDataFolder(), "richest.yml");
        YamlConfiguration richestSave = YamlConfiguration.loadConfiguration(richestFile);
        List<Long> richestIds = richestSave.getLongList("richest");
        if (!richestIds.contains(check.getId())) richestIds.add(check.getId());
        List<Character> characters = new ArrayList<>();
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            for (long id : richestIds) {
                characters.add(characterPlugin.getCharacter(id));
            }
        }
        quickSort(characters);
        richestIds.clear();
        for (int i = 0; i < Math.min(5, characters.size()); i++) {
            richestIds.add(characters.get(i).getId());
        }
        richestSave.set("richest", richestIds);
        try {
            richestSave.save(richestFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private int partition(List<Character> characters, int left, int right) {
        int i = left, j = right;
        Character tmp;
        Character pivot = characters.get((left + right) / 2);
        while (i <= j) {
            while (getMoney(characters.get(i)) > getMoney(pivot)) i++;
            while (getMoney(characters.get(j)) < getMoney(pivot)) j--;
            if (i <= j) {
                tmp = characters.get(i);
                characters.set(i, characters.get(j));
                characters.set(j, tmp);
                i++;
                j--;
            }
        }
        return i;
    }


    private void quickSort(List<Character> characters, int left, int right) {
        int index = partition(characters, left, right);
        if (left < index - 1) quickSort(characters, left, index - 1);
        if (index < right) quickSort(characters, index, right);
    }

    private void quickSort(List<Character> characters) {
        quickSort(characters, 0, characters.size() - 1);
    }

}
