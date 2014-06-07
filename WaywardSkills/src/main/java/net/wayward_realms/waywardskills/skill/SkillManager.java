package net.wayward_realms.waywardskills.skill;

import net.wayward_realms.waywardlib.skills.Skill;
import net.wayward_realms.waywardskills.WaywardSkills;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SkillManager {

    private WaywardSkills plugin;

    private Map<String, Skill> skills = new HashMap<>();

    private Map<String, Map<Skill, Long>> skillCooldowns = new HashMap<>();

    public SkillManager(WaywardSkills plugin) {
        this.plugin = plugin;
        addSkill(new ArrowSkill());
        addSkill(new BandageSkill());
        addSkill(new DashSkill());
        addSkill(new ItemSkill());
        addSkill(new PoisonArrowSkill());
        addSkill(new QuickStepSkill());
        addSkill(new SharpenSkill());
        addSkill(new SlashSkill());
        addSkill(new StabSkill());
        addSkill(new SpinningSweepSkill(plugin));
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
        File skillBindFile = new File(plugin.getDataFolder(), "skill-binds.yml");
        YamlConfiguration skillBindConfig = YamlConfiguration.loadConfiguration(skillBindFile);
        skillBindConfig.set(player.getName() + "." + type.toString(), skill.getName());
        try {
            skillBindConfig.save(skillBindFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void unbindSkill(Player player, Material type) {
        File skillBindFile = new File(plugin.getDataFolder(), "skill-binds.yml");
        YamlConfiguration skillBindConfig = YamlConfiguration.loadConfiguration(skillBindFile);
        skillBindConfig.set(player.getName() + "." + type.toString(), null);
        try {
            skillBindConfig.save(skillBindFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public Skill getBoundSkill(Player player, Material type) {
        File skillBindFile = new File(plugin.getDataFolder(), "skill-binds.yml");
        YamlConfiguration skillBindConfig = YamlConfiguration.loadConfiguration(skillBindFile);
        if (skillBindConfig.get(player.getName() + "." + type.toString()) != null) {
            return getSkill(skillBindConfig.getString(player.getName() + "." + type.toString(), null));
        } else {
            return null;
        }
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

}
