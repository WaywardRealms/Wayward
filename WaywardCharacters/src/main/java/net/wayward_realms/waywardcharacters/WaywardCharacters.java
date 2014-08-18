package net.wayward_realms.waywardcharacters;

import net.wayward_realms.waywardlib.character.*;
import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.classes.ClassesPlugin;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.combat.CombatPlugin;
import net.wayward_realms.waywardlib.events.EventsPlugin;
import net.wayward_realms.waywardlib.util.file.filter.YamlFileFilter;

import net.wayward_realms.waywardlib.util.player.PlayerNamePlateUtils;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class WaywardCharacters extends JavaPlugin implements CharacterPlugin {

    private Map<String, Gender> genders = new HashMap<>();
    private Map<String, Race> races = new HashMap<>();

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(GenderImpl.class);
        ConfigurationSerialization.registerClass(RaceImpl.class);
        ConfigurationSerialization.registerClass(RaceKit.class);
        saveDefaultConfig();
        registerListeners(new EntityDamageListener(this), new EntityRegainHealthListener(this), new FoodLevelChangeListener(this), new PlayerItemConsumeListener(this), new PlayerInteractListener(this), new PlayerInteractEntityListener(this), new PlayerJoinListener(this), new PlayerLoginListener(this), new PlayerRespawnListener(this), new SignChangeListener(this), new PlayerEditBookListener(this), new PlayerNamePlateChangeListener(this));
        getCommand("character").setExecutor(new CharacterCommand(this));
        getCommand("racekit").setExecutor(new RaceKitCommand(this));
        getCommand("stats").setExecutor(new StatsCommand(this));
        getCommand("skillpoints").setExecutor(new SkillPointsCommand(this));
        getCommand("togglethirst").setExecutor(new ToggleThirstCommand(this));
        getCommand("togglehunger").setExecutor(new ToggleHungerCommand(this));
        getCommand("party").setExecutor(new PartyCommand(this));
        getCommand("freezehealth").setExecutor(new FreezeHealthCommand(this));
        setupRegen();
        setupHungerSlowdown();
        setupPartyCleanup();
    }
    
    private void setupRegen() {
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                for (Player player : getServer().getOnlinePlayers()) {
                    if (player.getGameMode() != GameMode.CREATIVE) {
                        Character character = getActiveCharacter(player);
                        int decreaseChance = checkBiome(player.getLocation().getBlock().getBiome());
                        if (!isThirstDisabled(player)) {
                            if (character.getThirst() > 0 && random.nextInt(100) <= decreaseChance) {
                                character.setThirst(character.getThirst() - 1);
                                player.sendMessage(getPrefix() + ChatColor.RED + "Thirst: -1" + ChatColor.GRAY + " (Total: " + character.getThirst() + ")");
                            }
                            if (character.getThirst() < 5 && character.getHealth() > 1) {
                                character.setHealth(character.getHealth() - 1);
                                player.sendMessage(getPrefix() + ChatColor.RED + "You are very thirsty, be sure to drink something soon! " + ChatColor.GRAY + "(Health: -1)");
                            }
                        }
                        RegisteredServiceProvider<CombatPlugin> combatPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CombatPlugin.class);
                        if (combatPluginProvider != null) {
                            CombatPlugin combatPlugin = combatPluginProvider.getProvider();
                            if (combatPlugin.getActiveFight(character) != null) {
                                continue;
                            }
                        }
                        int manaRegen = Math.min(character.getMana() + Math.max(character.getMaxMana() / 30, 1), character.getMaxMana()) - character.getMana();
                        character.setMana(Math.min(character.getMana() + Math.max(character.getMaxMana() / 30, 1), character.getMaxMana()));
                        if (manaRegen > 0) {
                            player.sendMessage(getPrefix() + ChatColor.GREEN + "Mana regenerated: " + manaRegen);
                        }
                        if (!isHealthFrozen(player)) {
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
                                    player.sendMessage(getPrefix() + ChatColor.GREEN + "Health regenerated: " + (Math.round(healthRegen * 100D) / 100D));
                                }
                            }
                            player.setMaxHealth(character.getMaxHealth());
                            player.setHealth(Math.max(character.getHealth(), 0));
                        } else {
                            player.sendMessage(ChatColor.RED + "Health regen is disabled.");
                        }
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
                if (isHungerDisabled(player)) {
                    player.setFoodLevel(20);
                    player.setExhaustion(0.0F);
                } else {
                    float currentExhaustion = player.getExhaustion();
                    if (((currentExhaustion > -1.0F ? 1 : 0) & (currentExhaustion < 0.0F ? 1 : 0)) != 0) {
                        player.setExhaustion(4.0F);
                    }
                    if (((currentExhaustion > 0.0F ? 1 : 0) & (currentExhaustion < 4.0F ? 1 : 0)) != 0) {
                        player.setExhaustion(finalNewExhaustStartLevel);
                    }
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
            Map<Stat, Integer> statBonuses = new EnumMap<>(Stat.class);
            statBonuses.put(Stat.SPEED, 2);
            statBonuses.put(Stat.MELEE_DEFENCE, -2);
            races.put("ELF", new RaceImpl("Elf", arrows, statBonuses));
        }
        // Characters
        File characterDirectory = new File(getDataFolder(), "characters-new");
        if (characterDirectory.exists()) {
            for (File file : characterDirectory.listFiles(new YamlFileFilter())) {
                int id = Integer.parseInt(file.getName().replace(".yml", ""));
                if (id > CharacterImpl.getNextId()) CharacterImpl.setNextId(id);
            }
        }
        File partyDirectory = new File(getDataFolder(), "parties");
        if (partyDirectory.exists()) {
            for (File file : partyDirectory.listFiles(new YamlFileFilter())) {
                int id = Integer.parseInt(file.getName().replace(".yml", ""));
                if (id > PartyImpl.getNextId()) PartyImpl.setNextId(id);
            }
        }
        // UUID conversion
        File playerDirectory = new File(getDataFolder(), "players");
        if (playerDirectory.exists()) {
            File playerUUIDDirectory = new File(getDataFolder(), "players-uuid");
            if (!playerUUIDDirectory.exists()) playerUUIDDirectory.mkdir();
            for (File file : playerDirectory.listFiles(new YamlFileFilter())) {
                String playerName = file.getName().replace(".yml", "");
                OfflinePlayer player = getServer().getOfflinePlayer(playerName);
                UUID uuid = player.getUniqueId();
                File playerUUIDFile = new File(playerUUIDDirectory, uuid.toString() + ".yml");
                if (!playerUUIDFile.exists()) {
                    try {
                        Files.copy(Paths.get(file.getPath()), Paths.get(playerUUIDFile.getPath()));
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
            delete(playerDirectory);
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
        File playerDirectory = new File(getDataFolder(), "players-uuid");
        File playerFile = new File(playerDirectory, player.getUniqueId().toString() + ".yml");
        YamlConfiguration playerSave = YamlConfiguration.loadConfiguration(playerFile);
        if (playerSave.get("active-character") == null) {
            return null;
        }
        return getCharacter(playerSave.getInt("active-character"));
    }

    @Override
    public Set<Character> getCharacters(OfflinePlayer player) {
        YamlConfiguration playerSave = YamlConfiguration.loadConfiguration(new File(new File(getDataFolder(), "players-uuid"), player.getUniqueId().toString() + ".yml"));
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
        YamlConfiguration playerSave = YamlConfiguration.loadConfiguration(new File(new File(getDataFolder(), "players-uuid"), player.getUniqueId().toString() + ".yml"));
        character.setPlayer(player);
        if (playerSave.get("characters") == null) {
            playerSave.set("characters", new HashSet<Integer>());
        }
        Set<Integer> cids = (Set<Integer>) playerSave.get("characters");
        cids.add(character.getId());
        playerSave.set("characters", cids);
        try {
            playerSave.save(new File(new File(getDataFolder(), "players-uuid"), player.getUniqueId().toString() + ".yml"));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void removeCharacter(OfflinePlayer player, Character character) {
        YamlConfiguration playerSave = YamlConfiguration.loadConfiguration(new File(new File(getDataFolder(), "players-uuid"), player.getUniqueId().toString() + ".yml"));
        character.setPlayer(player);
        if (playerSave.get("characters") == null) {
            playerSave.set("characters", new HashSet<Integer>());
        }
        Set<Integer> cids = (Set<Integer>) playerSave.get("characters");
        cids.remove(character.getId());
        playerSave.set("characters", cids);
        try {
            playerSave.save(new File(new File(getDataFolder(), "players-uuid"), player.getUniqueId().toString() + ".yml"));
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
            activeCharacter.setHelmet(player.getInventory().getHelmet());
            activeCharacter.setChestplate(player.getInventory().getChestplate());
            activeCharacter.setLeggings(player.getInventory().getLeggings());
            activeCharacter.setBoots(player.getInventory().getBoots());
            activeCharacter.setInventoryContents(player.getInventory().getContents());
            activeCharacter.setLocation(player.getLocation());
            activeCharacter.setHealth(player.getHealth());
            activeCharacter.setFoodLevel(player.getFoodLevel());
        }
        addCharacter(player, character);
        File playerDirectory = new File(getDataFolder(), "players-uuid");
        File playerFile = new File(playerDirectory, player.getUniqueId() + ".yml");
        YamlConfiguration playerSave = YamlConfiguration.loadConfiguration(playerFile);
        playerSave.set("active-character", character.getId());
        try {
            playerSave.save(playerFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        player.getInventory().setHelmet(character.getHelmet());
        player.getInventory().setChestplate(character.getChestplate());
        player.getInventory().setLeggings(character.getLeggings());
        player.getInventory().setBoots(character.getBoots());
        player.getInventory().setContents(character.getInventoryContents());
        player.teleport(character.getLocation());
        player.setDisplayName(character.isNameHidden() ? ChatColor.MAGIC + character.getName() + ChatColor.RESET : character.getName());
        PlayerNamePlateUtils.refreshPlayer(player);
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
        if (newCharacterFile.exists()) {
            Character character = new CharacterImpl(this, newCharacterFile);
            character.getPlayer(); // UUID conversion
            return character;
        } else {
            RegisteredServiceProvider<EventsPlugin> eventsPluginProvider = getServer().getServicesManager().getRegistration(EventsPlugin.class);
            if (eventsPluginProvider != null) {
                EventsPlugin eventsPlugin = eventsPluginProvider.getProvider();
                return eventsPlugin.getEventCharacter(id);
            }
        }
        return null;
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
    public int getNextAvailableId() {
        return CharacterImpl.getNextId();
    }

    @Override
    public void setNextAvailableId(int id) {
        CharacterImpl.setNextId(id);
    }

    @Override
    public void incrementNextAvailableId() {
        CharacterImpl.nextAvailableId();
    }

    @Override
    public Party getParty(Character character) {
        for (Party party : getParties()) {
            for (Character member : party.getMembers()) {
                if (member.getId() == character.getId()) return party;
            }
        }
        return null;
    }

    public Set<Party> getParties() {
        File partyDirectory = new File(getDataFolder(), "parties");
        Set<Party> parties = new HashSet<>();
        if (partyDirectory.exists()) {
            for (File file : partyDirectory.listFiles(new YamlFileFilter())) {
                parties.add(new PartyImpl(this, file));
            }
        }
        return parties;
    }

    public void addParty(Party party) {
        if (!(party instanceof PartyImpl)) {
            new PartyImpl(this, party);
        }
    }

    public void removeParty(Party party) {
        File partyDirectory = new File(getDataFolder(), "parties");
        File partyFile = new File(partyDirectory, party.getId() + ".yml");
        if (partyFile.exists()) partyFile.delete();
    }

    public void setupPartyCleanup() {
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                Set<Party> partiesToRemove = new HashSet<>();
                for (Party party : getParties()) {
                    Set<Character> membersToRemove = new HashSet<>();
                    for (Character character : party.getMembers()) {
                        if (!character.getPlayer().isOnline()) membersToRemove.add(character);
                    }
                    for (Character character : membersToRemove) {
                        party.removeMember(character);
                    }
                    Set<Character> inviteesToRemove = new HashSet<>();
                    for (Character character : party.getInvitees()) {
                        if (!character.getPlayer().isOnline()) inviteesToRemove.add(character);
                    }
                    for (Character character : inviteesToRemove) {
                        party.uninvite(character);
                    }
                    if (party.getMembers().size() <= 1 && ((System.currentTimeMillis() - party.getCreationDate().getTime()) / 60000) > 5) partiesToRemove.add(party);
                }
                for (Party party : partiesToRemove) {
                    for (Character character : party.getMembers()) {
                        OfflinePlayer player = character.getPlayer();
                        if (player.isOnline()) player.getPlayer().sendMessage(getPrefix() + ChatColor.RED + "Your party was automatically removed due to having too few members, please re-create it if you still need it.");
                    }
                    for (Character character : party.getInvitees()) {
                        OfflinePlayer player = character.getPlayer();
                        if (player.isOnline()) player.getPlayer().sendMessage(getPrefix() + ChatColor.RED + "Your party invite expired.");
                    }
                    removeParty(party);
                }
            }
        }, 6000L, 6000L);
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

    public boolean isThirstDisabled(OfflinePlayer player) {
        File thirstDisabledFile = new File(getDataFolder(), "thirst-disabled.yml");
        YamlConfiguration thirstDisabledConfig = YamlConfiguration.loadConfiguration(thirstDisabledFile);
        return thirstDisabledConfig.getStringList("disabled").contains(player.getName());
    }

    public void setThirstDisabled(OfflinePlayer player, boolean disabled) {
        File thirstDisabledFile = new File(getDataFolder(), "thirst-disabled.yml");
        YamlConfiguration thirstDisabledConfig = YamlConfiguration.loadConfiguration(thirstDisabledFile);
        List<String> thirstDisabled = thirstDisabledConfig.getStringList("disabled");
        if (disabled) thirstDisabled.add(player.getName()); else thirstDisabled.remove(player.getName());
        thirstDisabledConfig.set("disabled", thirstDisabled);
        try {
            thirstDisabledConfig.save(thirstDisabledFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    
    private int checkBiome(Biome biome) {
        switch (biome) {
            case DESERT:
            case DESERT_HILLS:
            case DESERT_MOUNTAINS:
                return 8;
            case HELL:
                return 16;
            case JUNGLE:
            case JUNGLE_EDGE:
            case JUNGLE_EDGE_MOUNTAINS:
            case JUNGLE_HILLS:
            case JUNGLE_MOUNTAINS:
                return 6;
            case MESA:
            case MESA_BRYCE:
            case MESA_PLATEAU:
            case MESA_PLATEAU_FOREST:
            case MESA_PLATEAU_FOREST_MOUNTAINS:
            case MESA_PLATEAU_MOUNTAINS:
                return 8;
            case SAVANNA:
            case SAVANNA_MOUNTAINS:
            case SAVANNA_PLATEAU:
            case SAVANNA_PLATEAU_MOUNTAINS:
                return 6;
            default:
                return 4;
        }
    }

    public Biome convertBiomeFromString(String biomeString) {
        try {
            return Biome.valueOf(biomeString.toUpperCase().replace(' ', '_'));
        } catch (IllegalArgumentException | NullPointerException exception) {
            return null;
        }
    }
    
    public boolean isSafeWater(Biome biome) {
        switch (biome) {
            case BEACH:
            case COLD_BEACH:
            case FROZEN_OCEAN:
            case OCEAN:
            case SWAMPLAND:
            case SWAMPLAND_MOUNTAINS:
                return false;
            default:
                return true;
        }

    }

    public boolean isHungerDisabled(OfflinePlayer player) {
        File hungerDisabledFile = new File(getDataFolder(), "hunger-disabled.yml");
        YamlConfiguration hungerDisabledConfig = YamlConfiguration.loadConfiguration(hungerDisabledFile);
        return hungerDisabledConfig.getStringList("disabled").contains(player.getUniqueId().toString());
    }

    public void setHungerDisabled(OfflinePlayer player, boolean disabled) {
        File hungerDisabledFile = new File(getDataFolder(), "hunger-disabled.yml");
        YamlConfiguration hungerDisabledConfig = YamlConfiguration.loadConfiguration(hungerDisabledFile);
        List<String> hungerDisabled = hungerDisabledConfig.getStringList("disabled");
        if (disabled) hungerDisabled.add(player.getUniqueId().toString()); else hungerDisabled.remove(player.getUniqueId().toString());
        hungerDisabledConfig.set("disabled", hungerDisabled);
        try {
            hungerDisabledConfig.save(hungerDisabledFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public boolean isHealthFrozen(OfflinePlayer player) {
        File healthFrozenFile = new File(getDataFolder(), "health-frozen.yml");
        YamlConfiguration healthFrozenConfig = YamlConfiguration.loadConfiguration(healthFrozenFile);
        return healthFrozenConfig.getStringList("frozen").contains(player.getUniqueId().toString());
    }

    public void setHealthFrozen(OfflinePlayer player, boolean frozen) {
        File healthFrozenFile = new File(getDataFolder(), "health-frozen.yml");
        YamlConfiguration healthFrozenConfig = YamlConfiguration.loadConfiguration(healthFrozenFile);
        List<String> hungerDisabled = healthFrozenConfig.getStringList("frozen");
        if (frozen) hungerDisabled.add(player.getUniqueId().toString()); else hungerDisabled.remove(player.getUniqueId().toString());
        healthFrozenConfig.set("frozen", hungerDisabled);
        try {
            healthFrozenConfig.save(healthFrozenFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
