package net.wayward_realms.waywarddonations;

import net.wayward_realms.waywardlib.donation.DonationPlugin;
import net.wayward_realms.waywardlib.donation.DonationRank;
import net.wayward_realms.waywardlib.essentials.Kit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WaywardDonations extends JavaPlugin implements DonationPlugin {

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(DonationRankImpl.class);
        ConfigurationSerialization.registerClass(KitImpl.class);
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
            Kit bronzeKit = new KitImpl();
            bronzeKit.getItems().add(new ItemStack(Material.DIAMOND, 10));
            bronzeKit.getItems().add(new ItemStack(Material.EMERALD, 10));
            bronzeKit.getItems().add(new ItemStack(Material.GOLD_INGOT, 20));
            addDonationRank(new DonationRankImpl("bronze", bronzeKit, 400, 0));
            Kit silverKit = new KitImpl();
            silverKit.getItems().add(new ItemStack(Material.DIAMOND, 20));
            silverKit.getItems().add(new ItemStack(Material.EMERALD, 20));
            silverKit.getItems().add(new ItemStack(Material.GOLD_INGOT, 20));
            addDonationRank(new DonationRankImpl("silver", silverKit, 800, 6));
            Kit goldKit = new KitImpl();
            goldKit.getItems().add(new ItemStack(Material.DIAMOND, 30));
            goldKit.getItems().add(new ItemStack(Material.EMERALD, 30));
            goldKit.getItems().add(new ItemStack(Material.GOLD_INGOT, 30));
            addDonationRank(new DonationRankImpl("gold", goldKit, 1200, 6));
            Kit platinumKit = new KitImpl();
            platinumKit.getItems().add(new ItemStack(Material.DIAMOND, 40));
            platinumKit.getItems().add(new ItemStack(Material.EMERALD, 40));
            platinumKit.getItems().add(new ItemStack(Material.GOLD_INGOT, 40));
            addDonationRank(new DonationRankImpl("platinum", platinumKit, 1600, 6));
            saveConfig();
        }
        getCommand("claimlevels").setExecutor(new ClaimLevelsCommand(this));
        getCommand("claimmoney").setExecutor(new ClaimMoneyCommand(this));
        getCommand("claimitems").setExecutor(new ClaimItemsCommand(this));
        getCommand("donation").setExecutor(new DonationCommand(this));
    }

    @Override
    public String getPrefix() {
        //return "" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "|" + ChatColor.RESET + ChatColor.BLUE + "WaywardDonations" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "| " + ChatColor.RESET;
        return "";
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loadState() {

    }

    @Override
    public void saveState() {

    }

    @Override
    public List<DonationRank> getDonationRanks(OfflinePlayer player) {
        File playerDirectory = new File(getDataFolder(), "players");
        File playerFile = new File(playerDirectory, player.getUniqueId().toString());
        YamlConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
        List<DonationRank> ranks = new ArrayList<>();
        for (String rankName : playerConfig.getStringList("donation-ranks")) {
            ranks.add(getDonationRank(rankName));
        }
        return ranks;
    }

    @Override
    public void addDonationRank(OfflinePlayer player, DonationRank rank) {
        File playerDirectory = new File(getDataFolder(), "players");
        File playerFile = new File(playerDirectory, player.getUniqueId().toString());
        YamlConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
        List<String> rankNames = playerConfig.getStringList("donation-ranks");
        rankNames.add(rank.getName());
        playerConfig.set("donation-ranks", rankNames);
        try {
            playerConfig.save(playerFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void removeDonationRank(OfflinePlayer player, DonationRank rank) {
        File playerDirectory = new File(getDataFolder(), "players");
        File playerFile = new File(playerDirectory, player.getUniqueId().toString());
        YamlConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
        List<String> rankNames = playerConfig.getStringList("donation-ranks");
        rankNames.remove(rank.getName());
        playerConfig.set("donation-ranks", rankNames);
        try {
            playerConfig.save(playerFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void addDonationRank(DonationRank rank) {
        getConfig().set("donation-ranks." + rank.getName(), rank);
        saveConfig();
    }

    @Override
    public void removeDonationRank(DonationRank rank) {
        getConfig().set("donation-ranks." + rank.getName(), null);
        saveConfig();
    }

    @Override
    public DonationRank getDonationRank(String name) {
        return (DonationRank) getConfig().get("donation-ranks." + name);
    }

    public int getMoneyClaimed(OfflinePlayer player) {
        File playerDirectory = new File(getDataFolder(), "players");
        File playerFile = new File(playerDirectory, player.getUniqueId().toString() + ".yml");
        YamlConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
        return playerConfig.getInt("money-claimed");
    }

    public void setMoneyClaimed(OfflinePlayer player, int money) {
        File playerDirectory = new File(getDataFolder(), "players");
        File playerFile = new File(playerDirectory, player.getUniqueId().toString() + ".yml");
        YamlConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
        playerConfig.set("money-claimed", money);
    }

    public int getItemsClaimed(OfflinePlayer player, Material type) {
        File playerDirectory = new File(getDataFolder(), "players");
        File playerFile = new File(playerDirectory, player.getUniqueId().toString() + ".yml");
        YamlConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
        return playerConfig.getInt("items-claimed." + type.toString().toLowerCase());
    }

    public void setItemsClaimed(OfflinePlayer player, Material type, int amount) {
        File playerDirectory = new File(getDataFolder(), "players");
        File playerFile = new File(playerDirectory, player.getUniqueId().toString() + ".yml");
        YamlConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
        playerConfig.set("items-claimed." + type.toString().toLowerCase(), amount);
        try {
            playerConfig.save(playerFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}