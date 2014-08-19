package net.wayward_realms.waywardevents;

import net.wayward_realms.waywardlib.character.Gender;
import net.wayward_realms.waywardlib.character.Race;
import net.wayward_realms.waywardlib.skills.Stat;
import net.wayward_realms.waywardlib.events.EventCharacterTemplate;
import net.wayward_realms.waywardlib.events.EventsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class EventCharacterTemplateImpl implements EventCharacterTemplate {

    private File file;

    public EventCharacterTemplateImpl(EventCharacterImpl eventCharacter) {
        RegisteredServiceProvider<EventsPlugin> eventsPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(EventsPlugin.class);
        if (eventsPluginProvider != null) {
            EventsPlugin eventsPlugin = eventsPluginProvider.getProvider();
            File templateDirectory = new File(eventsPlugin.getDataFolder(), "event-character-templates");
            this.file = new File(templateDirectory, eventCharacter.getName() + ".yml");
            setCreator(eventCharacter.getPlayer());
            setName(eventCharacter.getName());
            setAge(eventCharacter.getAge());
            setGender(eventCharacter.getGender());
            setRace(eventCharacter.getRace());
            setDescription(eventCharacter.getDescription());
            setMaxHealth(eventCharacter.getMaxHealth());
            setMaxMana(eventCharacter.getMaxMana());
            setHelmet(eventCharacter.getHelmet());
            setChestplate(eventCharacter.getChestplate());
            setLeggings(eventCharacter.getLeggings());
            setBoots(eventCharacter.getBoots());
            setInventoryContents(eventCharacter.getInventoryContents());
            for (Stat stat : Stat.values()) {
                setStatValue(stat, eventCharacter.getStatValue(stat));
            }
        }
    }

    public EventCharacterTemplateImpl(EventCharacterTemplate template) {
        RegisteredServiceProvider<EventsPlugin> eventsPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(EventsPlugin.class);
        if (eventsPluginProvider != null) {
            EventsPlugin eventsPlugin = eventsPluginProvider.getProvider();
            File templateDirectory = new File(eventsPlugin.getDataFolder(), "event-character-templates");
            this.file = new File(templateDirectory, template.getName() + ".yml");
            setCreator(template.getCreator());
            setName(template.getName());
            setAge(template.getAge());
            setGender(template.getGender());
            setRace(template.getRace());
            setDescription(template.getDescription());
            setMaxHealth(template.getMaxHealth());
            setMaxMana(template.getMaxMana());
            setHelmet(template.getHelmet());
            setChestplate(template.getChestplate());
            setLeggings(template.getLeggings());
            setBoots(template.getBoots());
            setInventoryContents(template.getInventoryContents());
            for (Stat stat : Stat.values()) {
                setStatValue(stat, template.getStatValue(stat));
            }
        }
    }

    public EventCharacterTemplateImpl(File file) {
        this.file = file;
    }

    private void setFieldValue(String field, Object value) {
        YamlConfiguration save = YamlConfiguration.loadConfiguration(file);
        save.set("template." + field, value);
        try {
            save.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private Object getFieldValue(String field) {
        YamlConfiguration save = YamlConfiguration.loadConfiguration(file);
        return save.get("template." + field);
    }

    private int getFieldIntValue(String field) {
        YamlConfiguration save = YamlConfiguration.loadConfiguration(file);
        return save.getInt("template." + field);
    }

    private double getFieldDoubleValue(String field) {
        YamlConfiguration save = YamlConfiguration.loadConfiguration(file);
        return save.getDouble("template." + field);
    }

    private boolean getFieldBooleanValue(String field) {
        YamlConfiguration save = YamlConfiguration.loadConfiguration(file);
        return save.getBoolean("template." + field);
    }

    private String getFieldStringValue(String field) {
        YamlConfiguration save = YamlConfiguration.loadConfiguration(file);
        return save.getString("template." + field);
    }

    private ItemStack getFieldItemStackValue(String field) {
        YamlConfiguration save = YamlConfiguration.loadConfiguration(file);
        return save.getItemStack("template." + field);
    }

    @Override
    public OfflinePlayer getCreator() {
        return Bukkit.getOfflinePlayer(UUID.fromString(getFieldStringValue("creator")));
    }

    @Override
    public void setCreator(OfflinePlayer creator) {
        setFieldValue("creator", creator.getUniqueId().toString());
    }

    @Override
    public String getName() {
        return getFieldStringValue("name");
    }

    @Override
    public void setName(String name) {
        setFieldValue("name", name);
    }

    @Override
    public int getAge() {
        return getFieldIntValue("age");
    }

    @Override
    public void setAge(int age) {
        setFieldValue("age", age);
    }

    @Override
    public Gender getGender() {
        return (Gender) getFieldValue("gender");
    }

    @Override
    public void setGender(Gender gender) {
        setFieldValue("gender", gender);
    }

    @Override
    public Race getRace() {
        return (Race) getFieldValue("race");
    }

    @Override
    public void setRace(Race race) {
        setFieldValue("race", race);
    }

    @Override
    public String getDescription() {
        return getFieldStringValue("description");
    }

    @Override
    public void setDescription(String description) {
        setFieldValue("description", description);
    }

    @Override
    public void addDescription(String info) {
        setDescription(getDescription() + " " + info);
    }

    @Override
    public double getMaxHealth() {
        return getFieldDoubleValue("max-health");
    }

    @Override
    public void setMaxHealth(double maxHealth) {
        setFieldValue("max-health", maxHealth);
    }

    @Override
    public int getMaxMana() {
        return getFieldIntValue("max-mana");
    }

    @Override
    public void setMaxMana(int maxMana) {
        setFieldValue("max-mana", maxMana);
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
    public void setInventoryContents(ItemStack[] contents) {
        setFieldValue("inventory-contents", contents);
    }

    @Override
    public int getStatValue(Stat stat) {
        return getFieldIntValue("stats." + stat.toString().toLowerCase());
    }

    @Override
    public void setStatValue(Stat stat, int value) {
        setFieldValue("stats." + stat.toString().toLowerCase(), value);
    }

}
