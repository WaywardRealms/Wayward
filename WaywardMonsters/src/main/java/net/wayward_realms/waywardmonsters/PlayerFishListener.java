package net.wayward_realms.waywardmonsters;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayerFishListener implements Listener {

    private WaywardMonsters plugin;

    private FileConfiguration fishingConfig = null;
    private File fishingConfigFile = null;

    private Random random;

    private ArrayList<ItemStack> fish = new ArrayList<>();

    public PlayerFishListener(WaywardMonsters plugin) {
        this.plugin = plugin;
        saveDefaultFishingConfig();
        loadFish();
        random = new Random();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            event.getCaught().remove();
            int roll = random.nextInt(101);
            if (roll > (100 - (getFishingConfig().getInt("chance") + 1))) {
                event.getPlayer().sendMessage(ChatColor.YELLOW + "The caught fish looks a bit off...");
            } else {
                roll = random.nextInt(fish.size() + 1);
                event.getPlayer().getWorld().dropItemNaturally(event.getHook().getLocation(), fish.get(roll)).setVelocity(reelVelocity(event.getPlayer(), event.getHook()));
            }
        }
    }

    private Vector reelVelocity(Player player, FishHook hook) {
        double x = player.getLocation().getX() - hook.getLocation().getX();
        double y = player.getLocation().getY() - hook.getLocation().getY();
        double z = player.getLocation().getZ() - hook.getLocation().getZ();
        return new Vector(x * 0.1D, y * 0.1D + Math.sqrt(Math.sqrt(x * x + y * y + z * z)) * 0.08D, z * 0.1D);
    }

    private void loadFish() {
        fish.addAll((List<ItemStack>) getFishingConfig().getList("fish"));
    }

    private void saveDefaultFishingConfig() {
        if (fishingConfigFile == null) {
            fishingConfigFile = new File(plugin.getDataFolder(), "fishing.yml");
        }
        if (!fishingConfigFile.exists()) {
            fishingConfig = new YamlConfiguration();
            fishingConfig.set("chance", 5);
            fishingConfig.set("fish", getDefaultFish());
            try {
                fishingConfig.save(fishingConfigFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    private List<ItemStack> getDefaultFish() {
        List<ItemStack> availableFish = new ArrayList<>();
        availableFish.add(fishify("Trout", "A standard looking fish..."));
        availableFish.add(fishify("Carp", "Quit complaining..."));
        availableFish.add(fishify("Bass", "Where are the strings?..."));
        availableFish.add(fishify("Guppy", "More tail than fish..."));
        availableFish.add(fishify("Minnow", "Should have used this as bait..."));
        availableFish.add(fishify("Oreo", "Doesn't look very tasty..."));
        availableFish.add(fishify("Tuna", "How's this fit into a can?..."));
        availableFish.add(fishify("Salmon", "Looks just the same as pink..."));
        availableFish.add(fishify("Catfish", "Not good for pets..."));
        availableFish.add(fishify("Snapper", "Could be a red herring..."));
        availableFish.add(fishify("Herring", "Wonder why it isn't red..."));
        availableFish.add(fishify("Anchovy", "Tiny little thing..."));
        availableFish.add(fishify("Cod", "Better than the game..."));
        availableFish.add(fishify("Pike", "Less effective than a spear..."));
        availableFish.add(fishify("Marlin", "Not that great for ropework..."));
        availableFish.add(fishify("Loach", "Tastes like plant..."));
        availableFish.add(fishify("Flounder", "Probably didn't escape..."));
        availableFish.add(fishify("Haddock", "Doesn't wear a kilt..."));
        availableFish.add(fishify("Halibut", "Doesn't sound suitable for events..."));
        availableFish.add(fishify("Whiptail", "Sometimes used as a javelin..."));
        return availableFish;
    }

    private ItemStack fishify(String name, String description) {
        ItemStack fish = new ItemStack(Material.RAW_FISH);
        ItemMeta troutMeta = fish.getItemMeta();
        troutMeta.setDisplayName(name);
        troutMeta.setLore(Collections.singletonList(description));
        fish.setItemMeta(troutMeta);
        return fish;
    }

    private FileConfiguration getFishingConfig() {
        if (fishingConfig == null) {
            reloadFishingConfig();
        }
        return fishingConfig;
    }

    private void reloadFishingConfig() {
        if (fishingConfigFile == null) {
            fishingConfigFile = new File(plugin.getDataFolder(), "fishing.yml");
            if (!fishingConfigFile.exists()) {
                saveDefaultFishingConfig();
            }
        }
        fishingConfig = YamlConfiguration.loadConfiguration(fishingConfigFile);
    }

}