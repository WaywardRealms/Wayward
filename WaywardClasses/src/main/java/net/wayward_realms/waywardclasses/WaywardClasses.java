package net.wayward_realms.waywardclasses;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.classes.Class;
import net.wayward_realms.waywardlib.classes.ClassChangeEvent;
import net.wayward_realms.waywardlib.classes.ClassLevelChangeEvent;
import net.wayward_realms.waywardlib.classes.ClassesPlugin;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class WaywardClasses extends JavaPlugin implements ClassesPlugin {

    private Map<String, Class> classes = new HashMap<>();

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(ClassImpl.class);
        getCommand("addexp").setExecutor(new AddExpCommand(this));
        getCommand("class").setExecutor(new ClassCommand(this));
        getCommand("setclass").setExecutor(new SetClassCommand(this));
        getCommand("setlevel").setExecutor(new SetLevelCommand(this));
        getCommand("getclass").setExecutor(new GetClassCommand(this));
        getCommand("getlevel").setExecutor(new GetLevelCommand(this));
        getCommand("listclasses").setExecutor(new ListClassesCommand(this));
        registerListeners(new EntityDamageListener(this), new PlayerDeathListener(), new PlayerExpChangeListener(), new PlayerJoinListener(this));
    }

    @Override
    public void onDisable() {
        saveState();
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    @Override
    public String getPrefix() {
        //return "" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "|" + ChatColor.RESET + ChatColor.BLUE + "WaywardClasses" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "| " + ChatColor.RESET;
        return "";
    }

    @Override
    public void saveState() {

    }

    @Override
    public void loadState() {
        if (getConfig().getConfigurationSection("classes") != null) {
            for (String section : getConfig().getConfigurationSection("classes").getKeys(false)) {
                classes.put(section.toUpperCase(), (ClassImpl) getConfig().get("classes." + section));
            }
        }
        if (classes.isEmpty()) {
            ClassImpl wanderer = new ClassImpl("Wanderer");
            classes.put("WANDERER", wanderer);
            ClassImpl fighter = new ClassImpl("Fighter", 2.0, 5, 3, 0, 5, 3, 2, 3, 0, 30);
            classes.put("FIGHTER", fighter);
            ClassImpl mage = new ClassImpl("Mage", 1.0, 2, 2, 8, 2, 2, 8, 3, 10, 30);
            classes.put("MAGE", mage);
            ClassImpl ranger = new ClassImpl("Ranger", 3.0, 3, 5, 1, 2, 5, 0, 3, 0, 30);
            classes.put("RANGER", ranger);
            Map<Class, Integer> spellFencerPrereqs = new HashMap<>();
            spellFencerPrereqs.put(fighter, 30);
            spellFencerPrereqs.put(mage, 30);
            ClassImpl spellFencer = new ClassImpl("SpellFencer", 2.0, 5, 3, 5, 5, 5, 5, 0, 10, 30, spellFencerPrereqs);
            classes.put("SPELLFENCER", spellFencer);
            saveState();
            getConfig().set("default-class", wanderer);
            saveConfig();
        }
    }

    @Override
    public Class getClass(OfflinePlayer player) {
        CharacterPlugin characterPlugin = getServer().getServicesManager().getRegistration(CharacterPlugin.class).getProvider();
        return getClass(characterPlugin.getActiveCharacter(player));
    }

    @Override
    public Class getClass(Character character) {
        File characterDirectory = new File(getDataFolder(), "characters");
        YamlConfiguration characterConfig = YamlConfiguration.loadConfiguration(new File(characterDirectory, character.getId() + ".yml"));
        if (characterConfig.get("class") == null) setClass(character, (Class) getConfig().get("default-class"));
        return getClass(characterConfig.getString("class"));
    }

    @Override
    public Class getClass(String name) {
        return classes.get(name.toUpperCase());
    }

    @Override
    public int getExperienceTowardsNextLevel(OfflinePlayer player) {
        return getExperienceTowardsNextLevel(player, getClass(player));
    }

    @Override
    public int getExperienceTowardsNextLevel(OfflinePlayer player, Class clazz) {
        CharacterPlugin characterPlugin = getServer().getServicesManager().getRegistration(CharacterPlugin.class).getProvider();
        return getExperienceTowardsNextLevel(characterPlugin.getActiveCharacter(player), clazz);
    }

    @Override
    public int getExperienceTowardsNextLevel(Character character) {
        return getExperienceTowardsNextLevel(character, getClass(character));
    }

    @Override
    public int getExperienceTowardsNextLevel(Character character, Class clazz) {
        return getTotalExperience(character, clazz) - getTotalExperienceForLevel(getLevel(character));
    }

    @Override
    public int getLevel(OfflinePlayer player) {
        return getLevel(player, getClass(player));
    }

    @Override
    public int getLevel(Character character) {
        return getLevel(character, getClass(character));
    }

    @Override
    public int getLevel(OfflinePlayer player, Class clazz) {
        CharacterPlugin characterPlugin = getServer().getServicesManager().getRegistration(CharacterPlugin.class).getProvider();
        return getLevel(characterPlugin.getActiveCharacter(player), clazz);
    }

    @Override
    public int getLevel(Character character, Class clazz) {
        int level = 1;
        while (getTotalExperienceForLevel(level + 1) <= getTotalExperience(character, clazz)) {
            level += 1;
        }
        return level;
    }

    @Override
    public int getTotalExperience(OfflinePlayer player) {
        return getTotalExperience(player, getClass(player));
    }

    @Override
    public int getTotalExperience(OfflinePlayer player, Class clazz) {
        CharacterPlugin characterPlugin = getServer().getServicesManager().getRegistration(CharacterPlugin.class).getProvider();
        return getTotalExperience(characterPlugin.getActiveCharacter(player), clazz);
    }

    @Override
    public int getTotalExperience(Character character) {
        return getTotalExperience(character, getClass(character));
    }

    @Override
    public int getTotalExperience(Character character, Class clazz) {
        File characterDirectory = new File(getDataFolder(), character.getId() + ".yml");
        YamlConfiguration characterConfig = YamlConfiguration.loadConfiguration(new File(characterDirectory, character.getId() + ".yml"));
        if (characterConfig.getConfigurationSection("experience") == null || characterConfig.get("experience." + clazz.getName()) == null) {
            return 0;
        }
        return characterConfig.getInt("experience." + clazz.getName());
    }

    @Override
    public void giveExperience(OfflinePlayer player, int amount) {
        giveExperience(player, getClass(player), amount);
    }

    @Override
    public void giveExperience(OfflinePlayer player, Class clazz, int amount) {
        CharacterPlugin characterPlugin = getServer().getServicesManager().getRegistration(CharacterPlugin.class).getProvider();
        giveExperience(characterPlugin.getActiveCharacter(player), clazz, amount);
    }

    @Override
    public void giveExperience(Character character, int amount) {
        giveExperience(character, getClass(character), amount);
    }

    @Override
    public void giveExperience(Character character, Class clazz, int amount) {
        setTotalExperience(character, clazz, getTotalExperience(character, clazz) + amount);
        if (character.getPlayer() != null) {
            OfflinePlayer offlinePlayer = character.getPlayer();
            if (offlinePlayer.isOnline()) {
                Player player = offlinePlayer.getPlayer();
                if (getLevel(character) < clazz.getMaxLevel()) {
                    player.sendMessage(ChatColor.YELLOW + "+" + amount + " exp");
                    player.sendMessage(ChatColor.GRAY + "Total: " + ChatColor.WHITE + getExperienceTowardsNextLevel(character, clazz) + "/" + getExpToNextLevel(getLevel(character, clazz)));
                }
            }
        }
    }

    @Override
    public void setClass(OfflinePlayer player, Class clazz) {
        CharacterPlugin characterPlugin = getServer().getServicesManager().getRegistration(CharacterPlugin.class).getProvider();
        setClass(characterPlugin.getActiveCharacter(player), clazz);
    }

    @Override
    public void setClass(Character character, Class clazz) {
        ClassChangeEvent event = new ClassChangeEvent(character, getClass(character), clazz);
        getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        File characterDirectory = new File(getDataFolder(), "characters");
        File characterFile = new File(characterDirectory, event.getCharacter().getId() + ".yml");
        YamlConfiguration characterConfig = YamlConfiguration.loadConfiguration(characterFile);
        characterConfig.set("class", event.getClazz().getName());
        try {
            characterConfig.save(characterFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        event.getCharacter().setHealth(Math.min(event.getCharacter().getHealth(), event.getCharacter().getMaxHealth()));
        if (event.getCharacter().getPlayer().isOnline()) {
            updateExp(event.getCharacter().getPlayer().getPlayer());
            updateHealth(event.getCharacter().getPlayer().getPlayer());
        }
        if (character.getMana() > character.getMaxMana()) {
            character.setMana(character.getMaxMana());
        }
    }

    @Override
    public void setExperienceTowardsNextLevel(OfflinePlayer player, int amount) {
        setExperienceTowardsNextLevel(player, getClass(player), amount);
    }

    @Override
    public void setExperienceTowardsNextLevel(OfflinePlayer player, Class clazz, int amount) {
        CharacterPlugin characterPlugin = getServer().getServicesManager().getRegistration(CharacterPlugin.class).getProvider();
        setExperienceTowardsNextLevel(characterPlugin.getActiveCharacter(player), clazz, amount);
    }

    @Override
    public void setExperienceTowardsNextLevel(Character character, int amount) {
        setExperienceTowardsNextLevel(character, getClass(character), amount);
    }

    @Override
    public void setExperienceTowardsNextLevel(Character character, Class clazz, int amount) {
        setTotalExperience(character, clazz, getTotalExperienceForLevel(getLevel(character, clazz)) + amount);
    }

    @Override
    public void setLevel(OfflinePlayer player, int level) {
        setLevel(player, getClass(player), level);
    }

    @Override
    public void setLevel(OfflinePlayer player, Class clazz, int level) {
        CharacterPlugin characterPlugin = getServer().getServicesManager().getRegistration(CharacterPlugin.class).getProvider();
        setLevel(characterPlugin.getActiveCharacter(player), clazz, level);
    }

    @Override
    public void setLevel(Character character, int level) {
        setLevel(character, getClass(character), level);
    }

    @Override
    public void setLevel(Character character, Class clazz, int level) {
        setTotalExperience(character, clazz, getTotalExperienceForLevel(level));
    }

    @Override
    public void setTotalExperience(OfflinePlayer player, int amount) {
        setTotalExperience(player, getClass(player), amount);
    }

    @Override
    public void setTotalExperience(OfflinePlayer player, Class clazz, int amount) {
        CharacterPlugin characterPlugin = getServer().getServicesManager().getRegistration(CharacterPlugin.class).getProvider();
        setTotalExperience(characterPlugin.getActiveCharacter(player), clazz, amount);
    }

    @Override
    public void setTotalExperience(Character character, int amount) {
        setTotalExperience(character, getClass(character), amount);
    }

    @Override
    public void setTotalExperience(Character character, Class clazz, int amount) {
        int i = 0;
        while (amount >= getTotalExperienceForLevel(getLevel(character, clazz) + i + 1) && getLevel(character) + i < clazz.getMaxLevel()) {
            i += 1;
        }
        if (i >= 1) {
            ClassLevelChangeEvent event = new ClassLevelChangeEvent(character, clazz, getLevel(character), Math.min(getLevel(character) + i + 1, clazz.getMaxLevel()));
            getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }
            if (character.getPlayer().isOnline()) {
                Player player = character.getPlayer().getPlayer();
                player.sendMessage(getPrefix() + ChatColor.YELLOW + "Level up!");
                for (int x = player.getLocation().getBlockX() - 4; x < player.getLocation().getBlockX() + 4; x++) {
                    for (int z = player.getLocation().getBlockZ() - 4; z < player.getLocation().getBlockZ() + 4; z++) {
                        Location location = player.getWorld().getBlockAt(x, player.getLocation().getBlockY(), z).getLocation();
                        if (player.getLocation().distance(location) > 3 && player.getLocation().distance(location) < 5) {
                            Firework firework = (Firework) player.getWorld().spawnEntity(location, EntityType.FIREWORK);
                            FireworkMeta meta = firework.getFireworkMeta();
                            meta.setPower(0);
                            FireworkEffect effect = FireworkEffect.builder().withColor(Color.YELLOW, Color.RED).with(FireworkEffect.Type.BURST).trail(true).build();
                            meta.addEffect(effect);
                            firework.setFireworkMeta(meta);
                        }
                    }
                }
            }
        }
        File characterDirectory = new File(getDataFolder(), "characters");
        File characterFile = new File(characterDirectory, character.getId() + ".yml");
        YamlConfiguration characterConfig = YamlConfiguration.loadConfiguration(characterFile);
        characterConfig.set("experience." + clazz.getName(), Math.min(amount, getTotalExperienceForLevel(clazz.getMaxLevel())));
        try {
            characterConfig.save(characterFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        OfflinePlayer offlinePlayer = character.getPlayer();
        if (offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();
            updateExp(player);
            updateHealth(player);
        }
    }

    @Override
    public int getTotalExperienceForLevel(int level) {
        return level * (level - 1) * 500;
    }

    @Override
    public int getExpToNextLevel(int level) {
        return 1000 * level;
    }

    public Collection<Class> getClasses() {
        return classes.values();
    }

    @Override
    public void addClass(Class clazz) {
        classes.put(clazz.getName(), clazz);
    }

    @Override
    public void removeClass(Class clazz) {
        classes.remove(clazz.getName());
    }

    public void updateExp(Player player) {
        player.setExp((float) getExperienceTowardsNextLevel(player) / (float) getExpToNextLevel(getLevel(player)));
        player.setLevel(getLevel(player));
    }

    public void updateHealth(Player player) {
        CharacterPlugin characterPlugin = getServer().getServicesManager().getRegistration(CharacterPlugin.class).getProvider();
        player.setMaxHealth(characterPlugin.getActiveCharacter(player).getMaxHealth());
    }

}
