package net.wayward_realms.waywardlib.util.database.table;

import net.wayward_realms.waywardlib.util.database.Database;
import net.wayward_realms.waywardlib.util.database.Table;
import net.wayward_realms.waywardlib.util.database.table.row.LoreRow;

import java.sql.SQLException;

public class LoreTable extends Table<LoreRow> {

    public LoreTable(Database database, String name) throws SQLException {
        super(database, name, LoreRow.class);
    }

    public LoreTable(Database database) throws SQLException {
        super(database, LoreRow.class);
    }

    @Override
    public void create() throws SQLException {

    }

    @Override
    public int insert(LoreRow object) throws SQLException {
        return 0;
    }

    @Override
    public int update(LoreRow object) throws SQLException {
        return 0;
    }

    @Override
    public LoreRow get(int id) throws SQLException {
        return null;
    }

}
