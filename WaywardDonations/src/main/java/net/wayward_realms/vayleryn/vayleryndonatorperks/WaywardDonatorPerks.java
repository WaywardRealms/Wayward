package net.wayward_realms.vayleryn.vayleryndonatorperks;

import net.wayward_realms.waywardlib.donation.DonationPlugin;
import net.wayward_realms.waywardlib.donation.DonationRank;
import net.wayward_realms.waywardlib.essentials.Kit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class WaywardDonatorPerks extends JavaPlugin implements DonationPlugin {

    private Map<String, DonationRank> donationRanks = new HashMap<>();
    private Map<String, List<DonationRank>> playerDonationRanks = new HashMap<>();

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
            donationRanks.put("bronze", new DonationRankImpl("bronze", bronzeKit, 400, 0));
            Kit silverKit = new KitImpl();
            silverKit.getItems().add(new ItemStack(Material.DIAMOND, 20));
            silverKit.getItems().add(new ItemStack(Material.EMERALD, 20));
            silverKit.getItems().add(new ItemStack(Material.GOLD_INGOT, 20));
            donationRanks.put("silver", new DonationRankImpl("silver", silverKit, 800, 6));
            Kit goldKit = new KitImpl();
            goldKit.getItems().add(new ItemStack(Material.DIAMOND, 30));
            goldKit.getItems().add(new ItemStack(Material.EMERALD, 30));
            goldKit.getItems().add(new ItemStack(Material.GOLD_INGOT, 30));
            donationRanks.put("gold", new DonationRankImpl("gold", goldKit, 1200, 6));
            Kit platinumKit = new KitImpl();
            platinumKit.getItems().add(new ItemStack(Material.DIAMOND, 40));
            platinumKit.getItems().add(new ItemStack(Material.EMERALD, 40));
            platinumKit.getItems().add(new ItemStack(Material.GOLD_INGOT, 40));
            donationRanks.put("platinum", new DonationRankImpl("platinum", platinumKit, 1600, 6));
            for (String donationRankName : donationRanks.keySet()) {
                getConfig().set("donation-ranks." + donationRankName, donationRanks.get(donationRankName));
            }
            saveConfig();
        }
        if (getConfig().getConfigurationSection("donation-ranks") != null) {
            for (String rankName : getConfig().getConfigurationSection("donation-ranks").getKeys(false)) {
                donationRanks.put(rankName, (DonationRank) getConfig().get("donation-ranks." + rankName));
            }
        }
        getCommand("claimlevels").setExecutor(new ClaimLevelsCommand(this));
        getCommand("claimmoney").setExecutor(new ClaimMoneyCommand(this));
        getCommand("claimitems").setExecutor(new ClaimItemsCommand(this));
        getCommand("donation").setExecutor(new DonationCommand(this));
    }

    @Override
    public void onDisable() {
        for (String donationRank : donationRanks.keySet()) {
            this.getConfig().set("donation-ranks." + donationRank, donationRanks.get(donationRank));
        }
        for (String playerName : playerDonationRanks.keySet()) {
            this.getConfig().set("players." + playerName, playerDonationRanks.get(playerName));
        }
        this.saveConfig();
    }

    @Override
    public String getPrefix() {
        //return "" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "|" + ChatColor.RESET + ChatColor.BLUE + "WaywardDonatorPerks" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "| " + ChatColor.RESET;
        return "";
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loadState() {
        if (getConfig().getConfigurationSection("players") != null) {
            for (String section : getConfig().getConfigurationSection("players").getKeys(false)) {
                playerDonationRanks.put(section, (List<DonationRank>) getConfig().get("players." + section));
            }
        }
    }

    @Override
    public void saveState() {

    }

    @Override
    public List<DonationRank> getDonationRanks(OfflinePlayer player) {
        return playerDonationRanks.get(player.getName());
    }

    @Override
    public void addDonationRank(OfflinePlayer player, DonationRank rank) {
        if (playerDonationRanks.get(player.getName()) == null) {
            playerDonationRanks.put(player.getName(), new ArrayList<DonationRank>());
        }
        playerDonationRanks.get(player.getName()).add(rank);
    }

    @Override
    public void removeDonationRank(OfflinePlayer player, DonationRank rank) {
        playerDonationRanks.get(player.getName()).remove(rank);
    }

    @Override
    public void addDonationRank(DonationRank rank) {
        donationRanks.put(rank.getName(), rank);
    }

    @Override
    public void removeDonationRank(DonationRank rank) {
        donationRanks.remove(rank.getName());
    }

    @Override
    public DonationRank getDonationRank(String name) {
        return donationRanks.get(name);
    }

    public Map<String, DonationRank> getDonationRanks() {
        return donationRanks;
    }

}