package net.wayward_realms.waywardcharacters;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Gender;
import net.wayward_realms.waywardlib.character.Race;
import net.wayward_realms.waywardlib.classes.ClassesPlugin;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.util.serialisation.SerialisableLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharacterImpl implements Character, ConfigurationSerializable {

    private static int nextId = 0;

    public static int getNextId() {
        nextId++;
        return nextId;
    }

    private int id;
    //The player the character is associated with
    private String ign;
    //The character and their IC details. Character card stuff.
    private String name;
    private Gender gender;
    private int age;
    private Race race;
    private String description;
    private boolean dead;
    //World data to save/restore upon switching character
    private Location location;
    private ItemStack[] inventoryContents;
    //Stats and other character data
    private double health;
    private int foodLevel;
    private int mana;
    private int thirst;
	private int maxThirst = 20;
	private int minThirst = 0;
    //Field hiding
    private boolean nameHidden;
    private boolean genderHidden;
    private boolean ageHidden;
    private boolean raceHidden;
    private boolean descriptionHidden;
    private boolean classHidden;

    private CharacterImpl() {}

    public CharacterImpl(OfflinePlayer player) {
        CharacterPlugin characterPlugin = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class).getProvider();
        ClassesPlugin classesPlugin = Bukkit.getServer().getServicesManager().getRegistration(ClassesPlugin.class).getProvider();
        this.id = CharacterImpl.getNextId();
        this.ign = player.getName();
        this.name = "Unknown";
        this.gender = characterPlugin.getGender("UNKNOWN");
        this.age = 0;
        this.race = characterPlugin.getRace("UNKNOWN");
        this.description = player.getName() + "'s character";
        this.dead = false;
        this.location = Bukkit.getServer().getWorlds().get(0).getSpawnLocation();
        this.inventoryContents = new ItemStack[36];
        this.health = classesPlugin.getClass(this).getHpBonus() * classesPlugin.getLevel(this);
        this.foodLevel = 20;
        this.mana = getMaxMana();
        this.thirst = 20;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return nameHidden ? "???" : name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        if (getPlayer().isOnline()) {
            getPlayer().getPlayer().setDisplayName(getName());
        }
    }

    public boolean isNameHidden() {
        return nameHidden;
    }

    public void setNameHidden(boolean nameHidden) {
        this.nameHidden = nameHidden;
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
        return Bukkit.getOfflinePlayer(ign);
    }

    @Override
    public void setPlayer(OfflinePlayer player) {
        this.ign = player.getName();
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
        this.description += info + " ";
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
        this.health = health;
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
        return thirst;
    }

    @Override
    public void setThirst(int thirstLevel) {
        this.thirst = thirstLevel;
        if (getThirst() > maxThirst) {
            this.thirst = maxThirst;
        }
        if (getThirst() < minThirst) {
        	this.thirst = minThirst;
        }
        
    }

    @Override
    public double getMaxHealth() {
        RegisteredServiceProvider<ClassesPlugin> classesPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(ClassesPlugin.class);
        if (classesPluginProvider != null) {
            ClassesPlugin classesPlugin = classesPluginProvider.getProvider();
            return (int) Math.round((((250D + (2D * classesPlugin.getClass(this).getHpBonus() * 10D)) * (double) classesPlugin.getLevel(this)) / 100D) + 10D);
        }
        return 0;
    }

    @Override
    public void setMaxHealth(double maxHealth) {
        throw new UnsupportedOperationException("You can't set a character's max health!");
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
            return (int) Math.round((((250D + (2D * classesPlugin.getClass(this).getManaBonus() * 10D)) * (double) classesPlugin.getLevel(this)) / 100D) + 10D);
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
    }

    @Override
    public int getStatValue(Stat stat) {
        RegisteredServiceProvider<ClassesPlugin> classesPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(ClassesPlugin.class);
        if (classesPluginProvider != null) {
            ClassesPlugin classesPlugin = classesPluginProvider.getProvider();
            return (int) Math.round((((150D + (2D * (double) classesPlugin.getClass(this).getStatBonus(stat) * 10D)) * (double) classesPlugin.getLevel(this)) / 100D) + 5D);
        }
        return 0;
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

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("id", id);
        serialised.put("ign", ign);
        serialised.put("name", name);
        serialised.put("gender", gender);
        serialised.put("age", age);
        serialised.put("race", race);
        serialised.put("description", description);
        serialised.put("dead", dead);
        serialised.put("location", new SerialisableLocation(location));
        serialised.put("inventory-contents", inventoryContents);
        serialised.put("health", health);
        serialised.put("food-level", foodLevel);
        serialised.put("mana", mana);
        serialised.put("thirst", thirst);
        serialised.put("name-hidden", nameHidden);
        serialised.put("gender-hidden", genderHidden);
        serialised.put("age-hidden", ageHidden);
        serialised.put("race-hidden", raceHidden);
        serialised.put("description-hidden", descriptionHidden);
        serialised.put("class-hidden", classHidden);
        return serialised;
    }

    public static CharacterImpl deserialize(Map<String, Object> serialised) {
        CharacterImpl character = new CharacterImpl();
        character.id = (int) serialised.get("id");
        if (character.id > nextId) {
            nextId = character.id;
        }
        character.ign = (String) serialised.get("ign");
        character.name = (String) serialised.get("name");
        character.gender = (Gender) serialised.get("gender");
        character.age = (int) serialised.get("age");
        character.race = (Race) serialised.get("race");
        character.description = (String) serialised.get("description");
        character.dead = (boolean) serialised.get("dead");
        character.location = ((SerialisableLocation) serialised.get("location")).toLocation();
        if (serialised.get("inventory-contents") instanceof List<?>) {
            character.inventoryContents = ((List<ItemStack>) serialised.get("inventory-contents")).toArray(new ItemStack[36]);
        } else {
            character.inventoryContents = new ItemStack[36];
        }
        character.health = (double) serialised.get("health");
        character.foodLevel = (int) serialised.get("food-level");
        character.mana = (int) serialised.get("mana");
        character.thirst = (int) serialised.get("thirst");
        character.nameHidden = serialised.get("name-hidden") != null && (boolean) serialised.get("name-hidden");
        character.genderHidden = serialised.get("gender-hidden") != null && (boolean) serialised.get("gender-hidden");
        character.ageHidden = serialised.get("age-hidden") != null && (boolean) serialised.get("age-hidden");
        character.raceHidden = serialised.get("race-hidden") != null && (boolean) serialised.get("race-hidden");
        character.descriptionHidden = serialised.get("description-hidden") != null && (boolean) serialised.get("description-hidden");
        character.classHidden = serialised.get("class-hidden") != null && (boolean) serialised.get("class-hidden");
        return character;
    }

}
