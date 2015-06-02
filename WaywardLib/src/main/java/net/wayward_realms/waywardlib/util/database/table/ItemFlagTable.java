package net.wayward_realms.waywardlib.util.database.table;

import net.wayward_realms.waywardlib.util.database.Database;
import net.wayward_realms.waywardlib.util.database.Table;
import net.wayward_realms.waywardlib.util.database.table.row.ItemFlagRow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ItemFlagTable extends Table<ItemFlagRow> {

    public ItemFlagTable(Database database, String name
    ) throws SQLException {
        super(database, name, ItemFlagRow.class);
    }

    public ItemFlagTable(Database database
    ) throws SQLException {
        super(database, ItemFlagRow.class);
    }

    @Override
    public void create() throws SQLException {
        Connection connection = getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS itemflag (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "itemstack INTEGER," +
                        "flag VARCHAR(255)," +
                        "FOREIGN KEY(itemstack) REFERENCES itemstack(id)" +
                ")"
        )) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public int insert(ItemFlagRow object) throws SQLException {
        return 0;
    }

    @Override
    public int update(ItemFlagRow object) throws SQLException {
        return 0;
    }

    @Override
    public ItemFlagRow get(int id) throws SQLException {
        return null;
    }
}
