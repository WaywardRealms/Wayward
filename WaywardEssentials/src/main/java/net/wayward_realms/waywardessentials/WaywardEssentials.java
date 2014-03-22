package net.wayward_realms.waywardessentials;

import net.wayward_realms.waywardessentials.command.*;
import net.wayward_realms.waywardessentials.drink.DrinkManager;
import net.wayward_realms.waywardessentials.drink.PlayerItemConsumeListener;
import net.wayward_realms.waywardessentials.kit.KitImpl;
import net.wayward_realms.waywardessentials.kit.KitManager;
import net.wayward_realms.waywardessentials.time.TimeSlowRunnable;
import net.wayward_realms.waywardessentials.warp.WarpManager;
import net.wayward_realms.waywardessentials.warp.WarpPlayerInteractListener;
import net.wayward_realms.waywardessentials.warp.WarpSignChangeListener;
import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.essentials.EssentialsPlugin;
import net.wayward_realms.waywardlib.essentials.Kit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.kohsuke.github.GitHub;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WaywardEssentials extends JavaPlugin implements EssentialsPlugin {

    private WarpManager warpManager;
    private KitManager kitManager;
    private DrinkManager drinkManager;

    private GitHub gitHub;

    private Set<String> logMessagesEnabled = new HashSet<>();
    private Map<String, Location> previousLocations = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        ConfigurationSerialization.registerClass(KitImpl.class);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new TimeSlowRunnable(this), 100L, 100L);
        registerListeners(new PlayerJoinListener(this), new PlayerQuitListener(this), new PlayerTeleportListener(this),
                new PlayerItemConsumeListener(this),
                new WarpSignChangeListener(this), new WarpPlayerInteractListener(this));
        registerCommands();
        warpManager = new WarpManager(this);
        kitManager = new KitManager(this);
        drinkManager = new DrinkManager(this);
        try {
            File gitHubFile = new File(getDataFolder(), "github.yml");
            if (gitHubFile.exists()) {
                YamlConfiguration gitHubConfig = new YamlConfiguration();
                gitHubConfig.load(gitHubFile);
                if (gitHubConfig.getString("oauth") != null) {
                    getLogger().info("Found OAuth token in GitHub config, attempting to connect using OAuth...");
                    gitHub = GitHub.connectUsingOAuth(gitHubConfig.getString("oauth"));
                } else if (gitHubConfig.getString("user") != null && gitHubConfig.getString("password") != null) {
                    getLogger().info("Found user and password in GitHub config, attempting to connect using user and password...");
                    gitHub = GitHub.connectUsingPassword(gitHubConfig.getString("user"), gitHubConfig.getString("password"));
                } else {
                    getLogger().info("No user & password or OAuth token found in GitHub config, attempting to connect anonymously...");
                    gitHub = GitHub.connectAnonymously();
                }
            } else {
                getLogger().info("No GitHub config found, attempting to connect anonymously...");
                gitHub = GitHub.connectAnonymously();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (InvalidConfigurationException exception) {
            exception.printStackTrace();
        }
        drinkManager.setupRecipes();
    }

    @Override
    public void onDisable() {
        warpManager.save();
        kitManager.save();
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            this.getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    private void registerCommands() {
        getCommand("back").setExecutor(new BackCommand(this));
        getCommand("clone").setExecutor(new CloneCommand(this));
        getCommand("deletewarp").setExecutor(new DeleteWarpCommand(this));
        getCommand("enchant").setExecutor(new EnchantCommand(this));
        getCommand("feed").setExecutor(new FeedCommand(this));
        getCommand("fly").setExecutor(new FlyCommand(this));
        getCommand("getbook").setExecutor(new GetBookCommand(this));
        getCommand("getsign").setExecutor(new GetSignCommand(this));
        getCommand("heal").setExecutor(new HealCommand(this));
        getCommand("inventory").setExecutor(new InventoryCommand(this));
        getCommand("issue").setExecutor(new IssueCommand(this));
        getCommand("item").setExecutor(new ItemCommand(this));
        getCommand("itemmeta").setExecutor(new ItemMetaCommand(this));
        getCommand("jump").setExecutor(new JumpCommand(this));
        getCommand("kit").setExecutor(new KitCommand(this));
        getCommand("msg").setExecutor(new MsgCommand(this));
        getCommand("repair").setExecutor(new RepairCommand(this));
        getCommand("roll").setExecutor(new RollCommand(this));
        getCommand("runas").setExecutor(new RunAsCommand(this));
        getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
        getCommand("setwarp").setExecutor(new SetWarpCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("spawner").setExecutor(new SpawnerCommand(this));
        getCommand("spawnmob").setExecutor(new SpawnMobCommand(this));
        getCommand("speed").setExecutor(new SpeedCommand(this));
        getCommand("sudo").setExecutor(new SudoCommand(this));
        getCommand("togglelogmessages").setExecutor(new ToggleLogMessagesCommand(this));
        getCommand("unsign").setExecutor(new UnsignCommand(this));
        getCommand("warp").setExecutor(new WarpCommand(this));
    }

    @Override
    public String getPrefix() {
        //return "" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "|" + ChatColor.RESET + ChatColor.BLUE + "WaywardEssentials" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "| " + ChatColor.RESET;
        return "";
    }

    @Override
    public void loadState() {
        warpManager.load();
        kitManager.load();
    }

    @Override
    public void saveState() {
        warpManager.save();
        kitManager.save();
    }

    @Override
    public Map<String, Location> getWarps() {
        return warpManager.getWarps();
    }

    @Override
    public Location getWarp(String name) {
        return warpManager.getWarp(name);
    }

    @Override
    public void addWarp(String name, Location location) {
        warpManager.addWarp(name, location);
    }

    @Override
    public void removeWarp(String name) {
        warpManager.removeWarp(name);
    }

    @Override
    public Map<String, Kit> getKits() {
        return kitManager.getKits();
    }

    @Override
    public Kit getKit(String name) {
        return kitManager.getKit(name);
    }

    @Override
    public void addKit(Kit kit) {
        kitManager.addKit(kit);
    }

    @Override
    public void removeKit(Kit kit) {
        kitManager.removeKit(kit);
    }

    @Override
    public int getDrunkenness(Character character) {
        return drinkManager.getDrunkenness(character);
    }

    @Override
    public void setDrunkenness(Character character, int drunkenness) {
        drinkManager.setDrunkenness(character, drunkenness);
    }

    @Override
    public int getDrunkenness(OfflinePlayer player) {
        return drinkManager.getDrunkenness(player);
    }

    @Override
    public void setDrunkenness(OfflinePlayer player, int drunkenness) {
        drinkManager.setDrunkenness(player, drunkenness);
    }
    
    public GitHub getGitHub() {
        return gitHub;
    }

    public DrinkManager getDrinkManager() {
        return drinkManager;
    }

    public boolean isLogMessagesEnabled(OfflinePlayer player) {
        return logMessagesEnabled.contains(player.getName());
    }

    public void setLogMessagesEnabled(OfflinePlayer player, boolean enable) {
        if (enable) {
            logMessagesEnabled.add(player.getName());
        } else {
            logMessagesEnabled.remove(player.getName());
        }
    }

    public Set<Player> getPlayersWithLogMessagesEnabled() {
        Set<Player> players = new HashSet<>();
        for (String playerName : logMessagesEnabled) {
            players.add(getServer().getPlayer(playerName));
        }
        return players;
    }

    public Location getPreviousLocation(OfflinePlayer player) {
        return previousLocations.get(player.getName());
    }

    public void setPreviousLocation(OfflinePlayer player, Location location) {
        previousLocations.put(player.getName(), location);
    }

}
