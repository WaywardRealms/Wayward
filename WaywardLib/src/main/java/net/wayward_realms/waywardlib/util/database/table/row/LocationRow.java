package net.wayward_realms.waywardlib.util.database.table.row;

import net.wayward_realms.waywardlib.util.database.TableRowImpl;
import org.bukkit.Location;

public class LocationRow extends TableRowImpl {

    private Location location;

    public LocationRow(int id) {
        super(id);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
