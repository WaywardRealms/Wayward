package net.wayward_realms.waywardprofessions;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.professions.ProfessionsPlugin;
import net.wayward_realms.waywardlib.professions.ToolType;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WaywardProfessions extends JavaPlugin implements ProfessionsPlugin {

    private Map<Integer, ProfessionInfo> professionInfo = new HashMap<>();

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(ProfessionInfo.class);
        registerListeners(new BlockBreakListener(this), new EnchantItemListener(), new InventoryClickListener(this), new PrepareItemCraftListener(this));
        getCommand("efficiency").setExecutor(new EfficiencyCommand(this));
        getCommand("durability").setExecutor(new DurabilityCommand(this));
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
        professionInfo.get(character.getId()).setMaxToolDurability(toolType, durability);
    }

    @Override
    public int getCraftEfficiency(Character character, Material material) {
        initialiseProfessionInfo(character);
        return professionInfo.get(character.getId()).getCraftEfficiency(material);
    }

    public void setCraftEfficiency(Character character, Material material, int efficiency) {
        initialiseProfessionInfo(character);
        professionInfo.get(character.getId()).setCraftEfficiency(material, efficiency);
    }

    @Override
    public int getMiningEfficiency(Character character, Material material) {
        initialiseProfessionInfo(character);
        return professionInfo.get(character.getId()).getMiningEfficiency(material);
    }

    public void setMiningEfficiency(Character character, Material material, int efficiency) {
        initialiseProfessionInfo(character);
        professionInfo.get(character.getId()).setMiningEfficiency(material, efficiency);
    }

    @Override
    public int getBrewingEfficiency(Character character) {
        initialiseProfessionInfo(character);
        return professionInfo.get(character.getId()).getBrewingEfficiency();
    }

    public void setBrewingEfficiency(Character character, int efficiency) {
        initialiseProfessionInfo(character);
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
