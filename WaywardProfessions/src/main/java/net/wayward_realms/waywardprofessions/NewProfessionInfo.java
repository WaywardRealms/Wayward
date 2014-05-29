package net.wayward_realms.waywardprofessions;

import net.wayward_realms.waywardlib.professions.ToolType;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class NewProfessionInfo {

    private File file;

    public NewProfessionInfo(File file) {
        this.file = file;
    }

    public int getMaxToolDurability(ToolType type) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (config.get("max-tool-durability." + type.toString().toLowerCase()) == null) return 10;
        return Math.max(config.getInt("max-tool-durability." + type), 10);
    }

    public void setMaxToolDurability(ToolType type, int durability) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("max-tool-durability." + type.toString().toLowerCase(), durability);
        try {
            config.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public int getCraftEfficiency(Material material) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (config.get("craft-efficiency." + material.toString().toLowerCase()) == null) return 0;
        return config.getInt("craft-efficiency." + material.toString().toLowerCase());
    }

    public void setCraftEfficiency(Material material, int efficiency) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("craft-efficiency." + material.toString().toLowerCase(), Math.min(efficiency, 100));
        try {
            config.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public int getMiningEfficiency(Material material) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (config.get("mining-efficiency." + material.toString().toLowerCase()) == null) return 0;
        return config.getInt("mining-efficiency." + material.toString().toLowerCase());
    }

    public void setMiningEfficiency(Material material, int efficiency) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("mining-efficiency." + material.toString().toLowerCase(), Math.min(efficiency, 100));
        try {
            config.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public int getBrewingEfficiency() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        return config.getInt("brewing-efficiency");
    }

    public void setBrewingEfficiency(int efficiency) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("brewing-efficiency", Math.min(efficiency, 100));
        try {
            config.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void save() {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
