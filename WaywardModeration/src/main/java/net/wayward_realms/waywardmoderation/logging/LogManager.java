package net.wayward_realms.waywardmoderation.logging;

import net.wayward_realms.waywardlib.util.location.LocationUtils;
import net.wayward_realms.waywardmoderation.WaywardModeration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class LogManager {

    private Map<Block, List<BlockChange>> blockChanges = new HashMap<>();
    private Map<InventoryHolder, List<InventoryChange>> inventoryChanges = new HashMap<>();
    private WaywardModeration plugin;

    public LogManager(final WaywardModeration plugin) {
        this.plugin = plugin;
    }

    /**
     * @param block {@link Block} of interest.
     * @return a Map of the date and {@link Material} a {@link Block} changed to.
     */
    public Map<Date, Material> getBlockMaterialChanges(Block block) {
        Map<Date, Material> blockMaterialChanges = new HashMap<>();
        for (BlockChange change : blockChanges.get(block)) {
            blockMaterialChanges.put(change.getDate(), change.getMaterial());
        }
        return blockMaterialChanges;
    }

    public Map<Date, Byte> getBlockDataChanges(Block block) {
        Map<Date, Byte> blockDataChanges = new HashMap<>();
        for (BlockChange change : blockChanges.get(block)) {
            blockDataChanges.put(change.getDate(), change.getData());
        }
        return blockDataChanges;
    }

    public Map<Date, ItemStack> getInventoryContentAdditions(InventoryHolder inventoryHolder) {
        Map<Date, ItemStack> inventoryContentAdditions = new HashMap<>();
        for (InventoryChange inventoryChange : inventoryChanges.get(inventoryHolder)) {
            if (inventoryChange.getType() == InventoryChangeType.ADDITION) {
                inventoryContentAdditions.put(inventoryChange.getDate(), inventoryChange.getItemStack());
            }
        }
        return inventoryContentAdditions;
    }

    public Map<Date, ItemStack> getInventoryContentRemovals(InventoryHolder inventoryHolder) {
        Map<Date, ItemStack> inventoryContentRemovals = new HashMap<>();
        for (InventoryChange inventoryChange : inventoryChanges.get(inventoryHolder)) {
            if (inventoryChange.getType() == InventoryChangeType.REMOVAL) {
                inventoryContentRemovals.put(inventoryChange.getDate(), inventoryChange.getItemStack());
            }
        }
        return inventoryContentRemovals;
    }

    public Material getBlockMaterialAtTime(Block block, Date date) {
        List<Date> dates = new ArrayList<>();
        for (BlockChange change : blockChanges.get(block)) {
            dates.add(change.getDate());
        }
        Collections.sort(dates);
        Date lastChangeDate = null;
        for (Date changeDate : dates) {
            if (!changeDate.before(date)) {
                break;
            }
            lastChangeDate = changeDate;
        }
        BlockChange lastChange = null;
        for (BlockChange change : blockChanges.get(block)) {
            if (change.getDate().equals(lastChangeDate)) {
                lastChange = change;
            }
        }
        return lastChange == null ? block.getType() : lastChange.getMaterial();
    }

    public byte getBlockDataAtTime(Block block, Date date) {
        List<Date> dates = new ArrayList<>();
        for (BlockChange change : blockChanges.get(block)) {
            dates.add(change.getDate());
        }
        Collections.sort(dates);
        Date lastChangeDate = null;
        for (Date changeDate : dates) {
            if (!changeDate.before(date)) {
                break;
            }
            lastChangeDate = changeDate;
        }
        BlockChange lastChange = null;
        for (BlockChange change : blockChanges.get(block)) {
            if (change.getDate().equals(lastChangeDate)) {
                lastChange = change;
            }
        }
        return lastChange == null ? block.getData() : lastChange.getData();
    }

    public void recordBlockChange(Block block) {
        if (blockChanges.get(block) == null) {
            blockChanges.put(block, new ArrayList<BlockChange>());
        }
        blockChanges.get(block).add(new BlockChange(block));
    }

    public void recordBlockChange(Block block, OfflinePlayer player) {
        if (blockChanges.get(block) == null) {
            blockChanges.put(block, new ArrayList<BlockChange>());
        }
        blockChanges.get(block).add(new BlockChange(block, player));
    }

    public ItemStack[] getInventoryContentsAtTime(Inventory inventory, Date date) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Saves the yaml file
     */
    public void save() {
        File blockChangesFile = new File(plugin.getDataFolder(), "block-changes.yml");
        YamlConfiguration blockChangesConfig = new YamlConfiguration();
        for (Map.Entry<Block, List<BlockChange>> pair : blockChanges.entrySet()) {
            blockChangesConfig.set(LocationUtils.stringify(pair.getKey().getLocation()), pair.getValue());
        }
        try {
            blockChangesConfig.save(blockChangesFile);
        } catch (IOException exception) {
            plugin.getLogger().warning("Failed to store blockchanges yaml file: " + exception.getMessage());
        }
        //TODO save inventory changes
    }

    /**
     * loads the yaml file.
     */
    public void load() {
        File blockChangesFile = new File(plugin.getDataFolder(), "block-changes.yml");
        if (blockChangesFile.exists()) {
            YamlConfiguration blockChangesConfig = new YamlConfiguration();
            try {
                blockChangesConfig.load(blockChangesFile);
                for (String key : blockChangesConfig.getKeys(false)) {
                    Location location = LocationUtils.parseLocation(key);
                    blockChanges.put(location.getWorld().getBlockAt(location), (List<BlockChange>) blockChangesConfig.get(key));
                }
            } catch (FileNotFoundException exception) {
                plugin.getLogger().warning("blockchanges yaml file does not exist: " + exception.getMessage());
            } catch (IOException exception) {
                plugin.getLogger().warning("An error occured whenl loading the blockchanges yaml file: " + exception.getMessage());
            } catch (InvalidConfigurationException exception) {
                plugin.getLogger().warning("blockchanges yaml file has invalid configuration: " + exception.getMessage());
            }
        }
        //TODO load inventory changes
    }

    public void recordInventoryAddition(InventoryHolder holder, ItemStack item, OfflinePlayer player) {
        if (inventoryChanges.get(holder) == null) {
            inventoryChanges.put(holder, new ArrayList<InventoryChange>());
        }
        inventoryChanges.get(holder).add(new InventoryChange(item, InventoryChangeType.ADDITION, player));
    }

    public void recordInventoryAddition(InventoryHolder holder, ItemStack item) {
        if (inventoryChanges.get(holder) == null) {
            inventoryChanges.put(holder, new ArrayList<InventoryChange>());
        }
        inventoryChanges.get(holder).add(new InventoryChange(item, InventoryChangeType.ADDITION));
    }

    public void recordInventoryRemoval(InventoryHolder holder, ItemStack item, OfflinePlayer player) {
        if (inventoryChanges.get(holder) == null) {
            inventoryChanges.put(holder, new ArrayList<InventoryChange>());
        }
        inventoryChanges.get(holder).add(new InventoryChange(item, InventoryChangeType.REMOVAL, player));
    }

    public void recordInventoryRemoval(InventoryHolder holder, ItemStack item) {
        if (inventoryChanges.get(holder) == null) {
            inventoryChanges.put(holder, new ArrayList<InventoryChange>());
        }
        inventoryChanges.get(holder).add(new InventoryChange(item, InventoryChangeType.REMOVAL));
    }
}
