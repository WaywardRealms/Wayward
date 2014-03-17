package net.wayward_realms.waywardevents;

import net.wayward_realms.waywardlib.events.Dungeon;
import net.wayward_realms.waywardlib.events.EventsPlugin;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.Map;

public class WaywardEvents extends JavaPlugin implements EventsPlugin {

    private net.wayward_realms.waywardevents.DungeonManager dungeonManager = new net.wayward_realms.waywardevents.DungeonManager();

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(net.wayward_realms.waywardevents.DungeonImpl.class);
        getCommand("dungeon").setExecutor(new net.wayward_realms.waywardevents.DungeonCommand(this));
        getCommand("radiusemote").setExecutor(new net.wayward_realms.waywardevents.RadiusEmoteCommand(this));
        getCommand("createeffect").setExecutor(new net.wayward_realms.waywardevents.CreateEffectCommand(this));
    }

    @Override
    public void onDisable() {
        dungeonManager.save(this);
    }

    @Override
    public String getPrefix() {
        //return "" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "|" + ChatColor.RESET + ChatColor.BLUE + "WaywardEvents" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "| " + ChatColor.RESET;
        return "";
    }

    @Override
    public void loadState() {
        dungeonManager.load(this);
    }

    @Override
    public void saveState() {
        dungeonManager.save(this);
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

}
