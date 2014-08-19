package net.wayward_realms.waywardclasses;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.classes.Class;
import net.wayward_realms.waywardlib.classes.ClassesPlugin;
import net.wayward_realms.waywardlib.skills.Stat;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class ClassImpl implements Class, ConfigurationSerializable {

    private String name;
    private double hpBonus;
    private int manaBonus;
    private Map<Stat, Integer> statBonuses = new EnumMap<>(Stat.class);
    private Map<Class, Integer> prerequisites = new HashMap<>();
    private int maxLevel;

    public ClassImpl() {}

    public ClassImpl(String name) {
        this(name, 10D, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    public ClassImpl(String name, double hpBonus, int meleeAttackBonus, int rangedAttackBonus, int magicAttackBonus, int meleeDefenceBonus, int rangedDefenceBonus, int magicDefenceBonus, int speedBonus, int manaBonus) {
        this(name, hpBonus, meleeAttackBonus, rangedAttackBonus, magicAttackBonus, meleeDefenceBonus, rangedDefenceBonus, magicDefenceBonus, speedBonus, manaBonus, 1);
    }

    public ClassImpl(String name, double hpBonus, int meleeAttackBonus, int rangedAttackBonus, int magicAttackBonus, int meleeDefenceBonus, int rangedDefenceBonus, int magicDefenceBonus, int speedBonus, int manaBonus, int maxLevel) {
        this(name, hpBonus, meleeAttackBonus, rangedAttackBonus, magicAttackBonus, meleeDefenceBonus, rangedDefenceBonus, magicDefenceBonus, speedBonus, manaBonus, maxLevel, new HashMap<Class, Integer>());
    }

    public ClassImpl(String name, double hpBonus, int meleeAttackBonus, int rangedAttackBonus, int magicAttackBonus, int meleeDefenceBonus, int rangedDefenceBonus, int magicDefenceBonus, int speedBonus, int manaBonus, int maxLevel, Map<Class, Integer> prerequisites) {
        this.name = name;
        this.hpBonus = hpBonus;
        setStatBonus(Stat.MELEE_ATTACK, meleeAttackBonus);
        setStatBonus(Stat.MELEE_DEFENCE, meleeDefenceBonus);
        setStatBonus(Stat.RANGED_ATTACK, rangedAttackBonus);
        setStatBonus(Stat.RANGED_DEFENCE, rangedDefenceBonus);
        setStatBonus(Stat.MAGIC_ATTACK, magicAttackBonus);
        setStatBonus(Stat.MAGIC_DEFENCE, magicDefenceBonus);
        setStatBonus(Stat.SPEED, speedBonus);
        this.manaBonus = manaBonus;
        this.maxLevel = maxLevel;
        this.prerequisites.putAll(prerequisites);
    }

    public ClassImpl(String name, double hpBonus, int manaBonus, Map<Stat, Integer> statBonuses, Map<Class, Integer> prerequisites, int maxLevel) {
        this.name = name;
        this.hpBonus = hpBonus;
        this.manaBonus = manaBonus;
        this.statBonuses.putAll(statBonuses);
        this.prerequisites.putAll(prerequisites);
        this.maxLevel = maxLevel;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean hasPrerequisites(Character character) {
        ClassesPlugin plugin = Bukkit.getServer().getServicesManager().getRegistration(ClassesPlugin.class).getProvider();
        for (Class clazz : prerequisites.keySet()) {
            if (plugin.getLevel(character, clazz) < prerequisites.get(clazz)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Map<Class, Integer> getPrerequisites() {
        return prerequisites;
    }

    @Override
    public void addPrerequisite(Class clazz, int level) {
        prerequisites.put(clazz, level);
    }

    @Override
    public void removePrerequisite(Class clazz) {
        prerequisites.remove(clazz);
    }

    @Override
    public double getHpBonus() {
        return hpBonus;
    }

    @Override
    public void setHpBonus(double hpBonus) {
        this.hpBonus = hpBonus;
    }

    @Override
    public int getStatBonus(Stat stat) {
        return statBonuses.get(stat) != null ? statBonuses.get(stat) : 0;
    }

    @Override
    public void setStatBonus(Stat stat, int bonus) {
        statBonuses.put(stat, bonus);
    }

    @Override
    public int getManaBonus() {
        return manaBonus;
    }

    @Override
    public void setManaBonus(int manaBonus) {
        this.manaBonus = manaBonus;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("name", name);
        serialised.put("hp-bonus", hpBonus);
        serialised.put("melee-attack-bonus", getStatBonus(Stat.MELEE_ATTACK));
        serialised.put("ranged-attack-bonus", getStatBonus(Stat.RANGED_ATTACK));
        serialised.put("magic-attack-bonus", getStatBonus(Stat.MAGIC_ATTACK));
        serialised.put("melee-defence-bonus", getStatBonus(Stat.MELEE_DEFENCE));
        serialised.put("ranged-defence-bonus", getStatBonus(Stat.RANGED_DEFENCE));
        serialised.put("magic-defence-bonus", getStatBonus(Stat.MAGIC_DEFENCE));
        serialised.put("speed-bonus", getStatBonus(Stat.SPEED));
        serialised.put("mana-bonus", manaBonus);
        serialised.put("max-level", maxLevel);
        serialised.put("prerequisites", prerequisites);
        return serialised;
    }

    public static ClassImpl deserialize(Map<String, Object> serialised) {
        ClassImpl deserialised = new ClassImpl();
        deserialised.name = (String) serialised.get("name");
        deserialised.hpBonus = (double) serialised.get("hp-bonus");
        deserialised.setStatBonus(Stat.MELEE_ATTACK, (int) serialised.get("melee-attack-bonus"));
        deserialised.setStatBonus(Stat.RANGED_ATTACK, (int) serialised.get("ranged-attack-bonus"));
        deserialised.setStatBonus(Stat.MAGIC_ATTACK, (int) serialised.get("magic-attack-bonus"));
        deserialised.setStatBonus(Stat.MELEE_DEFENCE, (int) serialised.get("melee-defence-bonus"));
        deserialised.setStatBonus(Stat.RANGED_DEFENCE, (int) serialised.get("ranged-defence-bonus"));
        deserialised.setStatBonus(Stat.MAGIC_DEFENCE, (int) serialised.get("magic-defence-bonus"));
        deserialised.setStatBonus(Stat.SPEED, (int) serialised.get("speed-bonus"));
        deserialised.manaBonus = (int) serialised.get("mana-bonus");
        deserialised.maxLevel = (int) serialised.get("max-level");
        deserialised.prerequisites = (Map<Class, Integer>) serialised.get("prerequisites");
        return deserialised;
    }

}
