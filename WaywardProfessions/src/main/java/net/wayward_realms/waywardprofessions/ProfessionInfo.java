package net.wayward_realms.waywardprofessions;

import net.wayward_realms.waywardlib.professions.ProfessionsPlugin;
import net.wayward_realms.waywardlib.professions.ToolType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class ProfessionInfo implements ConfigurationSerializable {

    private int characterId;

    private Map<ToolType, Integer> maxToolDurability = new EnumMap<>(ToolType.class);
    private Map<Material, Integer> craftEfficiency = new EnumMap<>(Material.class);
    private Map<Material, Integer> miningEfficiency = new EnumMap<>(Material.class);
    private int brewingEfficiency;

    public int getMaxToolDurability(ToolType type) {
        if (maxToolDurability.get(type) == null) return 10;
        return Math.max(maxToolDurability.get(type), 10);
    }

    public void setMaxToolDurability(ToolType type, int durability) {
        maxToolDurability.put(type, durability);
    }

    public int getCraftEfficiency(Material material) {
        if (craftEfficiency.get(material) == null) return 0;
        return craftEfficiency.get(material);
    }

    public void setCraftEfficiency(Material material, int efficiency) {
        craftEfficiency.put(material, Math.min(efficiency, 100));
    }

    public int getMiningEfficiency(Material material) {
        if (miningEfficiency.get(material) == null) return 0;
        return miningEfficiency.get(material);
    }

    public void setMiningEfficiency(Material material, int efficiency) {
        miningEfficiency.put(material, Math.min(efficiency, 100));
    }

    public int getBrewingEfficiency() {
        return brewingEfficiency;
    }

    public void setBrewingEfficiency(int efficiency) {
        this.brewingEfficiency = Math.min(efficiency, 100);
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getCharacterId() {
        return characterId;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        Map<String, Integer> maxToolDurabilityNames = new HashMap<>();
        for (Map.Entry<ToolType, Integer> entry : maxToolDurability.entrySet()) {
            maxToolDurabilityNames.put(entry.getKey().toString(), entry.getValue());
        }
        serialised.put("max-tool-durability", maxToolDurabilityNames);
        Map<String, Integer> craftEfficiencyNames = new HashMap<>();
        for (Map.Entry<Material, Integer> entry : craftEfficiency.entrySet()) {
            craftEfficiencyNames.put(entry.getKey().toString(), entry.getValue());
        }
        serialised.put("craft-efficiency", craftEfficiencyNames);
        Map<String, Integer> miningEfficiencyNames = new HashMap<>();
        for (Map.Entry<Material, Integer> entry : miningEfficiency.entrySet()) {
            miningEfficiencyNames.put(entry.getKey().toString(), entry.getValue());
        }
        serialised.put("mining-efficiency", miningEfficiencyNames);
        serialised.put("brewing-efficiency", brewingEfficiency);
        return serialised;
    }

    public static ProfessionInfo deserialize(Map<String, Object> serialised) {
        ProfessionInfo deserialised = new ProfessionInfo();
        for (Map.Entry<String, Integer> entry : ((Map<String, Integer>) serialised.get("max-tool-durability")).entrySet()) {
            deserialised.maxToolDurability.put(ToolType.valueOf(entry.getKey()), entry.getValue());
        }
        for (Map.Entry<String, Integer> entry : ((Map<String, Integer>) serialised.get("craft-efficiency")).entrySet()) {
            deserialised.craftEfficiency.put(Material.getMaterial(entry.getKey()), entry.getValue());
        }
        for (Map.Entry<String, Integer> entry : ((Map<String, Integer>) serialised.get("mining-efficiency")).entrySet()) {
            deserialised.miningEfficiency.put(Material.getMaterial(entry.getKey()), entry.getValue());
        }
        deserialised.brewingEfficiency = (int) serialised.get("brewing-efficiency");
        return deserialised;
    }

    public NewProfessionInfo toNewProfessionInfo() {
        ProfessionsPlugin professionsPlugin = Bukkit.getServer().getServicesManager().getRegistration(ProfessionsPlugin.class).getProvider();
        File newProfessionsDirectory = new File(professionsPlugin.getDataFolder(), "characters-new");
        return new NewProfessionInfo(new File(newProfessionsDirectory, characterId + ".yml"));
    }

}
