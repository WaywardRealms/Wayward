package net.wayward_realms.waywardmoderation;

import net.wayward_realms.waywardmoderation.util.BlockChange;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class LogManager {

    Map<Block, List<BlockChange>> blockChanges = new HashMap<>();
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

    public Map<Date, ItemStack> getInventoryContentChanges(Inventory inventory) {
        return null;
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

    public ItemStack[] getInventoryContentsAtTime(Inventory inventory, Date date) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Saves the yaml file
     */
    public void save() {
        final File blockChangesFile = new File(plugin.getDataFolder(), "block-changes.yml");
        final YamlConfiguration blockChangesConfig = new YamlConfiguration();
        final Iterator<Map.Entry<Block, List<BlockChange>>> iterator = blockChanges.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<Block, List<BlockChange>> pair = iterator.next();
            // storing the name of the material in each map.
            blockChangesConfig.set(pair.getKey().getType().toString(), pair);
            iterator.remove();
        }
        blockChangesConfig.set("material", blockChanges);
        try {
            blockChangesConfig.save(blockChangesFile);
        } catch (final IOException exception) {
            plugin.getLogger().warning("Failed to store blockchanges yaml file: " + exception.getMessage());
        }
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
            } catch (FileNotFoundException exception) {
                plugin.getLogger().warning("blockchanges yaml file does not exist: " + exception.getMessage());
            } catch (IOException exception) {
                plugin.getLogger().warning("An error occured whenl loading the blockchanges yaml file: " + exception.getMessage());
            } catch (InvalidConfigurationException exception) {
                plugin.getLogger().warning("blockchanges yaml file has invalid configuration: " + exception.getMessage());
            }
        }
    }

}
