package net.wayward_realms.waywardmoderation.util;

import net.wayward_realms.waywardlib.util.serialisation.SerialisableLocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility Class to store persistent fields Information.
 * @author Mohnraj, Sudhir
 */
public class DataStorage implements ConfigurationSerializable {

    private Material material;
    private Date date;
    private SerialisableLocation serialisableLocation;

    /**
     * @return the material.
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * @return the location.
     */
    public Location getLocation() {
        return serialisableLocation.toLocation();
    }

    /**
     * @param location the location to set.
     */
    public void setLocation(final Location location) {
        this.serialisableLocation = new SerialisableLocation(location);
    }

    /**
     * @param material the material to set.
     */
    public void setMaterial(final Material material) {
        this.material = material;
    }

    /**
     * @return the date.
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set.
     */
    public void setDate(final Date date) {
        this.date = date;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> serialize() {
        final Map<String, Object> serialised = new HashMap<>();
        serialised.put("location", serialisableLocation);
        serialised.put("material", material.toString());
        serialised.put("date", date);
        return serialised;
    }

    /**
     * Creates a class representing the serialized map object.
     * @param serialised a map of serialized object
     * @return the class {@link DataStorage}
     */
    public static DataStorage deserialize(final Map<String, Object> serialised) {
        final DataStorage deserialised = new DataStorage();
        deserialised.material = Material.getMaterial((String) serialised.get("material"));
        deserialised.serialisableLocation = (SerialisableLocation) serialised.get("location");
        deserialised.date = (Date) serialised.get("date");
        return deserialised;
    }

}

