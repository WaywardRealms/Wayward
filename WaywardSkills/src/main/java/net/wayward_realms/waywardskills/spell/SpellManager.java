package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.skills.Spell;
import net.wayward_realms.waywardskills.WaywardSkills;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SpellManager {

    private WaywardSkills plugin;

    private Map<String, Spell> spells = new HashMap<>();

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
        addSpell(new MageArmourSpell(plugin));
        addSpell(new DrawSwordSpell());
        addSpell(new EmpowerSpell());
        addSpell(new SummonZombieSpell(plugin));
        addSpell(new SummonSkeletonSpell(plugin));
        addSpell(new SummonSpiderSpell(plugin));
        addSpell(new BlizzardSpell(plugin));
        addSpell(new IceBreathSpell(plugin));
        addSpell(new FireSwordSpell());
        addSpell(new LevitationSpell(plugin));
        addSpell(new CloakOfShadowsSpell(plugin));
        addSpell(new OvergrowthSpell());
        addSpell(new LightningSwordSpell());
        addSpell(new BlizzardSpell(plugin));
        addSpell(new LightningSpell());
        addSpell(new ExplosiveSpellSpell());
        addSpell(new BlizzardSwordSpell());
        addSpell(new FireBreathSpell(plugin));
        addSpell(new ShieldBarrierSpell(plugin));
        addSpell(new BlinkSpell(plugin));
        addSpell(new DoomSpell(plugin));
    }

    public Spell getSpell(String name) {
        return spells.get(name.toUpperCase());
    }

    public void addSpell(Spell spell) {
        spells.put(spell.getName().toUpperCase(), spell);
    }

    public void removeSpell(Spell spell) {
        spells.remove(spell.getName().toUpperCase());
    }

    public Collection<Spell> getSpells() {
        return spells.values();
    }

    public void bindSpell(Player player, Material type, Spell spell) {
        File spellBindFile = new File(plugin.getDataFolder(), "spell-binds.yml");
        YamlConfiguration spellBindConfig = YamlConfiguration.loadConfiguration(spellBindFile);
        spellBindConfig.set(player.getName() + "." + type.toString(), spell.getName());
        try {
            spellBindConfig.save(spellBindFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void unbindSpell(Player player, Material type) {
        File spellBindFile = new File(plugin.getDataFolder(), "spell-binds.yml");
        YamlConfiguration spellBindConfig = YamlConfiguration.loadConfiguration(spellBindFile);
        spellBindConfig.set(player.getName() + "." + type.toString(), null);
        try {
            spellBindConfig.save(spellBindFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public Spell getBoundSpell(Player player, Material type) {
        File spellBindFile = new File(plugin.getDataFolder(), "spell-binds.yml");
        YamlConfiguration spellBindConfig = YamlConfiguration.loadConfiguration(spellBindFile);
        if (spellBindConfig.get(player.getName() + "." + type.toString()) != null) {
            return getSpell(spellBindConfig.getString(player.getName() + "." + type.toString(), null));
        } else {
            return null;
        }
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

}
