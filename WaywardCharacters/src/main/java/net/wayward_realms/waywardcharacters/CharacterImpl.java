package net.wayward_realms.waywardcharacters;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.Gender;
import net.wayward_realms.waywardlib.character.Race;
import net.wayward_realms.waywardlib.character.TemporaryStatModification;
import net.wayward_realms.waywardlib.classes.ClassesPlugin;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardlib.util.database.TableRowImpl;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.*;

public class CharacterImpl extends TableRowImpl implements Character {

    private static int nextId = 0;

    public static int nextAvailableId() {
        nextId++;
        return nextId;
    }

    public static int getNextId() {
        return nextId;
    }

    public static void setNextId(int id) {
        CharacterImpl.nextId = id;
    }

    private static final int MAX_THIRST = 20;
    private static final int MIN_THIRST = 0;

    private WaywardCharacters plugin;
    private String name;
    private boolean nameHidden;
    private int age;
    private boolean ageHidden;
    private UUID playerUUID;
    private Gender gender;
    private boolean genderHidden;
    private Race race;
    private boolean raceHidden;
    private String description;
    private boolean descriptionHidden;
    private Location location;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private ItemStack[] inventoryContents;
    private double health;
    private int foodLevel;
    private int thirstLevel;
    private int mana;
    private boolean dead;
    private Collection<TemporaryStatModification> temporaryStatModifications;
    private boolean classHidden;
    private Map<Stat, Integer> statPoints;

    public CharacterImpl(WaywardCharacters plugin, OfflinePlayer player) {
        super(CharacterImpl.nextAvailableId());
        this.plugin = plugin;
        setName("Unknown");
        setGender(plugin.getGender("UNKNOWN"));
        setAge(0);
        setRace(plugin.getRace("UNKNOWN"));
        setDescription(player.getName() + "'s character");
        setDead(false);
        setLocation(Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
        setInventoryContents(new ItemStack[36]);
        setHealth(getMaxHealth());
        setFoodLevel(20);
        setMana(getMaxMana());
        setThirst(20);
    }

    public CharacterImpl(int id, WaywardCharacters plugin, int id1, String name, boolean nameHidden, int age, boolean ageHidden, UUID playerUUID, Gender gender, boolean genderHidden, Race race, boolean raceHidden, String description, boolean descriptionHidden, Location location, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, ItemStack[] inventoryContents, double health, int foodLevel, int thirstLevel, int mana, boolean dead, Collection<TemporaryStatModification> temporaryStatModifications, boolean classHidden, Map<Stat, Integer> statPoints) {
        super(id);
        this.plugin = plugin;
        this.name = name;
        this.nameHidden = nameHidden;
        this.age = age;
        this.ageHidden = ageHidden;
        this.playerUUID = playerUUID;
        this.gender = gender;
        this.genderHidden = genderHidden;
        this.race = race;
        this.raceHidden = raceHidden;
        this.description = description;
        this.descriptionHidden = descriptionHidden;
        this.location = location;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.inventoryContents = inventoryContents;
        this.health = health;
        this.foodLevel = foodLevel;
        this.thirstLevel = thirstLevel;
        this.mana = mana;
        this.dead = dead;
        this.temporaryStatModifications = temporaryStatModifications;
        this.classHidden = classHidden;
        this.statPoints = statPoints;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        OfflinePlayer player = getPlayer();
        if (player.isOnline()) {
            player.getPlayer().setDisplayName(isNameHidden() ? "???" : name);
        }
    }

    public boolean isNameHidden() {
        return nameHidden;
    }

    public void setNameHidden(boolean nameHidden) {
        this.nameHidden = nameHidden;
        OfflinePlayer player = getPlayer();
        if (player.isOnline()) {
            player.getPlayer().setDisplayName(nameHidden ? ChatColor.MAGIC + getName() + ChatColor.RESET : getName());
        }
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public void setAge(int age) {
        this.age = age;
    }

    public boolean isAgeHidden() {
        return ageHidden;
    }

    public void setAgeHidden(boolean ageHidden) {
        this.ageHidden = ageHidden;
    }

    @Override
    public OfflinePlayer getPlayer() {
        return plugin.getServer().getOfflinePlayer(playerUUID);
    }

    @Override
    public void setPlayer(OfflinePlayer player) {
        this.playerUUID = player.getUniqueId();
    }

    @Override
    public Gender getGender() {
        return gender;
    }

    @Override
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public boolean isGenderHidden() {
        return genderHidden;
    }

    public void setGenderHidden(boolean genderHidden) {
        this.genderHidden = genderHidden;
    }

    @Override
    public Race getRace() {
        return race;
    }

    @Override
    public void setRace(Race race) {
        this.race = race;
    }

    public boolean isRaceHidden() {
        return raceHidden;
    }

    public void setRaceHidden(boolean raceHidden) {
        this.raceHidden = raceHidden;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description + " ";
    }

    @Override
    public void addDescription(String info) {
        setDescription(getDescription() + info + " ");
    }

    public boolean isDescriptionHidden() {
        return descriptionHidden;
    }

    public void setDescriptionHidden(boolean descriptionHidden) {
        this.descriptionHidden = descriptionHidden;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public ItemStack getHelmet() {
        return helmet;
    }

    @Override
    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet;
    }

    @Override
    public ItemStack getChestplate() {
        return chestplate;
    }

    @Override
    public void setChestplate(ItemStack chestplate) {
        this.chestplate = chestplate;
    }

    @Override
    public ItemStack getLeggings() {
        return leggings;
    }

    @Override
    public void setLeggings(ItemStack leggings) {
        this.leggings = leggings;
    }

    @Override
    public ItemStack getBoots() {
        return boots;
    }

    @Override
    public void setBoots(ItemStack boots) {
        this.boots = boots;
    }

    @Override
    public ItemStack[] getInventoryContents() {
        return inventoryContents;
    }

    @Override
    public void setInventoryContents(ItemStack[] inventoryContents) {
        this.inventoryContents = inventoryContents;
    }

    @Override
    public double getHealth() {
        return health;
    }

    @Override
    public void setHealth(double health) {
        this.health = Math.max(Math.min(health, getMaxHealth()), 0);
    }

    @Override
    public int getFoodLevel() {
        return foodLevel;
    }

    @Override
    public void setFoodLevel(int foodLevel) {
        this.foodLevel = foodLevel;
    }

    @Override
    public int getThirst() {
        return thirstLevel;
    }

    @Override
    public void setThirst(int thirstLevel) {
        this.thirstLevel = thirstLevel;
        if (getThirst() > MAX_THIRST) {
            setThirst(MAX_THIRST);
        }
        if (getThirst() < MIN_THIRST) {
            setThirst(MIN_THIRST);
        }
        
    }

    @Override
    public double getMaxHealth() {
        RegisteredServiceProvider<ClassesPlugin> classesPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(ClassesPlugin.class);
        if (classesPluginProvider != null) {
            ClassesPlugin classesPlugin = classesPluginProvider.getProvider();
            return (int) Math.round((((250D + (20D * classesPlugin.getClass(this).getHpBonus())) * (double) classesPlugin.getLevel(this)) / 100D) + 10D);
        }
        return 0;
    }

    @Override
    public int getMana() {
        return mana;
    }

    @Override
    public void setMana(int mana) {
        this.mana = mana;
    }

    @Override
    public int getMaxMana() {
        RegisteredServiceProvider<ClassesPlugin> classesPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(ClassesPlugin.class);
        if (classesPluginProvider != null) {
            ClassesPlugin classesPlugin = classesPluginProvider.getProvider();
            return (int) Math.round((((250D + (20D * classesPlugin.getClass(this).getManaBonus())) * (double) classesPlugin.getLevel(this)) / 100D) + 20D);
        }
        return 0;
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    @Override
    public void setDead(boolean dead) {
        this.dead = dead;
        if (dead) setHealth(0D); else setHealth(1D);
    }

    @Override
    public int getStatValue(Stat stat) {
        RegisteredServiceProvider<ClassesPlugin> classesPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(ClassesPlugin.class);
        if (classesPluginProvider != null) {
            ClassesPlugin classesPlugin = classesPluginProvider.getProvider();
            int value = (int) Math.round((((150D + (20D * (double) (classesPlugin.getClass(this).getStatBonus(stat) + getStatPoints(stat) + getRace().getStatBonus(stat)))) * (double) classesPlugin.getLevel(this)) / 100D) + 5D);
            for (TemporaryStatModification modification : getTemporaryStatModifications()) {
                if (modification != null) value = modification.apply(stat, value);
            }
            return value;
        }
        return 0;
    }

    @Override
    public Collection<TemporaryStatModification> getTemporaryStatModifications() {
        if (temporaryStatModifications == null) temporaryStatModifications = new ArrayList<>();
        return temporaryStatModifications;
    }

    @Override
    public void addTemporaryStatModification(TemporaryStatModification modification) {
        getTemporaryStatModifications().add(modification);
    }

    @Override
    public void removeTemporaryStatModification(TemporaryStatModification modification) {
        getTemporaryStatModifications().remove(modification);
    }

    @Override
    public int getSkillPoints(SkillType type) {
        RegisteredServiceProvider<ClassesPlugin> classesPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(ClassesPlugin.class);
        if (classesPluginProvider != null) {
            ClassesPlugin classesPlugin = classesPluginProvider.getProvider();
            return classesPlugin.getClass(this).getSkillPointBonus(type) * classesPlugin.getLevel(this);
        }
        return 0;
    }

    public boolean isClassHidden() {
        return classHidden;
    }

    public void setClassHidden(boolean classHidden) {
        this.classHidden = classHidden;
    }

    public int getStatPoints(Stat stat) {
        return statPoints.containsKey(stat) ? statPoints.get(stat) : 0;
    }

    public void setStatPoints(Stat stat, int amount) {
        statPoints.put(stat, amount);
    }

    public int getAssignedStatPoints() {
        int assigned = 0;
        for (Stat stat : Stat.values()) {
            assigned += getStatPoints(stat);
        }
        return assigned;
    }

    public int getUnassignedStatPoints() {
        return getTotalStatPoints() - getAssignedStatPoints();
    }

    public int getTotalStatPoints() {
        return 10;
    }

    public void assignStatPoint(Stat stat) {
        setStatPoints(stat, getStatPoints(stat) + 1);
    }

    public void resetStatPoints() {
        statPoints = new EnumMap<>(Stat.class);
    }

}
