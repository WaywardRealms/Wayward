package net.wayward_realms.waywardtravel;

import net.wayward_realms.waywardlib.travel.Portal;
import net.wayward_realms.waywardlib.travel.TravelPlugin;
import net.wayward_realms.waywardlib.util.file.filter.YamlFileFilter;
import net.wayward_realms.waywardlib.util.location.LocationUtils;
import net.wayward_realms.waywardtravel.boat.BoatPlayerInteractListener;
import net.wayward_realms.waywardtravel.boat.BoatSignChangeListener;
import net.wayward_realms.waywardtravel.horse.HorsePlayerInteractEntityListener;
import net.wayward_realms.waywardtravel.portal.PlayerMoveListener;
import net.wayward_realms.waywardtravel.portal.PortalCommand;
import net.wayward_realms.waywardtravel.portal.PortalImpl;
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

    }

    @Override
    public void saveState() {
    }

    @Override
    public Set<Portal> getPortals() {
        Set<Portal> portals = new HashSet<>();
        File portalsDirectory = new File(getDataFolder(), "portals");
        if (portalsDirectory.exists()) {
            for (File file : portalsDirectory.listFiles(new YamlFileFilter())) {
                YamlConfiguration portalConfig = YamlConfiguration.loadConfiguration(file);
                portals.add((Portal) portalConfig.get("portal"));
            }
        }
        return portals;
    }

    @Override
    public void removePortal(Portal portal) {
        File portalDirectory = new File(getDataFolder(), "portals");
        File portalFile = new File(portalDirectory, "l1" + LocationUtils.toString(portal.getMinLocation()) + "l2" + LocationUtils.toString(portal.getMaxLocation()));
        if (portalFile.exists()) {
            portalFile.delete();
        }
    }

    @Override
    public void addPortal(Portal portal) {
        File portalDirectory = new File(getDataFolder(), "portals");
        File portalFile = new File(portalDirectory, "l1" + LocationUtils.toString(portal.getMinLocation()) + "l2" + LocationUtils.toString(portal.getMaxLocation()));
        YamlConfiguration portalSave = new YamlConfiguration();
        portalSave.set("portal", portal);
        try {
            portalSave.save(portalFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
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
