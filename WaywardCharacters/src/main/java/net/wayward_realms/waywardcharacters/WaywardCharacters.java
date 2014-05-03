package net.wayward_realms.waywardcharacters;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Gender;
import net.wayward_realms.waywardlib.character.Race;
import net.wayward_realms.waywardlib.classes.ClassesPlugin;
import net.wayward_realms.waywardlib.combat.CombatPlugin;
import org.bukkit.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.*;

public class WaywardCharacters extends JavaPlugin implements CharacterPlugin {

    private Map<String, Gender> genders = new HashMap<>();
    private Map<String, Race> races = new HashMap<>();

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(CharacterImpl.class);
        ConfigurationSerialization.registerClass(GenderImpl.class);
        ConfigurationSerialization.registerClass(RaceImpl.class);
        ConfigurationSerialization.registerClass(RaceKit.class);
        saveDefaultConfig();
        registerListeners(new EntityDamageListener(this), new EntityRegainHealthListener(this), new FoodLevelChangeListener(this), new PlayerItemConsumeListener(this), new PlayerInteractListener(this), new PlayerInteractEntityListener(this), new PlayerJoinListener(this), new PlayerLoginListener(this), new PlayerRespawnListener(this), new SignChangeListener(this), new PlayerEditBookListener(this));
        getCommand("character").setExecutor(new CharacterCommand(this));
        getCommand("racekit").setExecutor(new RaceKitCommand(this));
        getCommand("stats").setExecutor(new StatsCommand(this));
        getCommand("skillpoints").setExecutor(new SkillPointsCommand(this));
        setupRegen();
        setupHungerSlowdown();
    }
    
    private void setupRegen() {
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                for (Player player : getServer().getOnlinePlayers()) {
                    if (player.getGameMode() != GameMode.CREATIVE) {
                        Character character = getActiveCharacter(player);
                        if (character.getThirst() > 0 && random.nextInt(100) <= 5) {
                            character.setThirst(character.getThirst() - 1);
                            player.sendMessage(getPrefix() + ChatColor.RED + "Thirst: -1" + ChatColor.GRAY + " (Total: " + character.getThirst() + ")");
                        }
                        if (character.getThirst() < 5) {
                            character.setHealth(character.getHealth() - 1);
                            player.sendMessage(getPrefix() + ChatColor.RED + "You are very thirsty, be sure to drink something soon! " + ChatColor.GRAY + "(Health: -1)");
                        }
                        RegisteredServiceProvider<CombatPlugin> combatPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CombatPlugin.class);
                        if (combatPluginProvider != null) {
                            CombatPlugin combatPlugin = combatPluginProvider.getProvider();
                            if (combatPlugin.getActiveFight(character) != null) {
                                continue;
                            }
                        }
                        int manaRegen = Math.min(character.getMana() + Math.max(character.getMaxMana() / 50, 1), character.getMaxMana()) - character.getMana();
                        character.setMana(Math.min(character.getMana() + Math.max(character.getMaxMana() / 50, 1), character.getMaxMana()));
                        if (manaRegen > 0) {
                            player.sendMessage(getPrefix() + ChatColor.GREEN + "Mana regenerated: " + manaRegen);
                        }
                        if (player.getFoodLevel() >= 15) {
                            double healthRegen;
                            if (player.isSleeping()) {
                                healthRegen = Math.min(character.getHealth() + (character.getMaxHealth() / 5), character.getMaxHealth()) - character.getHealth();
                                character.setHealth(Math.min(character.getHealth() + (character.getMaxHealth() / 5), character.getMaxHealth()));
                            } else {
                                healthRegen = Math.min(character.getHealth() + (character.getMaxHealth() / 20), character.getMaxHealth()) - character.getHealth();
                                character.setHealth(Math.min(character.getHealth() + (character.getMaxHealth() / 20), character.getMaxHealth()));
                            }
                            if (healthRegen > 0) {
                                player.sendMessage(getPrefix() + ChatColor.GREEN + "Health regenerated: " + healthRegen);
                            }
                        }
                        player.setMaxHealth(character.getMaxHealth());
                        player.setHealth(Math.max(character.getHealth(), 0));
                    }
                }
            }
        }, 500L, 500L);
    }

    private void setupHungerSlowdown() {
        float hungerModifier = -4F;
        float newExhaustStartLevel = 0.0F;
        if (hungerModifier > 0.0F) {
            newExhaustStartLevel = 4.0F / hungerModifier * (hungerModifier - 1.0F);
        }
        if (hungerModifier < 0.0F) {
            newExhaustStartLevel = hungerModifier * 4.0F - 1.0F;
        }
        final float finalNewExhaustStartLevel = newExhaustStartLevel;
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
            for (Player player : getServer().getOnlinePlayers()) {
                float currentExhaustion = player.getExhaustion();
                if (((currentExhaustion > -1.0F ? 1 : 0) & (currentExhaustion < 0.0F ? 1 : 0)) != 0) {
                    player.setExhaustion(4.0F);
                }
                if (((currentExhaustion > 0.0F ? 1 : 0) & (currentExhaustion < 4.0F ? 1 : 0)) != 0) {
                    player.setExhaustion(finalNewExhaustStartLevel);
                }
            }
            }
        }, 0L, 1L);
    }

    @Override
    public void onDisable() {
        saveState();
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            this.getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    @Override
    public String getPrefix() {
        //return "" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "|" + ChatColor.RESET + ChatColor.BLUE + "WaywardCharacters" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "| " + ChatColor.RESET;
        return "";
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loadState() {
        // Genders
        File gendersFile = new File(getDataFolder(), "genders.yml");
        if (gendersFile.exists()) {
            YamlConfiguration gendersConfig = new YamlConfiguration();
            try {
                gendersConfig.load(gendersFile);
                for (String section : gendersConfig.getKeys(false)) {
                    genders.put(section, (GenderImpl) gendersConfig.get(section));
                }
            } catch (IOException | InvalidConfigurationException exception) {
                exception.printStackTrace();
            }
        }
        if (genders.isEmpty()) {
            getLogger().info("There are no genders, creating defaults.");
            genders.put("UNKNOWN", new GenderImpl("UNKNOWN"));
            genders.put("MALE", new GenderImpl("MALE"));
            genders.put("FEMALE", new GenderImpl("FEMALE"));
            genders.put("AGENDER", new GenderImpl("AGENDER"));
            genders.put("ANDROGYNE", new GenderImpl("ANDROGYNE"));
            genders.put("BIGENDER", new GenderImpl("BIGENDER"));
            genders.put("CISGENDER", new GenderImpl("CISGENDER"));
            genders.put("CISSEXUAL", new GenderImpl("CISSEXUAL"));
            genders.put("GENDERQUEER", new GenderImpl("GENDERQUEER"));
            genders.put("HIJRA", new GenderImpl("HIJRA"));
            genders.put("PANGENDER", new GenderImpl("PANGENDER"));
            genders.put("TRANS_MAN", new GenderImpl("TRANS_MAN"));
            genders.put("TRANS_WOMAN", new GenderImpl("TRANS_WOMAN"));
            genders.put("TRIGENDER", new GenderImpl("TRIGENDER"));
            genders.put("TWO_SPIRIT", new GenderImpl("TWO_SPIRIT"));
        }
        // Races
        File racesFile = new File(getDataFolder(), "races.yml");
        if (racesFile.exists()) {
            YamlConfiguration racesConfig = new YamlConfiguration();
            try {
                racesConfig.load(racesFile);
                for (String section : racesConfig.getKeys(false)) {
                    races.put(section, (RaceImpl) racesConfig.get(section));
                }
            } catch (IOException | InvalidConfigurationException exception) {
                exception.printStackTrace();
            }
        }
        if (races.isEmpty()) {
            getLogger().info("There are no races, creating defaults.");
            races.put("UNKNOWN", new RaceImpl("Unknown"));
            races.put("HUMAN", new RaceImpl("Human"));
            RaceKit arrows = new RaceKit();
            arrows.addItem(new ItemStack(Material.ARROW, 4));
            races.put("ELF", new RaceImpl("Elf", arrows));
        }
        // Characters
        // Old character conversion
        File oldCharacterDirectory = new File(getDataFolder(), "characters");
        if (oldCharacterDirectory.exists()) {
            for (File file : oldCharacterDirectory.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().endsWith(".yml");
                }
            })) {
                YamlConfiguration oldcharacterSave = YamlConfiguration.loadConfiguration(file);
                if (oldcharacterSave.get("character") != null) {
                    if (oldcharacterSave.get("character") instanceof CharacterImpl) {
                        oldcharacterSave.get("character");
                    }
                }
            }
            delete(oldCharacterDirectory);
        }

        File characterDirectory = new File(getDataFolder(), "characters-new");
        if (characterDirectory.exists()) {
            for (File file : characterDirectory.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.getPath().endsWith(".yml");
                }
            })) {
                int id = Integer.parseInt(file.getName().replace(".yml", ""));
                if (id > CharacterImpl.getNextId()) CharacterImpl.setNextId(id);
            }
        }
    }

    private void delete(File file) {
        if (file.isDirectory()) {
            for (File childFile : file.listFiles()) {
                delete(childFile);
            }
        }
        file.delete();
    }

    @Override
    public void saveState() {
        // Genders
        File gendersFile = new File(getDataFolder().getPath() + File.separator + "genders.yml");
        YamlConfiguration gendersConfig = new YamlConfiguration();
        for (String genderName : genders.keySet()) {
            gendersConfig.set(genderName, genders.get(genderName));
        }
        try {
            gendersConfig.save(gendersFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        // Races
        File racesFile = new File(getDataFolder().getPath() + File.separator + "races.yml");
        YamlConfiguration racesConfig = new YamlConfiguration();
        for (String raceName : races.keySet()) {
            racesConfig.set(raceName, races.get(raceName));
        }
        try {
            racesConfig.save(racesFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Character getActiveCharacter(OfflinePlayer player) {
        YamlConfiguration playerSave = YamlConfiguration.loadConfiguration(new File(new File(getDataFolder(), "players"), player.getName() + ".yml"));
        if (playerSave.get("active-character") == null) {
            CharacterImpl character = new CharacterImpl(this, player);
            playerSave.set("active-character", character.getId());
        }
        return getCharacter(playerSave.getInt("active-character"));
    }

    @Override
    public Set<Character> getCharacters(OfflinePlayer player) {
        YamlConfiguration playerSave = YamlConfiguration.loadConfiguration(new File(new File(getDataFolder(), "players"), player.getName() + ".yml"));
        if (playerSave.get("characters") == null) {
            playerSave.set("characters", new HashSet<Integer>());
        }
        Set<Character> characters = new HashSet<>();
        for (int cid : (Set<Integer>) playerSave.get("characters")) {
            characters.add(getCharacter(cid));
        }
        return characters;
    }

    @Override
    public void addCharacter(OfflinePlayer player, Character character) {
        YamlConfiguration playerSave = YamlConfiguration.loadConfiguration(new File(new File(getDataFolder(), "players"), player.getName() + ".yml"));
        character.setPlayer(player);
        if (playerSave.get("characters") == null) {
            playerSave.set("characters", new HashSet<Integer>());
        }
        Set<Integer> cids = (Set<Integer>) playerSave.get("characters");
        cids.add(character.getId());
        playerSave.set("characters", cids);
        try {
            playerSave.save(new File(new File(getDataFolder(), "players"), player.getName() + ".yml"));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void removeCharacter(OfflinePlayer player, Character character) {
        YamlConfiguration playerSave = YamlConfiguration.loadConfiguration(new File(new File(getDataFolder(), "players"), player.getName() + ".yml"));
        character.setPlayer(player);
        if (playerSave.get("characters") == null) {
            playerSave.set("characters", new HashSet<Integer>());
        }
        Set<Integer> cids = (Set<Integer>) playerSave.get("characters");
        cids.remove(character.getId());
        playerSave.set("characters", cids);
        try {
            playerSave.save(new File(new File(getDataFolder(), "players"), player.getName() + ".yml"));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void removeCharacter(Character character) {
        throw new UnsupportedOperationException("Removing a character breaks stuff! Don't do it!");
    }

    @Override
    public void setActiveCharacter(Player player, Character character) {
        if (getActiveCharacter(player) != null) {
            Character activeCharacter = getActiveCharacter(player);
            activeCharacter.setInventoryContents(player.getInventory().getContents());
            activeCharacter.setLocation(player.getLocation());
            activeCharacter.setHealth(player.getHealth());
            activeCharacter.setFoodLevel(player.getFoodLevel());
        }
        addCharacter(player, character);
        File playerDirectory = new File(getDataFolder(), "players");
        File playerFile = new File(playerDirectory, player.getName() + ".yml");
        YamlConfiguration playerSave = YamlConfiguration.loadConfiguration(playerFile);
        playerSave.set("active-character", character.getId());
        try {
            playerSave.save(playerFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        player.getInventory().setContents(character.getInventoryContents());
        player.teleport(character.getLocation());
        player.setDisplayName(character.isNameHidden() ? ChatColor.MAGIC + character.getName() + ChatColor.RESET : character.getName());
        player.setMaxHealth(character.getMaxHealth());
        player.setHealth(Math.max(character.getHealth(), 0));
        player.setFoodLevel(character.getFoodLevel());
        RegisteredServiceProvider<ClassesPlugin> classesPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(ClassesPlugin.class);
        if (classesPluginProvider != null) {
            ClassesPlugin classesPlugin = classesPluginProvider.getProvider();
            player.setExp((float) classesPlugin.getExperienceTowardsNextLevel(player) / (float) classesPlugin.getExpToNextLevel(classesPlugin.getLevel(player)));
            player.setLevel(classesPlugin.getLevel(player));
        }
    }

    @Override
    public Character getCharacter(int id) {
        File newCharacterDirectory = new File(getDataFolder(), "characters-new");
        File newCharacterFile = new File(newCharacterDirectory, id + ".yml");
        return new CharacterImpl(newCharacterFile);
    }

    @Override
    public Collection<Gender> getGenders() {
        return genders.values();
    }

    @Override
    public Gender getGender(String name) {
        return genders.get(name);
    }

    @Override
    public void addGender(Gender gender) {
        genders.put(gender.getName(), gender);
    }

    @Override
    public void removeGender(Gender gender) {
        genders.remove(gender.getName());
    }

    @Override
    public Collection<Race> getRaces() {
        return races.values();
    }

    @Override
    public Race getRace(String name) {
        return races.get(name.toUpperCase());
    }

    @Override
    public void addRace(Race race) {
        races.put(race.getName(), race);
    }

    @Override
    public void removeRace(Race race) {
        races.remove(race.getName());
    }

    @Override
    public CharacterImpl createNewCharacter(OfflinePlayer player) {
        return new CharacterImpl(this, player);
    }

    public long getRaceKitClaim(OfflinePlayer player) {
        YamlConfiguration raceKitClaimSave = YamlConfiguration.loadConfiguration(new File("racekitclaims.yml"));
        return raceKitClaimSave.getLong(player.getName());
    }

    public void setRaceKitClaim(OfflinePlayer player) {
        File raceKitClaimFile = new File("racekitclaims.yml");
        YamlConfiguration raceKitClaimSave = YamlConfiguration.loadConfiguration(new File("racekitclaims.yml"));
        raceKitClaimSave.set(player.getName(), System.currentTimeMillis());
        try {
            raceKitClaimSave.save(raceKitClaimFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public boolean canClaimRaceKit(OfflinePlayer player) {
        YamlConfiguration raceKitClaimSave = YamlConfiguration.loadConfiguration(new File("racekitclaims.yml"));
        return raceKitClaimSave.get(player.getName()) == null || System.currentTimeMillis() - raceKitClaimSave.getLong(player.getName()) > 86400000L;
    }

}
