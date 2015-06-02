package net.wayward_realms.waywardlib.util.database.table;

import net.wayward_realms.waywardlib.util.database.Database;
import net.wayward_realms.waywardlib.util.database.Table;
import net.wayward_realms.waywardlib.util.database.table.row.ItemStackRow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ItemStackTable extends Table<ItemStackRow> {

    public ItemStackTable(Database database, String name) throws SQLException {
        super(database, name, ItemStackRow.class);
    }

    public ItemStackTable(Database database) throws SQLException {
        super(database, ItemStackRow.class);
    }

    @Override
    public void create() throws SQLException {
        Connection connection = getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS itemstack (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "type VARCHAR(255)," +
                        "amount INTEGER," +
                        "data TINYINT," +
                        "durability SMALLINT," +
                        "display_name VARCHAR(255)" +
                ")"
        )) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public int insert(ItemStackRow object) throws SQLException {
        return 0;
    }

    @Override
    public int update(ItemStackRow object) throws SQLException {
        return 0;
    }

    @Override
    public ItemStackRow get(int id) throws SQLException {
        return null;
    }

}
