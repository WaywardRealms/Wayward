package net.wayward_realms.waywardunconsciousness;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.death.DeathPlugin;
import net.wayward_realms.waywardlib.util.serialisation.SerialisableLocation;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;

public class WaywardUnconsciousness extends JavaPlugin implements DeathPlugin {

    private File deathLocationsFile;
    private YamlConfiguration deathLocations;
    private File deathTimesFile;
    private YamlConfiguration deathTimes;

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
        for (World world : getServer().getWorlds()) {
            world.setGameRuleValue("keepInventory", "true");
        }
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
        try {
            deathLocations.load(deathLocationsFile);
            deathTimes.load(deathTimesFile);
        } catch (IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void saveState() {

    }

    @Override
    public void wake(OfflinePlayer player) {
        setUnconscious(player, false);
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
