package net.wayward_realms.waywardtravel;

import net.wayward_realms.waywardlib.travel.Portal;
import net.wayward_realms.waywardlib.travel.TravelPlugin;
import net.wayward_realms.waywardtravel.boat.BoatPlayerInteractListener;
import net.wayward_realms.waywardtravel.boat.BoatSignChangeListener;
import net.wayward_realms.waywardtravel.horse.HorsePlayerInteractEntityListener;
import net.wayward_realms.waywardtravel.portal.PlayerMoveListener;
import net.wayward_realms.waywardtravel.portal.PortalCommand;
import net.wayward_realms.waywardtravel.portal.PortalImpl;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class WaywardTravel extends JavaPlugin implements TravelPlugin {

    private Set<Portal> portals = new HashSet<>();

    private Set<String> untaming = new HashSet<>();

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(PortalImpl.class);
        registerListeners(new PlayerMoveListener(this), new HorsePlayerInteractEntityListener(this), new BoatPlayerInteractListener(this), new BoatSignChangeListener(this));
        getCommand("portal").setExecutor(new PortalCommand(this));
        getCommand("untame").setExecutor(new UntameCommand(this));
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
        //return "" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "|" + ChatColor.RESET + ChatColor.BLUE + "WaywardTravel" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "| " + ChatColor.RESET;
        return "";
    }

    @Override
    public void loadState() {
        File portalFile = new File(getDataFolder(), "portals.yml");
        if (portalFile.exists()) {
            YamlConfiguration portalConfig = new YamlConfiguration();
            try {
                portalConfig.load(portalFile);
            } catch (IOException | InvalidConfigurationException exception) {
                exception.printStackTrace();
            }
            if (portalConfig.getConfigurationSection("portals") != null) {
                for (String portalCode : portalConfig.getConfigurationSection("portals").getKeys(false)) {
                    portals.add((Portal) portalConfig.get("portals." + portalCode));
                }
            }
        }
    }

    @Override
    public void saveState() {
        File portalFile = new File(getDataFolder(), "portals.yml");
        if (portalFile.exists()) {
            portalFile.delete();
        }
        YamlConfiguration portalConfig = new YamlConfiguration();
        for (Portal portal : portals) {
            portalConfig.set("portals." + portal.hashCode(), portal);
        }
        try {
            portalConfig.save(portalFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Set<Portal> getPortals() {
        return portals;
    }

    @Override
    public void removePortal(Portal portal) {
        portals.remove(portal);
    }

    @Override
    public void addPortal(Portal portal) {
        portals.add(portal);
    }

    public boolean isUntaming(Player player) {
        return untaming.contains(player.getName());
    }

    public void setUntaming(Player player, boolean untaming) {
        if (untaming) {
            this.untaming.add(player.getName());
        } else {
            this.untaming.remove(player.getName());
        }
    }

}
