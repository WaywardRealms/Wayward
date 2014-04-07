package net.wayward_realms.waywardmoderation.util;

import net.wayward_realms.waywardlib.util.serialisation.SerialisableLocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility Class to store persistent fields Information.
 * @author Mohnraj, Sudhir
 */
public class BlockChange implements ConfigurationSerializable {

    private Material material;
    private byte data;
    private Date date;
    private Location location;

    private BlockChange() {}

    public BlockChange(Block block) {
        this.date = new Date();
        this.material = block.getType();
        this.data = block.getData();
        this.location = block.getLocation();
    }

    /**
     * @return the material.
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * @param material the material to set.
     */
    public void setMaterial(final Material material) {
        this.material = material;
    }

    public byte getData() {
        return data;
    }

    public void setData(byte data) {
        this.data = data;
    }

    /**
     * @return the location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @param location the location to set.
     */
    public void setLocation(final Location location) {
        this.location = location;
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
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("location", new SerialisableLocation(location));
        serialised.put("material", material.toString());
        serialised.put("date", date);
        return serialised;
    }

    /**
     * Creates a class representing the serialized map object.
     * @param serialised a map of serialized object
     * @return the class {@link BlockChange}
     */
    public static BlockChange deserialize(Map<String, Object> serialised) {
        BlockChange deserialised = new BlockChange();
        deserialised.material = Material.getMaterial((String) serialised.get("material"));
        deserialised.location = ((SerialisableLocation) serialised.get("location")).toLocation();
        deserialised.date = (Date) serialised.get("date");
        return deserialised;
    }

}

