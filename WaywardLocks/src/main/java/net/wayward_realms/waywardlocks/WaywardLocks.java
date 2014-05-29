package net.wayward_realms.waywardlocks;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.lock.LockPlugin;
import net.wayward_realms.waywardlib.util.location.LocationUtils;
import net.wayward_realms.waywardlib.util.serialisation.SerialisableLocation;
import net.wayward_realms.waywardlocks.keyring.KeyringManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WaywardLocks extends JavaPlugin implements LockPlugin {

    private KeyringManager keyringManager;

    private Set<String> unclaiming = new HashSet<>();
    private Set<String> getkey = new HashSet<>();

    @Override
    public void onEnable() {
        keyringManager = new KeyringManager(this);
        getCommand("keyring").setExecutor(new KeyringCommand(this));
        getCommand("lock").setExecutor(new LockCommand(this));
        getCommand("unlock").setExecutor(new UnlockCommand(this));
        getCommand("getkey").setExecutor(new GetKeyCommand(this));
        getCommand("lockpickefficiency").setExecutor(new LockpickEfficiencyCommand(this));
        getServer().getPluginManager().registerEvents(new CraftItemListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryCloseListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        ItemStack lockpickItem = new ItemStack(Material.IRON_INGOT);
        ItemMeta lockpickMeta = lockpickItem.getItemMeta();
        lockpickMeta.setDisplayName("Lockpick");
        List<String> lockpickLore = new ArrayList<>();
        lockpickLore.add("Used for breaking through locks");
        lockpickMeta.setLore(lockpickLore);
        lockpickItem.setItemMeta(lockpickMeta);
        ShapedRecipe lockpickRecipe = new ShapedRecipe(lockpickItem);
        lockpickRecipe.shape("L", "I", "I").setIngredient('L', Material.LEVER).setIngredient('I', Material.IRON_INGOT);
        getServer().addRecipe(lockpickRecipe);
        ItemStack lockItem = new ItemStack(Material.IRON_INGOT);
        ItemMeta lockMeta = lockItem.getItemMeta();
        lockMeta.setDisplayName("Lock");
        List<String> lockLore = new ArrayList<>();
        lockLore.add("Used for locking chests");
        lockMeta.setLore(lockLore);
        lockItem.setItemMeta(lockMeta);
        ShapedRecipe lockRecipe = new ShapedRecipe(lockItem);
        lockRecipe.shape("I", "B").setIngredient('I', Material.IRON_INGOT).setIngredient('B', Material.IRON_BLOCK);
        getServer().addRecipe(lockRecipe);

        File oldLocksFile = new File(getDataFolder(), "locks.yml");
        if (oldLocksFile.exists()) {
            YamlConfiguration oldLocksConfig = YamlConfiguration.loadConfiguration(oldLocksFile);
            File newLocksFile = new File(getDataFolder(), "locks-new.yml");
            YamlConfiguration newLocksConfig = new YamlConfiguration();
            List<String> locationStrings = new ArrayList<>();
            for (SerialisableLocation location : (List<SerialisableLocation>) oldLocksConfig.getList("locks")) {
                locationStrings.add(LocationUtils.toString(location.toLocation()));
            }
            newLocksConfig.set("locks", locationStrings);
            try {
                newLocksConfig.save(newLocksFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public String getPrefix() {
        //return "" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "|" + ChatColor.RESET + ChatColor.BLUE + "WaywardLocks" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "| " + ChatColor.RESET;
        return "";
    }

    @Override
    public void loadState() {

    }

    private void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                    file.delete();
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }

    @Override
    public void saveState() {

    }

    @Override
    public int getLockId(Block block) {
        return 0;
    }

    @Override
    public boolean isLocked(Block block) {
        File locksFile = new File(getDataFolder(), "locks-new.yml");
        YamlConfiguration lockConfig = YamlConfiguration.loadConfiguration(locksFile);
        List<String> locked = lockConfig.getStringList("locks");
        return locked.contains(LocationUtils.toString(block.getLocation()));
    }

    @Override
    public ItemStack lock(Block block) {
        ItemStack key = new ItemStack(Material.IRON_INGOT);
        File locksFile = new File(getDataFolder(), "locks-new.yml");
        YamlConfiguration lockConfig = YamlConfiguration.loadConfiguration(locksFile);
        List<String> locked = lockConfig.getStringList("locks");
        locked.add(LocationUtils.toString(block.getLocation()));
        lockConfig.set("locks", locked);
        try {
            lockConfig.save(locksFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        ItemMeta keyMeta = key.getItemMeta();
        keyMeta.setDisplayName("Key");
        List<String> keyLore = new ArrayList<>();
        keyLore.add(block.getWorld().getName() + "," + block.getLocation().getBlockX() + "," + block.getLocation().getBlockY() + "," + block.getLocation().getBlockZ());
        keyMeta.setLore(keyLore);
        key.setItemMeta(keyMeta);
        return key;
    }

    @Override
    public void unlock(Block block) {
        File locksFile = new File(getDataFolder(), "locks-new.yml");
        YamlConfiguration lockConfig = YamlConfiguration.loadConfiguration(locksFile);
        List<String> locked = lockConfig.getStringList("locks");
        locked.remove(LocationUtils.toString(block.getLocation()));
        lockConfig.set("locks", locked);
        try {
            lockConfig.save(locksFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public int getLockpickEfficiency(net.wayward_realms.waywardlib.character.Character character) {
        File efficiencyFile = new File(getDataFolder(), "efficiency.yml");
        YamlConfiguration efficiencySave = YamlConfiguration.loadConfiguration(efficiencyFile);
        if (efficiencySave.get("" + character.getId()) == null) {
            return 5;
        }
        return efficiencySave.getInt("" + character.getId());
    }

    @Override
    public void setLockpickEfficiency(Character character, int efficiency) {
        if (efficiency > 0 && efficiency <= 100) {
            if (character.getPlayer().isOnline()) {
                character.getPlayer().getPlayer().sendMessage(getPrefix() + ChatColor.GREEN + "+" + (efficiency - getLockpickEfficiency(character)) + "% lockpicking efficiency " + ChatColor.GRAY + "(Total: " + efficiency + "%)");
            }
            File efficiencyFile = new File(getDataFolder(), "efficiency.yml");
            YamlConfiguration effiencySave = YamlConfiguration.loadConfiguration(efficiencyFile);
            effiencySave.set("" + character.getId(), efficiency);
        }
    }

    public int getLockpickEfficiency(OfflinePlayer player) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            return getLockpickEfficiency(characterPlugin.getActiveCharacter(player));
        }
        return 5;
    }

    public void setLockpickEfficiency(OfflinePlayer player, int efficiency) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            setLockpickEfficiency(characterPlugin.getActiveCharacter(player), efficiency);
        }
    }

    public boolean isClaiming(OfflinePlayer offlinePlayer) {
        if (offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();
            if (player.getItemInHand() != null) {
                ItemStack item = player.getItemInHand();
                if (item.hasItemMeta()) {
                    if (item.getItemMeta().hasDisplayName() && item.getItemMeta().hasLore()) {
                        return item.getItemMeta().getDisplayName().equalsIgnoreCase("Lock") && item.getItemMeta().getLore().contains("Used for locking chests");
                    }
                }
            }
        }
        return false;
    }

    public boolean isUnclaiming(OfflinePlayer player) {
        return unclaiming.contains(player.getName());
    }

    public void setUnclaiming(OfflinePlayer player, boolean unclaim) {
        if (unclaim) {
            unclaiming.add(player.getName());
        } else {
            unclaiming.remove(player.getName());
        }
    }

    public boolean isGettingKey(OfflinePlayer player) {
        return getkey.contains(player.getName());
    }

    public void setGettingKey(OfflinePlayer player, boolean gettingKey) {
        if (gettingKey) {
            getkey.add(player.getName());
        } else {
            getkey.remove(player.getName());
        }
    }

    public KeyringManager getKeyringManager() {
        return keyringManager;
    }
}
