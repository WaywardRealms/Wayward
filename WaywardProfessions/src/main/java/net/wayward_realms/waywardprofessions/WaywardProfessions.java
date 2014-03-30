package net.wayward_realms.waywardprofessions;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.professions.ProfessionsPlugin;
import net.wayward_realms.waywardlib.professions.ToolType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.bukkit.Material.*;

public class WaywardProfessions extends JavaPlugin implements ProfessionsPlugin {

    private Map<Integer, ProfessionInfo> professionInfo = new HashMap<>();

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(ProfessionInfo.class);
        registerListeners(new BlockBreakListener(this), new EnchantItemListener(), new InventoryClickListener(this), new PrepareItemCraftListener(this));
        getCommand("efficiency").setExecutor(new EfficiencyCommand(this));
        getCommand("durability").setExecutor(new DurabilityCommand(this));
        getCommand("profession").setExecutor(new ProfessionCommand(this));
    }

    @Override
    public void onDisable() {
        saveState();
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    @Override
    public int getMaxToolDurability(Character character, ToolType toolType) {
        initialiseProfessionInfo(character);
        return professionInfo.get(character.getId()).getMaxToolDurability(toolType);
    }

    public void setMaxToolDurability(Character character, ToolType toolType, int durability) {
        initialiseProfessionInfo(character);
        if (getMaxToolDurability(character, toolType) < durability) {
            if (character.getPlayer().isOnline()) {
                character.getPlayer().getPlayer().sendMessage(getPrefix() + ChatColor.GREEN + "+" + (durability - getMaxToolDurability(character, toolType)) + " maximum " + toolType.toString().toLowerCase().replace('_', ' ') + " durability");
            }
        }
        professionInfo.get(character.getId()).setMaxToolDurability(toolType, durability);
    }

    @Override
    public int getCraftEfficiency(Character character, Material material) {
        if (!canGainCraftEfficency(material)) return 0;
        initialiseProfessionInfo(character);
        return professionInfo.get(character.getId()).getCraftEfficiency(material);
    }

    public void setCraftEfficiency(Character character, Material material, int efficiency) {
        if (!canGainCraftEfficency(material)) return;
        initialiseProfessionInfo(character);
        if (getCraftEfficiency(character, material) < efficiency && getCraftEfficiency(character, material) < 100) {
            if (character.getPlayer().isOnline()) {
                character.getPlayer().getPlayer().sendMessage(getPrefix() + ChatColor.GREEN + "+" + (efficiency - getCraftEfficiency(character, material)) + " " + material.toString().toLowerCase().replace('_', ' ') + " crafting efficiency");
            }
        }
        professionInfo.get(character.getId()).setCraftEfficiency(material, efficiency);
    }

    public boolean canGainCraftEfficency(Material material) {
        File professionsWhitelistFile = new File(getDataFolder(), "whitelist.yml");
        if (professionsWhitelistFile.exists()) {
            YamlConfiguration professionsWhitelist = new YamlConfiguration();
            try {
                professionsWhitelist.load(professionsWhitelistFile);
            } catch (IOException | InvalidConfigurationException exception) {
                exception.printStackTrace();
            }
            return professionsWhitelist.getStringList("craft").contains(material.toString());
        }
        return true;
    }

    @Override
    public int getMiningEfficiency(Character character, Material material) {
        if (!canGainMiningEfficiency(material)) return 0;
        initialiseProfessionInfo(character);
        return professionInfo.get(character.getId()).getMiningEfficiency(material);
    }

    public void setMiningEfficiency(Character character, Material material, int efficiency) {
        if (!canGainMiningEfficiency(material)) return;
        initialiseProfessionInfo(character);
        if (getMiningEfficiency(character, material) < efficiency && getMiningEfficiency(character, material) < 100) {
            if (character.getPlayer().isOnline()) {
                character.getPlayer().getPlayer().sendMessage(getPrefix() + ChatColor.GREEN + "+" + (efficiency - getMiningEfficiency(character, material)) + " " + material.toString().toLowerCase().replace('_', ' ') + " mining efficiency");
            }
        }
        professionInfo.get(character.getId()).setMiningEfficiency(material, efficiency);
    }

    public boolean canGainMiningEfficiency(Material material) {
        File professionsWhitelistFile = new File(getDataFolder(), "whitelist.yml");
        if (professionsWhitelistFile.exists()) {
            YamlConfiguration professionsWhitelist = new YamlConfiguration();
            try {
                professionsWhitelist.load(professionsWhitelistFile);
            } catch (IOException | InvalidConfigurationException exception) {
                exception.printStackTrace();
            }
            return professionsWhitelist.getStringList("mine").contains(material.toString());
        }
        return true;
    }

    @Override
    public int getBrewingEfficiency(Character character) {
        initialiseProfessionInfo(character);
        return professionInfo.get(character.getId()).getBrewingEfficiency();
    }

    public void setBrewingEfficiency(Character character, int efficiency) {
        initialiseProfessionInfo(character);
        if (getBrewingEfficiency(character) < efficiency && getBrewingEfficiency(character) < 100) {
            if (character.getPlayer().isOnline()) {
                character.getPlayer().getPlayer().sendMessage(getPrefix() + ChatColor.GREEN + "+" + (efficiency - getBrewingEfficiency(character)) + " brewing efficiency");
            }
        }
        professionInfo.get(character.getId()).setBrewingEfficiency(efficiency);
    }

    private void initialiseProfessionInfo(Character character) {
        if (professionInfo.get(character.getId()) == null) {
            professionInfo.put(character.getId(), new ProfessionInfo());
        }
    }

    @Override
    public String getPrefix() {
        return "";
    }

    @Override
    public void loadState() {
        File professionsWhitelistFile = new File(getDataFolder(), "whitelist.yml");
        if (!professionsWhitelistFile.exists()) {
            YamlConfiguration professionsWhitelist = new YamlConfiguration();
            professionsWhitelist.set("craft", Arrays.asList(
                    WOOD_PICKAXE.toString(), WOOD_HOE.toString(), WOOD_AXE.toString(), WOOD_SWORD.toString(), WOOD_SPADE.toString(),
                    STONE_PICKAXE.toString(), STONE_HOE.toString(), STONE_AXE.toString(), STONE_SWORD.toString(), STONE_SPADE.toString(),
                    IRON_PICKAXE.toString(), IRON_HOE.toString(), IRON_AXE.toString(), IRON_SWORD.toString(), IRON_SPADE.toString(),
                    GOLD_PICKAXE.toString(), GOLD_HOE.toString(), GOLD_AXE.toString(), GOLD_SWORD.toString(), GOLD_SPADE.toString(),
                    DIAMOND_PICKAXE.toString(), DIAMOND_HOE.toString(), DIAMOND_AXE.toString(), DIAMOND_SWORD.toString(), DIAMOND_SPADE.toString(),
                    LEATHER_HELMET.toString(), LEATHER_CHESTPLATE.toString(), LEATHER_LEGGINGS.toString(), LEATHER_BOOTS.toString(),
                    CHAINMAIL_HELMET.toString(), CHAINMAIL_CHESTPLATE.toString(), CHAINMAIL_LEGGINGS.toString(), CHAINMAIL_BOOTS.toString(),
                    IRON_HELMET.toString(), IRON_CHESTPLATE.toString(), IRON_LEGGINGS.toString(), IRON_BOOTS.toString(),
                    DIAMOND_HELMET.toString(), DIAMOND_CHESTPLATE.toString(), DIAMOND_LEGGINGS.toString(), DIAMOND_BOOTS.toString(),
                    BOW.toString(), ARROW.toString(),
                    ACTIVATOR_RAIL.toString(), DETECTOR_RAIL.toString(), POWERED_RAIL.toString(), RAILS.toString()
            ));
            professionsWhitelist.set("mine", Arrays.asList(
                    COAL_ORE.toString(), IRON_ORE.toString(), DIAMOND_ORE.toString(), EMERALD_ORE.toString(), REDSTONE_ORE.toString(), GLOWING_REDSTONE_ORE.toString(), GOLD_ORE.toString(), LAPIS_ORE.toString(), QUARTZ_ORE.toString(),
                    STONE.toString(), OBSIDIAN.toString(), GRAVEL.toString(), SMOOTH_BRICK.toString(), SAND.toString(), SANDSTONE.toString(), GRASS.toString(), DIRT.toString(),
                    CACTUS.toString(), LOG.toString(), LOG_2.toString(), LEAVES.toString(), LEAVES_2.toString(),
                    SOUL_SAND.toString(), NETHERRACK.toString(), GLOWSTONE.toString(), NETHER_BRICK.toString(),
                    PUMPKIN.toString(), MELON_BLOCK.toString(), SEEDS.toString(), LONG_GRASS.toString(), WHEAT.toString(), SUGAR_CANE_BLOCK.toString(), SUGAR_CANE.toString(), CARROT.toString(), POTATO.toString(), COCOA.toString(), BROWN_MUSHROOM.toString(), RED_MUSHROOM.toString(), NETHER_WARTS.toString(), NETHER_STALK.toString(),
                    SNOW.toString(), SNOW_BLOCK.toString()
            ));
            try {
                professionsWhitelist.save(professionsWhitelistFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        File characterDirectory = new File(getDataFolder(), "characters");
        if (characterDirectory.exists()) {
            for (File file : characterDirectory.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.getPath().endsWith(".yml");
                }
            })) {
                YamlConfiguration characterConfig = new YamlConfiguration();
                try {
                    characterConfig.load(file);
                } catch (IOException | InvalidConfigurationException exception) {
                    exception.printStackTrace();
                }
                ProfessionInfo professionInfo = (ProfessionInfo) characterConfig.get("profession-info");
                try {
                    int characterId = Integer.parseInt(file.getName().replace(".yml", ""));
                    this.professionInfo.put(characterId, professionInfo);
                } catch (NumberFormatException ignore) {}
            }
        }
    }

    @Override
    public void saveState() {
        File characterDirectory = new File(getDataFolder(), "characters");
        for (Map.Entry<Integer, ProfessionInfo> entry : professionInfo.entrySet()) {
            File professionInfoFile = new File(characterDirectory, entry.getKey() + ".yml");
            YamlConfiguration professionInfoConfig = new YamlConfiguration();
            professionInfoConfig.set("profession-info", entry.getValue());
            try {
                professionInfoConfig.save(professionInfoFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

}
