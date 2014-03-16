package net.wayward_realms.waywardclasses;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.classes.Class;
import net.wayward_realms.waywardlib.classes.ClassesPlugin;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.skills.SkillType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class ClassImpl implements Class, ConfigurationSerializable {

    private String name;
    private double hpBonus;
    private int manaBonus;
    private Map<Stat, Integer> statBonuses = new HashMap<>();
    private Map<SkillType, Integer> skillPointBonuses = new HashMap<>();
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

    public ClassImpl(String name, double hpBonus, int manaBonus, Map<Stat, Integer> statBonuses, Map<SkillType, Integer> skillPointBonuses, Map<Class, Integer> prerequisites, int maxLevel) {
        this.name = name;
        this.hpBonus = hpBonus;
        this.manaBonus = manaBonus;
        this.statBonuses.putAll(statBonuses);
        this.skillPointBonuses.putAll(skillPointBonuses);
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
        return statBonuses.get(stat);
    }

    @Override
    public void setStatBonus(Stat stat, int bonus) {
        statBonuses.put(stat, bonus);
    }

    @Override
    public int getMeleeAttackBonus() {
        return getStatBonus(Stat.MELEE_ATTACK);
    }

    @Override
    public void setMeleeAttackBonus(int attackBonus) {
        setStatBonus(Stat.MELEE_ATTACK, attackBonus);
    }

    @Override
    public int getRangedAttackBonus() {
        return getStatBonus(Stat.RANGED_ATTACK);
    }

    @Override
    public void setRangedAttackBonus(int attackBonus) {
        setStatBonus(Stat.RANGED_ATTACK, attackBonus);
    }

    @Override
    public int getMagicAttackBonus() {
        return getStatBonus(Stat.MAGIC_ATTACK);
    }

    @Override
    public void setMagicAttackBonus(int attackBonus) {
        setStatBonus(Stat.MAGIC_ATTACK, attackBonus);
    }

    @Override
    public int getMeleeDefenceBonus() {
        return getStatBonus(Stat.MELEE_DEFENCE);
    }

    @Override
    public void setMeleeDefenceBonus(int defenceBonus) {
        setStatBonus(Stat.MELEE_DEFENCE, defenceBonus);
    }

    @Override
    public int getRangedDefenceBonus() {
        return getStatBonus(Stat.RANGED_DEFENCE);
    }

    @Override
    public void setRangedDefenceBonus(int defenceBonus) {
        setStatBonus(Stat.RANGED_DEFENCE, defenceBonus);
    }

    @Override
    public int getMagicDefenceBonus() {
        return getStatBonus(Stat.MAGIC_DEFENCE);
    }

    @Override
    public void setMagicDefenceBonus(int defenceBonus) {
        setStatBonus(Stat.MAGIC_DEFENCE, defenceBonus);
    }

    @Override
    public int getSpeedBonus() {
        return getStatBonus(Stat.SPEED);
    }

    @Override
    public void setSpeedBonus(int speedBonus) {
        setStatBonus(Stat.SPEED, speedBonus);
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
    public int getSkillPointBonus(SkillType type) {
        return skillPointBonuses.get(type);
    }

    @Override
    public void setSkillPointBonus(SkillType type, int bonus) {
        skillPointBonuses.put(type, bonus);
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
        serialised.put("melee-offence-skill-point-bonus", getSkillPointBonus(SkillType.MELEE_OFFENCE));
        serialised.put("melee-defence-skill-point-bonus", getSkillPointBonus(SkillType.MELEE_DEFENCE));
        serialised.put("ranged-offence-skill-point-bonus", getSkillPointBonus(SkillType.RANGED_OFFENCE));
        serialised.put("ranged-defence-skill-point-bonus", getSkillPointBonus(SkillType.RANGED_DEFENCE));
        serialised.put("magic-offence-skill-point-bonus", getSkillPointBonus(SkillType.MAGIC_OFFENCE));
        serialised.put("magic-defence-skill-point-bonus", getSkillPointBonus(SkillType.MAGIC_DEFENCE));
        serialised.put("magic-healing-skill-point-bonus", getSkillPointBonus(SkillType.MAGIC_HEALING));
        serialised.put("magic-nature-skill-point-bonus", getSkillPointBonus(SkillType.MAGIC_NATURE));
        serialised.put("magic-illusion-skill-point-bonus", getSkillPointBonus(SkillType.MAGIC_ILLUSION));
        serialised.put("magic-summoning-skill-point-bonus", getSkillPointBonus(SkillType.MAGIC_SUMMONING));
        serialised.put("magic-sword-skill-point-bonus", getSkillPointBonus(SkillType.MAGIC_SWORD));
        serialised.put("speed-nimble-skill-point-bonus", getSkillPointBonus(SkillType.SPEED_NIMBLE));
        serialised.put("support-perform-skill-point-bonus", getSkillPointBonus(SkillType.SUPPORT_PERFORM));
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
        deserialised.setSkillPointBonus(SkillType.MELEE_OFFENCE, (int) serialised.get("melee-offence-skill-point-bonus"));
        deserialised.setSkillPointBonus(SkillType.MELEE_DEFENCE, (int) serialised.get("melee-defence-skill-point-bonus"));
        deserialised.setSkillPointBonus(SkillType.RANGED_OFFENCE, (int) serialised.get("ranged-offence-skill-point-bonus"));
        deserialised.setSkillPointBonus(SkillType.RANGED_DEFENCE, (int) serialised.get("ranged-defence-skill-point-bonus"));
        deserialised.setSkillPointBonus(SkillType.MAGIC_OFFENCE, (int) serialised.get("magic-offence-skill-point-bonus"));
        deserialised.setSkillPointBonus(SkillType.MAGIC_DEFENCE, (int) serialised.get("magic-defence-skill-point-bonus"));
        deserialised.setSkillPointBonus(SkillType.MAGIC_HEALING, (int) serialised.get("magic-healing-skill-point-bonus"));
        deserialised.setSkillPointBonus(SkillType.MAGIC_NATURE, (int) serialised.get("magic-nature-skill-point-bonus"));
        deserialised.setSkillPointBonus(SkillType.MAGIC_ILLUSION, (int) serialised.get("magic-illusion-skill-point-bonus"));
        deserialised.setSkillPointBonus(SkillType.MAGIC_SUMMONING, (int) serialised.get("magic-summoning-skill-point-bonus"));
        deserialised.setSkillPointBonus(SkillType.MAGIC_SWORD, (int) serialised.get("magic-sword-skill-point-bonus"));
        deserialised.setSkillPointBonus(SkillType.SPEED_NIMBLE, (int) serialised.get("speed-nimble-skill-point-bonus"));
        deserialised.setSkillPointBonus(SkillType.SUPPORT_PERFORM, (int) serialised.get("support-perform-skill-point-bonus"));
        deserialised.manaBonus = (int) serialised.get("mana-bonus");
        deserialised.maxLevel = (int) serialised.get("max-level");
        deserialised.prerequisites = (Map<Class, Integer>) serialised.get("prerequisites");
        return deserialised;
    }

}
