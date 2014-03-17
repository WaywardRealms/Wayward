package net.wayward_realms.waywardeconomy.auction;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardeconomy.WaywardEconomy;
import net.wayward_realms.waywardlib.economy.Auction;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AuctionManager {

    private WaywardEconomy plugin;
    private Map<Integer, Auction> auctions = new HashMap<>();

    public AuctionManager(WaywardEconomy plugin) {
        this.plugin = plugin;
    }

    public Collection<? extends Auction> getAuctions() {
        return auctions.values();
    }

    public void addAuction(Auction auction) {
        auctions.put(auction.getCharacter().getId(), auction);
    }

    public void removeAuction(Auction auction) {
        auctions.remove(auction.getCharacter().getId());
    }

    public Auction getAuction(Character character) {
        return auctions.get(character.getId());
    }

    public void loadState() {
        File auctionDirectory = new File(plugin.getDataFolder(), "auctions");
        if (auctionDirectory.exists()) {
            for (File currencyFile : auctionDirectory.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.getName().endsWith(".yml");
                }
            })) {
                YamlConfiguration auctionConfig = new YamlConfiguration();
                try {
                    auctionConfig.load(currencyFile);
                } catch (IOException exception) {
                    exception.printStackTrace();
                } catch (InvalidConfigurationException exception) {
                    exception.printStackTrace();
                }
                Auction auction = (Auction) auctionConfig.get("auction");
                auctions.put(auction.getCharacter().getId(), auction);
            }
        }
    }

    public void saveState() {
        File auctionDirectory = new File(plugin.getDataFolder(), "auction");
        if (!auctionDirectory.isDirectory()) {
            auctionDirectory.delete();
        }
        if (!auctionDirectory.exists()) {
            auctionDirectory.mkdir();
        }
        for (Auction auction : auctions.values()) {
            YamlConfiguration auctionConfig = new YamlConfiguration();
            auctionConfig.set("auction", auction);
            try {
                auctionConfig.save(new File(auctionDirectory, auction.getCharacter().getId() + ".yml"));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
