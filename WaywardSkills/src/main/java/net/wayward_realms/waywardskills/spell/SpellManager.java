package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.skills.Spell;
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

public class SpellManager {

    private WaywardSkills plugin;

    private Map<String, Spell> spells = new HashMap<>();

    private Map<String, Map<Material, Spell>> boundSpells = new HashMap<>();
    private Map<String, Map<Spell, Long>> spellCooldowns = new HashMap<>();

    public SpellManager(WaywardSkills plugin) {
        this.plugin = plugin;
        addSpell(new FireballSpell());
        addSpell(new IceboltSpell());
        addSpell(new FreezeSpell());
        addSpell(new BurnSpell());
        addSpell(new EsunaSpell());
        addSpell(new MagicMissileSpell());
        addSpell(new CureSpell());
        addSpell(new MageArmourSpell());
        addSpell(new DrawSwordSpell());
        addSpell(new EmpowerSpell());
        addSpell(new SummonZombieSpell(plugin));
        addSpell(new SummonSkeletonSpell(plugin));
    }

    public Spell getSpell(String name) {
        return spells.get(name.toUpperCase());
    }

    public void addSpell(Spell spell) {
        ConfigurationSerialization.registerClass(spell.getClass());
        spells.put(spell.getName().toUpperCase(), spell);
    }

    public void removeSpell(Spell spell) {
        spells.remove(spell.getName().toUpperCase());
    }

    public Collection<Spell> getSpells() {
        return spells.values();
    }

    public void bindSpell(Player player, Material type, Spell spell) {
        if (boundSpells.get(player.getName()) == null) {
            boundSpells.put(player.getName(), new HashMap<Material, Spell>());
        }
        boundSpells.get(player.getName()).put(type, spell);
    }

    public void unbindSpell(Player player, Material type) {
        if (boundSpells.get(player.getName()) != null) {
            boundSpells.get(player.getName()).remove(type);
        }
    }

    public Spell getBoundSpell(Player player, Material type) {
        if (boundSpells.get(player.getName()) != null) {
            if (boundSpells.get(player.getName()).get(type) != null) {
                return boundSpells.get(player.getName()).get(type);
            }
        }
        return null;
    }

    public void setSpellCooldownTime(Player player, Spell spell) {
        if (spellCooldowns.get(player.getName()) == null) {
            spellCooldowns.put(player.getName(), new HashMap<Spell, Long>());
        }
        spellCooldowns.get(player.getName()).put(spell, System.currentTimeMillis() + (spell.getCoolDown() * 1000));
    }

    public boolean hasCooledDown(Player player, Spell spell) {
        if (spellCooldowns.get(player.getName()) != null) {
            if (spellCooldowns.get(player.getName()).get(spell) != null) {
                return System.currentTimeMillis() - spellCooldowns.get(player.getName()).get(spell) >= 0;
            }
        }
        return true;
    }

    public int getCooldownTime(Player player, Spell spell) {
        return (int) ((spellCooldowns.get(player.getName()).get(spell) - System.currentTimeMillis()) / 1000);
    }

    public void saveBinds() {
        YamlConfiguration bindConfig = new YamlConfiguration();
        for (Map.Entry<String, Map<Material, Spell>> player : boundSpells.entrySet()) {
            for (Map.Entry<Material,Spell> spellBind : player.getValue().entrySet()) {
                bindConfig.set(player.getKey() + "." + spellBind.getKey().toString(), spellBind.getValue().getName());
            }
        }
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        File bindFile = new File(plugin.getDataFolder(), "spell-binds.yml");
        try {
            bindConfig.save(bindFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void loadBinds() {
        File oldBindFile = new File(plugin.getDataFolder(), "binds.yml");
        File bindFile = new File(plugin.getDataFolder(), "spell-binds.yml");
        if (oldBindFile.exists()) {
            oldBindFile.renameTo(bindFile);
        }
        if (bindFile.exists()) {
            YamlConfiguration bindConfig = new YamlConfiguration();
            try {
                bindConfig.load(bindFile);
            } catch (IOException | InvalidConfigurationException exception) {
                exception.printStackTrace();
            }
            for (String player : bindConfig.getKeys(false)) {
                boundSpells.put(player, new HashMap<Material, Spell>());
                for (String type : bindConfig.getConfigurationSection(player).getKeys(false)) {
                    boundSpells.get(player).put(Material.getMaterial(type), getSpell(bindConfig.getString(player + "." + type)));
                }
            }
        }
    }

}
