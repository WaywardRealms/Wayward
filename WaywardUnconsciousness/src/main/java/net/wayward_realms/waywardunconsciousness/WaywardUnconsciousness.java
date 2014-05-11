package net.wayward_realms.waywardunconsciousness;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.death.DeathPlugin;
import net.wayward_realms.waywardlib.util.serialisation.SerialisableLocation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class WaywardUnconsciousness extends JavaPlugin implements DeathPlugin {

    private File deathLocationsFile;
    private YamlConfiguration deathLocations;
    private File deathTimesFile;
    private YamlConfiguration deathTimes;
    private File deathInventoriesFile;
    private YamlConfiguration deathInventories;
    private File deathCausesFile;
    private YamlConfiguration deathCauses;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        registerListeners(new PlayerDeathListener(this),
                new PlayerRespawnListener(this),
                new PlayerMoveListener(this),
                new PlayerInteractEntityListener(this),
                new EntityDamageListener(this),
                new EntityTargetListener(this),
                new PlayerJoinListener(this),
                new PlayerInteractListener(this),
                new EntityDamageByEntityListener(this));
        getCommand("wake").setExecutor(new WakeCommand(this));
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (Player player : getServer().getOnlinePlayers()) {
                    if (!isUnconscious(player)) player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 0, 0), true);
                }
            }
        }, 0L, 200L);
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            this.getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    @Override
    public String getPrefix() {
        //return "" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "|" + ChatColor.RESET + ChatColor.BLUE + "WaywardUnconsciousness" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "| " + ChatColor.RESET;
        return "";
    }

    @Override
    public void loadState() {
        this.deathLocationsFile = new File(getDataFolder(), "death-locations.yml");
        this.deathLocations = new YamlConfiguration();
        this.deathTimesFile = new File(getDataFolder(), "death-times.yml");
        this.deathTimes = new YamlConfiguration();
        this.deathInventoriesFile = new File(getDataFolder(), "death-inventories.yml");
        this.deathInventories = new YamlConfiguration();
        this.deathCausesFile = new File(getDataFolder(), "death-causes.yml");
        this.deathCauses = new YamlConfiguration();
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        if (!deathLocationsFile.exists()) {
            try {
                deathLocationsFile.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        if (!deathTimesFile.exists()) {
            try {
                deathTimesFile.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        if (!deathInventoriesFile.exists()) {
            try {
                deathInventoriesFile.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        if (!deathCausesFile.exists()) {
            try {
                deathCausesFile.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        try {
            deathLocations.load(deathLocationsFile);
            deathTimes.load(deathTimesFile);
            deathInventories.load(deathInventoriesFile);
            deathCauses.load(deathCausesFile);
        } catch (IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void saveState() {

    }

    @Override
    public ItemStack[] getDeathInventory(OfflinePlayer player) {
        RegisteredServiceProvider<CharacterPlugin> characterProvider = getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterProvider != null) {
            return getDeathInventory(characterProvider.getProvider().getActiveCharacter(player));
        }
        return null;
    }

    public void setDeathInventory(OfflinePlayer player, List<ItemStack> inventoryContents) {
        RegisteredServiceProvider<CharacterPlugin> characterProvider = getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterProvider != null) {
            setDeathInventory(characterProvider.getProvider().getActiveCharacter(player), inventoryContents);
        }
    }

    public void setDeathInventory(Character character, List<ItemStack> inventoryContents) {
        deathInventories.set("" + character.getId(), inventoryContents);
        try {
            deathInventories.save(deathInventoriesFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void removeDeathInventory(OfflinePlayer player) {
        RegisteredServiceProvider<CharacterPlugin> characterProvider = getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterProvider != null) {
            removeDeathInventory(characterProvider.getProvider().getActiveCharacter(player));
        }
    }

    public void removeDeathInventory(Character character) {
        deathInventories.set("" + character.getId(), null);
        try {
            deathInventories.save(deathInventoriesFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void setDeathCause(OfflinePlayer player, EntityDamageEvent.DamageCause cause) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            setDeathCause(characterPlugin.getActiveCharacter(player), cause);
        }
    }

    public void setDeathCause(Character character, EntityDamageEvent.DamageCause cause) {
        deathCauses.set("" + character.getId(), cause.toString());
        try {
            deathCauses.save(deathCausesFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public EntityDamageEvent.DamageCause getDeathCause(OfflinePlayer player) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            return getDeathCause(characterPlugin.getActiveCharacter(player));
        }
        return null;
    }

    public EntityDamageEvent.DamageCause getDeathCause(Character character) {
        return EntityDamageEvent.DamageCause.valueOf(deathCauses.getString("" + character.getId()));
    }

    public void removeDeathCause(OfflinePlayer player) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            removeDeathCause(characterPlugin.getActiveCharacter(player));
        }
    }

    public void removeDeathCause(Character character) {
        deathCauses.set("" + character.getId(), null);
        try {
            deathCauses.save(deathCausesFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public boolean isDeath(EntityDamageEvent.DamageCause damageCause) {
        return damageCause == EntityDamageEvent.DamageCause.LAVA ||
                damageCause == EntityDamageEvent.DamageCause.FIRE ||
                damageCause == EntityDamageEvent.DamageCause.FIRE_TICK ||
                damageCause == EntityDamageEvent.DamageCause.DROWNING;
    }

    @Override
    public void wake(OfflinePlayer player) {
        setUnconscious(player, false);
    }

    @Override
    public ItemStack[] getDeathInventory(Character character) {
        List<?> var = deathInventories.getList("" + character.getId());
        return var.toArray(new ItemStack[var.size()]);
    }

    @Override
    public void wake(Character character) {
        setUnconscious(character, false);
    }

    public void setUnconscious(final OfflinePlayer player, boolean unconscious) {
        RegisteredServiceProvider<CharacterPlugin> characterProvider = getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterProvider != null) {
            setUnconscious(characterProvider.getProvider().getActiveCharacter(player), unconscious);
        }
    }

    public void setUnconscious(final Character character, boolean unconscious) {
        if (unconscious) {
            if (character.getPlayer().isOnline()) {
                character.getPlayer().getPlayer().sendMessage(getPrefix() + ChatColor.RED + "Your injuries have caused you to fall unconscious.");
                character.getPlayer().getPlayer().sendMessage(getPrefix() + ChatColor.RED + "You will wake after a short period of time has elapsed, or someone assists you.");
            }
            setDeathTime(character);
            getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    WaywardUnconsciousness.this.setUnconscious(character, false);
                }
            }, getConfig().getInt("unconscious-time") * 1200);
        } else {
            removeDeathLocation(character);
            removeDeathTime(character);
            if (character.getPlayer().isOnline()) {
                RegisteredServiceProvider<CharacterPlugin> characterProvider = getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                if (characterProvider != null) {
                    CharacterPlugin characterPlugin = characterProvider.getProvider();
                    if (characterPlugin.getActiveCharacter(character.getPlayer()) == character) {
                        character.getPlayer().getPlayer().sendMessage(getPrefix() + ChatColor.GREEN + "You have regained consciousness.");
                        character.getPlayer().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 0, 0), true);
                    }
                }
            }
        }
    }

    @Override
    public boolean isUnconscious(OfflinePlayer player) {
        RegisteredServiceProvider<CharacterPlugin> characterProvider = getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        return characterProvider != null && isUnconscious(characterProvider.getProvider().getActiveCharacter(player));
    }

    @Override
    public boolean isUnconscious(Character character) {
        return deathTimes.contains("" + character.getId());
    }

    public Location getDeathLocation(OfflinePlayer player) {
        RegisteredServiceProvider<CharacterPlugin> characterProvider = getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterProvider != null) {
            return getDeathLocation(characterProvider.getProvider().getActiveCharacter(player));
        }
        return null;
    }

    public Location getDeathLocation(Character character) {
        return ((SerialisableLocation) deathLocations.get("" + character.getId())).toLocation();
    }

    public void setDeathLocation(OfflinePlayer player, Location location) {
        RegisteredServiceProvider<CharacterPlugin> characterProvider = getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterProvider != null) {
            setDeathLocation(characterProvider.getProvider().getActiveCharacter(player), location);
        }
    }

    public void setDeathLocation(Character character, Location location) {
        deathLocations.set("" + character.getId(), new SerialisableLocation(location));
        try {
            deathLocations.save(deathLocationsFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void removeDeathLocation(OfflinePlayer player) {
        RegisteredServiceProvider<CharacterPlugin> characterProvider = getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterProvider != null) {
            CharacterPlugin characterPlugin = characterProvider.getProvider();
            removeDeathLocation(characterPlugin.getActiveCharacter(player));
        }
    }

    private void removeDeathLocation(Character character) {
        deathLocations.set("" + character.getId(), null);
        try {
            deathLocations.save(deathLocationsFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public long getDeathTime(OfflinePlayer player) {
        RegisteredServiceProvider<CharacterPlugin> characterProvider = getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterProvider != null) {
            return getDeathTime(characterProvider.getProvider().getActiveCharacter(player));
        }
        return 0L;
    }

    public long getDeathTime(Character character) {
        return deathTimes.getLong("" + character.getId());
    }

    public void setDeathTime(OfflinePlayer player) {
        RegisteredServiceProvider<CharacterPlugin> characterProvider = getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterProvider != null) {
            CharacterPlugin characterPlugin = characterProvider.getProvider();
            setDeathTime(characterPlugin.getActiveCharacter(player));
        }
    }

    public void setDeathTime(Character character) {
        deathTimes.set("" + character.getId(), System.currentTimeMillis());
        try {
            deathTimes.save(deathTimesFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void removeDeathTime(OfflinePlayer player) {
        RegisteredServiceProvider<CharacterPlugin> characterProvider = getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterProvider != null) {
            CharacterPlugin characterPlugin = characterProvider.getProvider();
            removeDeathTime(characterPlugin.getActiveCharacter(player));
        }
    }

    private void removeDeathTime(Character character) {
        deathTimes.set("" + character.getId(), null);
        try {
            deathTimes.save(deathTimesFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
