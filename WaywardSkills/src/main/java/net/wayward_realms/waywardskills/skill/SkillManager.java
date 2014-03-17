package net.wayward_realms.waywardskills.skill;

import net.wayward_realms.waywardlib.skills.Skill;
import net.wayward_realms.waywardskills.WaywardSkills;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SkillManager {

    private WaywardSkills plugin;

    private Map<String, Skill> skills = new HashMap<>();

    private Map<String, Map<Material, Skill>> boundSkills = new HashMap<>();
    private Map<String, Map<Skill, Long>> skillCooldowns = new HashMap<>();

    public SkillManager(WaywardSkills plugin) {
        this.plugin = plugin;
        ConfigurationSerialization.registerClass(PoisonArrowSkill.class);
        addSkill(new PoisonArrowSkill());
        ConfigurationSerialization.registerClass(SharpenSkill.class);
        addSkill(new SharpenSkill());
        ConfigurationSerialization.registerClass(QuickStepSkill.class);
        addSkill(new QuickStepSkill());
        ConfigurationSerialization.registerClass(DashSkill.class);
        addSkill(new DashSkill());
    }

    public Skill getSkill(String name) {
        return skills.get(name.toUpperCase());
    }

    public void addSkill(Skill skill) {
        skills.put(skill.getName().toUpperCase(), skill);
    }

    public void removeSkill(Skill skill) {
        skills.remove(skill.getName().toUpperCase());
    }

    public Collection<Skill> getSkills() {
        return skills.values();
    }

    public void bindSkill(Player player, Material type, Skill skill) {
        if (boundSkills.get(player.getName()) == null) {
            boundSkills.put(player.getName(), new HashMap<Material, Skill>());
        }
        boundSkills.get(player.getName()).put(type, skill);
    }

    public void unbindSkill(Player player, Material type) {
        if (boundSkills.get(player.getName()) != null) {
            boundSkills.get(player.getName()).remove(type);
        }
    }

    public Skill getBoundSkill(Player player, Material type) {
        if (boundSkills.get(player.getName()) != null) {
            if (boundSkills.get(player.getName()).get(type) != null) {
                return boundSkills.get(player.getName()).get(type);
            }
        }
        return null;
    }

    public void setSkillCooldownTime(Player player, Skill skill) {
        if (skillCooldowns.get(player.getName()) == null) {
            skillCooldowns.put(player.getName(), new HashMap<Skill, Long>());
        }
        skillCooldowns.get(player.getName()).put(skill, System.currentTimeMillis() + (skill.getCoolDown() * 1000));
    }

    public boolean hasCooledDown(Player player, Skill skill) {
        if (skillCooldowns.get(player.getName()) != null) {
            if (skillCooldowns.get(player.getName()).get(skill) != null) {
                return System.currentTimeMillis() - skillCooldowns.get(player.getName()).get(skill) >= 0;
            }
        }
        return true;
    }

    public int getCooldownTime(Player player, Skill skill) {
        return (int) ((skillCooldowns.get(player.getName()).get(skill) - System.currentTimeMillis()) / 1000);
    }

    public void saveBinds() {
        YamlConfiguration bindConfig = new YamlConfiguration();
        for (Map.Entry<String, Map<Material, Skill>> player : boundSkills.entrySet()) {
            for (Map.Entry<Material,Skill> skillBind : player.getValue().entrySet()) {
                bindConfig.set(player.getKey() + "." + skillBind.getKey().toString(), skillBind.getValue().getName());
            }
        }
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        File bindFile = new File(plugin.getDataFolder(), "skill-binds.yml");
        try {
            bindConfig.save(bindFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void loadBinds() {
        File bindFile = new File(plugin.getDataFolder(), "skill-binds.yml");
        if (bindFile.exists()) {
            YamlConfiguration bindConfig = new YamlConfiguration();
            try {
                bindConfig.load(bindFile);
            } catch (IOException | InvalidConfigurationException exception) {
                exception.printStackTrace();
            }
            for (String player : bindConfig.getKeys(false)) {
                boundSkills.put(player, new HashMap<Material, Skill>());
                for (String type : bindConfig.getConfigurationSection(player).getKeys(false)) {
                    boundSkills.get(player).put(Material.getMaterial(type), getSkill(bindConfig.getString(player + "." + type)));
                }
            }
        }
    }

}
