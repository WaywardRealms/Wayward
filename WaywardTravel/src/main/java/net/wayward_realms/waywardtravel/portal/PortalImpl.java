package net.wayward_realms.waywardtravel.portal;

import net.wayward_realms.waywardlib.travel.Portal;
import net.wayward_realms.waywardlib.util.serialisation.SerialisableLocation;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class PortalImpl implements Portal {

    private Location maxLocation;
    private Location minLocation;
    private Location teleportLocation;

    private PortalImpl() {}

    public PortalImpl(Location minLocation, Location maxLocation, Location teleportLocation) {
        this.minLocation = minLocation;
        this.maxLocation = maxLocation;
        this.teleportLocation = teleportLocation;
    }

    @Override
    public Location getMaxLocation() {
        return maxLocation;
    }

    @Override
    public Location getMinLocation() {
        return minLocation;
    }

    @Override
    public Location getTeleportLocation() {
        return teleportLocation;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("min-location", new SerialisableLocation(minLocation));
        serialised.put("max-location", new SerialisableLocation(maxLocation));
        serialised.put("teleport-location", new SerialisableLocation(teleportLocation));
        return serialised;
    }

    public static PortalImpl deserialize(Map<String, Object> serialised) {
        PortalImpl deserialised = new PortalImpl();
        deserialised.minLocation = ((SerialisableLocation) serialised.get("min-location")).toLocation();
        deserialised.maxLocation = ((SerialisableLocation) serialised.get("max-location")).toLocation();
        deserialised.teleportLocation = ((SerialisableLocation) serialised.get("teleport-location")).toLocation();
        return deserialised;
    }

    @Override
    public void setTeleportLocation(Location location) {
        this.teleportLocation = location;
    }

    @Override
    public void setMinLocation(Location location) {
        this.minLocation = location;
    }

    @Override
    public void setMaxLocation(Location location) {
        this.maxLocation = location;
    }

}
