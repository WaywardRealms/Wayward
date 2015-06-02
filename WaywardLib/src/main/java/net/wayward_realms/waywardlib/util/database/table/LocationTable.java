package net.wayward_realms.waywardlib.util.database.table;

import net.wayward_realms.waywardlib.util.database.Database;
import net.wayward_realms.waywardlib.util.database.Table;
import net.wayward_realms.waywardlib.util.database.table.row.LocationRow;

import java.sql.SQLException;

public class LocationTable extends Table<LocationRow> {

    public LocationTable(Database database, String name) throws SQLException {
        super(database, name, LocationRow.class);
    }

    public LocationTable(Database database) throws SQLException {
        super(database, LocationRow.class);
    }

    @Override
    public void create() throws SQLException {

    }

    @Override
    public int insert(LocationRow object) throws SQLException {
        return 0;
    }

    @Override
    public int update(LocationRow object) throws SQLException {
        return 0;
    }

    @Override
    public LocationRow get(int id) throws SQLException {
        return null;
    }

}
