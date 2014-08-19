package net.wayward_realms.waywardevents;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Race;
import net.wayward_realms.waywardlib.events.Dungeon;
import net.wayward_realms.waywardlib.events.EventCharacter;
import net.wayward_realms.waywardlib.events.EventCharacterTemplate;
import net.wayward_realms.waywardlib.events.EventsPlugin;
import net.wayward_realms.waywardlib.util.file.filter.YamlFileFilter;
import net.wayward_realms.waywardlib.util.location.LocationUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static net.wayward_realms.waywardlib.util.plugin.ListenerUtils.registerListeners;

public class WaywardEvents extends JavaPlugin implements EventsPlugin {

    private net.wayward_realms.waywardevents.DungeonManager dungeonManager = new net.wayward_realms.waywardevents.DungeonManager(this);

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(BlockDescriptor.class);
        ConfigurationSerialization.registerClass(DungeonImpl.class);
        ConfigurationSerialization.registerClass(RaceImpl.class);
        getCommand("dungeon").setExecutor(new DungeonCommand(this));
        getCommand("radiusemote").setExecutor(new RadiusEmoteCommand(this));
        getCommand("createeffect").setExecutor(new CreateEffectCommand(this));
        getCommand("eventcharacter").setExecutor(new EventCharacterCommand(this));
        getCommand("blockdescription").setExecutor(new BlockDescriptionCommand(this));
        registerListeners(this, new PlayerInteractListener(this));
    }

    @Override
    public String getPrefix() {
        //return "" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "|" + ChatColor.RESET + ChatColor.BLUE + "WaywardEvents" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "| " + ChatColor.RESET;
        return "";
    }

    @Override
    public void loadState() {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            File characterDirectory = new File(getDataFolder(), "event-characters");
            if (characterDirectory.exists()) {
                for (File file : characterDirectory.listFiles(new YamlFileFilter())) {
                    int id = Integer.parseInt(file.getName().replace(".yml", ""));
                    if (id > characterPlugin.getNextAvailableId()) characterPlugin.setNextAvailableId(id);
                }
            }
        }

    }

    @Override
    public void saveState() {

    }

    @Override
    public Collection<Dungeon> getDungeons() {
        return dungeonManager.getDungeons().values();
    }

    @Override
    public Dungeon getDungeon(String name) {
        return dungeonManager.getDungeon(name);
    }

    public net.wayward_realms.waywardevents.DungeonManager getDungeonManager() {
        return dungeonManager;
    }

    @Override
    public void removeDungeon(Dungeon dungeon) {
        for (Map.Entry<String, Dungeon> entry : dungeonManager.getDungeons().entrySet()) {
            if (entry.getValue() == dungeon) {
                dungeonManager.getDungeons().remove(entry.getKey());
            }
        }
    }

    @Override
    public void addDungeon(Dungeon dungeon) {
        dungeonManager.getDungeons().put(dungeon.toString(), dungeon);
    }

    @Override
    public EventCharacter getEventCharacter(int id) {
        File eventCharacterDirectory = new File(getDataFolder(), "event-characters");
        File eventCharacterFile = new File(eventCharacterDirectory, id + ".yml");
        if (eventCharacterFile.exists()) return new EventCharacterImpl(eventCharacterFile);
        return null;
    }

    @Override
    public EventCharacter createNewEventCharacter(OfflinePlayer player) {
        return new EventCharacterImpl(this, player);
    }

    @Override
    public EventCharacterTemplate getEventCharacterTemplate(String name) {
        File templateDirectory = new File(getDataFolder(), "event-character-templates");
        File templateFile = new File(templateDirectory, name + ".yml");
        if (templateFile.exists()) return new EventCharacterTemplateImpl(templateFile); else return null;
    }

    @Override
    public void addEventCharacterTemplate(EventCharacterTemplate template) {
        if (!(template instanceof EventCharacterTemplateImpl)) {
            new EventCharacterTemplateImpl(template);
        }
    }

    @Override
    public void removeEventCharacterTemplate(EventCharacterTemplate template) {
        File templateDirectory = new File(getDataFolder(), "event-character-templates");
        File templateFile = new File(templateDirectory, template.getName() + ".yml");
        if (templateFile.exists()) templateFile.delete();
    }

    public Collection<EventCharacterTemplate> getEventCharacterTemplates() {
        File templateDirectory = new File(getDataFolder(), "event-character-templates");
        Set<EventCharacterTemplate> templates = new HashSet<>();
        if (templateDirectory.exists()) {
            for (File file : templateDirectory.listFiles(new YamlFileFilter())) {
                templates.add(new EventCharacterTemplateImpl(file));
            }
        }
        return templates;
    }

    @Override
    public Race getRace(String name) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            if (characterPlugin.getRace(name) != null) return characterPlugin.getRace(name);
        }
        File raceDirectory = new File(getDataFolder(), "races");
        File raceFile = new File(raceDirectory, name.toLowerCase() + ".yml");
        if (raceFile.exists()) {
            return new RaceImpl(raceFile);
        }
        return null;
    }

    @Override
    public void addRace(Race race) {
        if (!(race instanceof RaceImpl)) {
            new RaceImpl(this, race);
        }
    }

    @Override
    public void removeRace(Race race) {
        File raceDirectory = new File(getDataFolder(), "races");
        File raceFile = new File(raceDirectory, race.getName().toLowerCase() + ".yml");
        if (raceFile.exists()) raceFile.delete();
    }

    public boolean hasBlockDescription(Block block) {
        File blockDescriptionDirectory = new File(getDataFolder(), "block-descriptors");
        File blockDescriptionFile = new File(blockDescriptionDirectory, LocationUtils.toString(block.getLocation()) + ".yml");
        return blockDescriptionFile.exists();
    }

    public BlockDescriptor getBlockDescription(Block block) {
        File blockDescriptionDirectory = new File(getDataFolder(), "block-descriptors");
        File blockDescriptionFile = new File(blockDescriptionDirectory, LocationUtils.toString(block.getLocation()) + ".yml");
        if (blockDescriptionFile.exists()) {
            YamlConfiguration blockDescriptorConfig = YamlConfiguration.loadConfiguration(blockDescriptionFile);
            return (BlockDescriptor) blockDescriptorConfig.get("description");
        } else {
            return null;
        }
    }

    public void setBlockDescription(Block block, BlockDescriptor descriptor) {
        File blockDescriptionDirectory = new File(getDataFolder(), "block-descriptors");
        File blockDescriptionFile = new File(blockDescriptionDirectory, LocationUtils.toString(block.getLocation()) + ".yml");
        YamlConfiguration blockDescriptorConfig = new YamlConfiguration();
        blockDescriptorConfig.set("description", descriptor);
        try {
            blockDescriptorConfig.save(blockDescriptionFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void removeBlockDescription(Block block) {
        File blockDescriptionDirectory = new File(getDataFolder(), "block-descriptors");
        File blockDescriptionFile = new File(blockDescriptionDirectory, LocationUtils.toString(block.getLocation()) + ".yml");
        if (blockDescriptionFile.exists()) {
            blockDescriptionFile.delete();
        }
    }

}
