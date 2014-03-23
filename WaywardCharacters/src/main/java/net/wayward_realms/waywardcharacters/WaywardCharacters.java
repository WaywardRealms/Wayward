package net.wayward_realms.waywardcharacters;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Gender;
import net.wayward_realms.waywardlib.character.Race;
import net.wayward_realms.waywardlib.classes.ClassesPlugin;
import net.wayward_realms.waywardlib.combat.CombatPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class WaywardCharacters extends JavaPlugin implements CharacterPlugin {

    private Map<Integer, net.wayward_realms.waywardlib.character.Character> characters = new HashMap<>();
    private Map<String, Integer> activeCharacters = new HashMap<>();
    private Map<String, Set<Integer>> playerCharacters = new HashMap<>();
    private Map<String, Gender> genders = new HashMap<>();
    private Map<String, Race> races = new HashMap<>();
    private Map<String, Long> kitClaims = new HashMap<>();

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(CharacterImpl.class);
        ConfigurationSerialization.registerClass(GenderImpl.class);
        ConfigurationSerialization.registerClass(RaceImpl.class);
        ConfigurationSerialization.registerClass(RaceKit.class);
        saveDefaultConfig();
        registerListeners(new EntityDamageListener(this), new EntityRegainHealthListener(this), new FoodLevelChangeListener(this), new PlayerItemConsumeListener(this), new PlayerInteractListener(this), new PlayerInteractEntityListener(this), new PlayerJoinListener(this), new PlayerLoginListener(this), new PlayerRespawnListener(this),new SignChangeListener(this));
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
                for (Player player : getServer().getOnlinePlayers()) {
                    Character character = getActiveCharacter(player);
                    if (character.getThirst() > 0) {
                        character.setThirst(character.getThirst() - 1);
                        player.sendMessage(getPrefix() + ChatColor.RED + "Thirst: -1" + ChatColor.GRAY + " (Total: " + character.getThirst() + ")");
                    }
                    if (character.getThirst() < 5) {
                    	character.setHealth(character.getHealth() - 1);
                        player.sendMessage(getPrefix() + ChatColor.RED + "You are very thirsty, be sure to drink something soon!");
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
        File gendersFile = new File(getDataFolder().getPath() + File.separator + "genders.yml");
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
        File racesFile = new File(getDataFolder().getPath() + File.separator + "races.yml");
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
        File characterDirectory = new File(getDataFolder().getPath() + File.separator + "characters");
        if (characterDirectory.exists()) {
            for (File file : characterDirectory.listFiles()) {
                YamlConfiguration characterSave = new YamlConfiguration();
                try {
                    characterSave.load(file);
                    if (characterSave.contains("character")) {
                        if (characterSave.get("character") instanceof CharacterImpl) {
                            CharacterImpl character = (CharacterImpl) characterSave.get("character");
                            characters.put(character.getId(), character);
                        } else {
                            getLogger().severe("Failed to load character from " + file.getPath() + ": file did not specify a valid character");
                        }
                    } else {
                        getLogger().severe("Failed to load character from " + file.getPath() + ": file did not specify a valid character");
                    }
                } catch (FileNotFoundException exception) {
                    getLogger().severe("Failed to load character data from " + file.getPath() + ": the file did not exist.");
                    exception.printStackTrace();
                } catch (IOException exception) {
                    getLogger().severe("Failed to load character data from " + file.getPath() + ": encountered I/O exception.");
                    exception.printStackTrace();
                } catch (InvalidConfigurationException exception) {
                    getLogger().severe("Failed to load character data from " + file.getPath() + ": file was invalid.");
                    exception.printStackTrace();
                }
            }
        }
        // Player character associations
        File playerDataDirectory = new File(getDataFolder().getPath() + File.separator + "players");
        if (playerDataDirectory.exists()) {
            for (File file : playerDataDirectory.listFiles()) {
                YamlConfiguration playerSave = new YamlConfiguration();
                try {
                    playerSave.load(file);
                    if (playerSave.contains("active-character")) {
                        activeCharacters.put(file.getName().replace(".yml", ""), playerSave.getInt("active-character"));
                    }
                    if (playerSave.contains("characters")) {
                        playerCharacters.put(file.getName().replace(".yml", ""), (Set<Integer>) playerSave.get("characters"));
                    }
                } catch (FileNotFoundException exception) {
                    getLogger().severe("Failed to load player data from " + file.getPath() + ": the file did not exist.");
                    exception.printStackTrace();
                } catch (IOException exception) {
                    getLogger().severe("Failed to load player data from " + file.getPath() + ": encountered I/O exception.");
                    exception.printStackTrace();
                } catch (InvalidConfigurationException exception) {
                    getLogger().severe("Failed to load player data from " + file.getPath() + ": file was invalid.");
                    exception.printStackTrace();
                }
            }
        }
        // Race kit claims
        File raceKitClaimFile = new File(getDataFolder().getPath() + File.separator + "racekitclaims.yml");
        if (raceKitClaimFile.exists()) {
            YamlConfiguration raceKitClaimSave = new YamlConfiguration();
            try {
                raceKitClaimSave.load(raceKitClaimFile);
                for (String playerName : raceKitClaimSave.getKeys(false)) {
                    kitClaims.put(playerName, raceKitClaimSave.getLong(playerName));
                }
            } catch (IOException | InvalidConfigurationException exception) {
                exception.printStackTrace();
            }
        }
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
        // Characters
        File characterDirectory = new File(getDataFolder().getPath() + File.separator + "characters");
        if (characterDirectory.exists()) {
            characterDirectory.delete();
        }
        characterDirectory.mkdir();
        for (Character character : characters.values()) {
            File file = new File(characterDirectory.getPath() + File.separator + character.getId() + ".yml");
            YamlConfiguration characterSave = new YamlConfiguration();
            characterSave.set("character", character);
            try {
                characterSave.save(file);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        // Player character associations
        File playerDirectory = new File(getDataFolder().getPath() + File.separator + "players");
        if (playerDirectory.exists()) {
            playerDirectory.delete();
        }
        playerDirectory.mkdir();
        for (String player : activeCharacters.keySet()) {
            File file = new File(playerDirectory + File.separator + player + ".yml");
            YamlConfiguration playerSave = new YamlConfiguration();
            playerSave.set("active-character", activeCharacters.get(player));
            playerSave.set("characters", playerCharacters.get(player));
            try {
                playerSave.save(file);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        // Race kit claims
        File raceKitClaimFile = new File(getDataFolder().getPath() + File.separator + "racekitclaims.yml");
        YamlConfiguration raceKitClaimSave = new YamlConfiguration();
        for (String playerName : kitClaims.keySet()) {
            raceKitClaimSave.set(playerName, kitClaims.get(playerName));
        }
        try {
            raceKitClaimSave.save(raceKitClaimFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Character getActiveCharacter(OfflinePlayer player) {
        if (activeCharacters.get(player.getName()) == null) {
            CharacterImpl character = new CharacterImpl(player);
            characters.put(character.getId(), character);
            activeCharacters.put(player.getName(), character.getId());
        }
        return characters.get(activeCharacters.get(player.getName()));
    }

    @Override
    public Set<Character> getCharacters(OfflinePlayer player) {
        if (playerCharacters.get(player.getName()) == null) {
            playerCharacters.put(player.getName(), new HashSet<Integer>());
        }
        for (Character character : characters.values()) {
            if (character.getPlayer() == player) {
                playerCharacters.get(player.getName()).add(character.getId());
            }
        }
        Set<Character> characters = new HashSet<>();
        for (int characterId : playerCharacters.get(player.getName())) {
            characters.add(this.characters.get(characterId));
        }
        return characters;
    }

    @Override
    public void addCharacter(OfflinePlayer player, Character character) {
        character.setPlayer(player);
        if (playerCharacters.get(player.getName()) == null) {
            playerCharacters.put(player.getName(), new HashSet<Integer>());
        }
        playerCharacters.get(player.getName()).add(character.getId());
        if (!characters.containsKey(character.getId())) {
            characters.put(character.getId(), character);
        }
    }

    @Override
    public void removeCharacter(OfflinePlayer player, Character character) {
        playerCharacters.get(player.getName()).remove(character.getId());
    }

    @Override
    public void removeCharacter(Character character) {
        Set<String> playersToRemove = new HashSet<>();
        for (String playerName : playerCharacters.keySet()) {
            if (playerCharacters.get(playerName).contains(character.getId())) {
                playersToRemove.add(playerName);
            }
        }
        for (String playerName : playersToRemove) {
            playerCharacters.get(playerName).remove(character.getId());
        }
    }

    @Override
    public void setActiveCharacter(Player player, Character character) {
        if (this.getActiveCharacter(player) != null) {
            Character activeCharacter = this.getActiveCharacter(player);
            activeCharacter.setInventoryContents(player.getInventory().getContents());
            activeCharacter.setLocation(player.getLocation());
            activeCharacter.setHealth(player.getHealth());
            activeCharacter.setFoodLevel(player.getFoodLevel());
        }
        addCharacter(player, character);
        this.activeCharacters.put(player.getName(), character.getId());
        player.getInventory().setContents(character.getInventoryContents());
        player.teleport(character.getLocation());
        characters.put(character.getId(), character);
        player.setDisplayName(character.getName());
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
        return characters.get(id);
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
        return new CharacterImpl(player);
    }

    public long getRaceKitClaim(OfflinePlayer player) {
        return kitClaims.get(player.getName());
    }

    public void setRaceKitClaim(OfflinePlayer player) {
        kitClaims.put(player.getName(), System.currentTimeMillis());
    }

    public boolean canClaimRaceKit(OfflinePlayer player) {
        return kitClaims.get(player.getName()) == null || System.currentTimeMillis() - kitClaims.get(player.getName()) > 86400000L;
    }

}
