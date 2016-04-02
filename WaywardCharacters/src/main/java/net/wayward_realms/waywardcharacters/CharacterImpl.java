package net.wayward_realms.waywardcharacters;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.Gender;
import net.wayward_realms.waywardlib.character.Race;
import net.wayward_realms.waywardlib.character.TemporaryStatModification;
import net.wayward_realms.waywardlib.classes.ClassesPlugin;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardlib.util.serialisation.SerialisableLocation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CharacterImpl implements Character {

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

    private WaywardCharacters plugin;
    private File file;
    private static final int MAX_THIRST = 20;
    private static final int MIN_THIRST = 0;

    private CharacterImpl() {}

    public CharacterImpl(WaywardCharacters plugin, OfflinePlayer player) {
        int id = CharacterImpl.nextAvailableId();
        this.plugin = plugin;
        this.file = new File(new File(plugin.getDataFolder(), "characters-new"), id + ".yml");
        setId(id);
        setPlayer(player);
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
        setNamePlate("");
    }

    public CharacterImpl(WaywardCharacters plugin, File file) {
        this.plugin = plugin;
        this.file = file;
    }

    private void setFieldValue(String field, Object value) {
        YamlConfiguration save = YamlConfiguration.loadConfiguration(file);
        save.set("character." + field, value);
        try {
            save.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private Object getFieldValue(String field) {
        YamlConfiguration save = YamlConfiguration.loadConfiguration(file);
        return save.get("character." + field);
    }

    private int getFieldIntValue(String field) {
        YamlConfiguration save = YamlConfiguration.loadConfiguration(file);
        return save.getInt("character." + field);
    }

    private double getFieldDoubleValue(String field) {
        YamlConfiguration save = YamlConfiguration.loadConfiguration(file);
        return save.getDouble("character." + field);
    }

    private boolean getFieldBooleanValue(String field) {
        YamlConfiguration save = YamlConfiguration.loadConfiguration(file);
        return save.getBoolean("character." + field);
    }

    private String getFieldStringValue(String field) {
        YamlConfiguration save = YamlConfiguration.loadConfiguration(file);
        return save.getString("character." + field);
    }

    private ItemStack getFieldItemStackValue(String field) {
        YamlConfiguration save = YamlConfiguration.loadConfiguration(file);
        return save.getItemStack("character." + field);
    }

    private List<?> getFieldListValue(String field) {
        YamlConfiguration save = YamlConfiguration.loadConfiguration(file);
        return save.getList("character." + field);
    }

    @Override
    public int getId() {
        return getFieldIntValue("id");
    }

    private void setId(int id) {
        setFieldValue("id", id);
    }

    @Override
    public String getName() {
        return getFieldStringValue("name");
    }

    @Override
    public void setName(String name) {
        setFieldValue("name", name);
        OfflinePlayer player = getPlayer();
        if (player.isOnline()) {
            player.getPlayer().setDisplayName(isNameHidden() ? "???" : name);
        }
    }

    public boolean isNameHidden() {
        return getFieldBooleanValue("name-hidden");
    }

    public void setNameHidden(boolean nameHidden) {
        setFieldValue("name-hidden", nameHidden);
        OfflinePlayer player = getPlayer();
        if (player.isOnline()) {
            player.getPlayer().setDisplayName(nameHidden ? ChatColor.MAGIC + getName() + ChatColor.RESET : getName());
        }
    }

    @Override
    public int getAge() {
        return getFieldIntValue("age");
    }

    @Override
    public void setAge(int age) {
        setFieldValue("age", age);
    }

    public boolean isAgeHidden() {
        return getFieldBooleanValue("age-hidden");
    }

    public void setAgeHidden(boolean ageHidden) {
        setFieldValue("age-hidden", ageHidden);
    }

    @Override
    public OfflinePlayer getPlayer() {
        if (getFieldValue("uuid") != null) {
            return Bukkit.getOfflinePlayer(UUID.fromString(getFieldStringValue("uuid")));
        } else {
            setFieldValue("uuid", Bukkit.getOfflinePlayer(getFieldStringValue("ign")).getUniqueId().toString());
            setFieldValue("ign", null);
            return getPlayer();
        }
    }

    @Override
    public void setPlayer(OfflinePlayer player) {
        setFieldValue("uuid", player.getUniqueId().toString());
    }

    @Override
    public Gender getGender() {
        return (Gender) getFieldValue("gender");
    }

    @Override
    public void setGender(Gender gender) {
        setFieldValue("gender", gender);
    }

    public boolean isGenderHidden() {
        return getFieldBooleanValue("gender-hidden");
    }

    public void setGenderHidden(boolean genderHidden) {
        setFieldValue("gender-hidden", genderHidden);
    }

    @Override
    public Race getRace() {
        return (Race) getFieldValue("race");
    }

    @Override
    public void setRace(Race race) {
        setFieldValue("race", race);
    }

    public boolean isRaceHidden() {
        return getFieldBooleanValue("race-hidden");
    }

    public void setRaceHidden(boolean raceHidden) {
        setFieldValue("race-hidden", raceHidden);
    }

    @Override
    public String getDescription() {
        return getFieldStringValue("description");
    }

    @Override
    public void setDescription(String description) {
        setFieldValue("description", description + " ");
    }

    @Override
    public void addDescription(String info) {
        setDescription(getDescription() + info + " ");
    }

    public boolean isDescriptionHidden() {
        return getFieldBooleanValue("description-hidden");
    }

    public void setDescriptionHidden(boolean descriptionHidden) {
        setFieldValue("description-hidden", descriptionHidden);
    }

    @Override
    public Location getLocation() {
        return ((SerialisableLocation) getFieldValue("location")).toLocation();
    }

    @Override
    public void setLocation(Location location) {
        setFieldValue("location", new SerialisableLocation(location));
    }

    @Override
    public ItemStack getHelmet() {
        return getFieldItemStackValue("helmet");
    }

    @Override
    public void setHelmet(ItemStack helmet) {
        setFieldValue("helmet", helmet);
    }

    @Override
    public ItemStack getChestplate() {
        return getFieldItemStackValue("chestplate");
    }

    @Override
    public void setChestplate(ItemStack chestplate) {
        setFieldValue("chestplate", chestplate);
    }

    @Override
    public ItemStack getLeggings() {
        return getFieldItemStackValue("leggings");
    }

    @Override
    public void setLeggings(ItemStack leggings) {
        setFieldValue("leggings", leggings);
    }

    @Override
    public ItemStack getBoots() {
        return getFieldItemStackValue("boots");
    }

    @Override
    public void setBoots(ItemStack boots) {
        setFieldValue("boots", boots);
    }

    @Override
    public ItemStack[] getInventoryContents() {
        if (getFieldValue("inventory-contents") instanceof List<?>) {
            return ((List<ItemStack>) getFieldValue("inventory-contents")).toArray(new ItemStack[36]);
        } else {
            return new ItemStack[36];
        }
    }

    @Override
    public void setInventoryContents(ItemStack[] inventoryContents) {
        setFieldValue("inventory-contents", inventoryContents);
    }

    @Override
    public double getHealth() {
        return getFieldDoubleValue("health");
    }

    @Override
    public void setHealth(double health) {
        setFieldValue("health", Math.max(Math.min(health, getMaxHealth()), 0));
    }

    @Override
    public int getFoodLevel() {
        return getFieldIntValue("food-level");
    }

    @Override
    public void setFoodLevel(int foodLevel) {
        setFieldValue("food-level", foodLevel);
    }

    @Override
    public int getThirst() {
        return getFieldIntValue("thirst");
    }

    @Override
    public void setThirst(int thirstLevel) {
        setFieldValue("thirst", thirstLevel);
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
        return getFieldIntValue("mana");
    }

    @Override
    public void setMana(int mana) {
        setFieldValue("mana", mana);
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
        return getFieldBooleanValue("dead");
    }

    @Override
    public void setDead(boolean dead) {
        setFieldValue("dead", dead);
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
        return getFieldValue("temporary-stat-modifications") != null ? (List<TemporaryStatModification>) getFieldListValue("temporary-stat-modifications") : new ArrayList<TemporaryStatModification>();
    }

    @Override
    public void addTemporaryStatModification(TemporaryStatModification modification) {
        List<TemporaryStatModification> statModifications = getFieldValue("temporary-stat-modifications") != null ? (List<TemporaryStatModification>) getFieldListValue("temporary-stat-modifications") : new ArrayList<TemporaryStatModification>();
        statModifications.add(modification);
        setFieldValue("temporary-stat-modifications", statModifications);
    }

    @Override
    public void removeTemporaryStatModification(TemporaryStatModification modification) {
        List<TemporaryStatModification> statModifications = getFieldValue("temporary-stat-modifications") != null ? (List<TemporaryStatModification>) getFieldListValue("temporary-stat-modifications") : new ArrayList<TemporaryStatModification>();
        Iterator<TemporaryStatModification> iterator = statModifications.iterator();
        while (iterator.hasNext()) {
            TemporaryStatModification statMod = iterator.next();
            if (statMod.equals(modification)) {
                iterator.remove();
                break;
            }
        }
        setFieldValue("temporary-stat-modifications", statModifications);
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
        return getFieldBooleanValue("class-hidden");
    }

    public void setClassHidden(boolean classHidden) {
        setFieldValue("class-hidden", classHidden);
    }

    @Override
    public String getNamePlate() {
        return getFieldStringValue("nameplate");
    }

    @Override
    public void setNamePlate(String namePlate) {
        setFieldValue("nameplate", namePlate);
    }

    public int getStatPoints(Stat stat) {
        return getFieldValue("stat-points." + stat.toString().toLowerCase()) != null ? getFieldIntValue("stat-points." + stat.toString().toLowerCase()) : 0;
    }

    public void setStatPoints(Stat stat, int amount) {
        setFieldValue("stat-points." + stat.toString().toLowerCase(), amount);
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
        setFieldValue("stat-points", null);
    }

}
