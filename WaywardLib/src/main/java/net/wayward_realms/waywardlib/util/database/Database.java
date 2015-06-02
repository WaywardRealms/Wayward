package net.wayward_realms.waywardlib.util.database;

import net.wayward_realms.waywardlib.Wayward;
import net.wayward_realms.waywardlib.util.database.table.CharacterTable;
import net.wayward_realms.waywardlib.util.database.table.ItemStackTable;
import net.wayward_realms.waywardlib.util.database.table.LocationTable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;

public class Database {

    private Wayward plugin;
    private Connection connection;
    private Map<String, Table<?>> tables;

    public Database(Wayward plugin) throws SQLException {
        this.plugin = plugin;
        tables = new HashMap<>();
        connect();
        addTable(new ItemStackTable(this));
        addTable(new LocationTable(this));
        addTable(new CharacterTable(this));
    }

    public void connect() throws SQLException {
        if (connection != null) return;
        try {
            connectMySQL();
        } catch (SQLException exception) {
            connectSQLite();
        }
    }

    private void connectMySQL() throws SQLException {
        String url = "jdbc:mysql://" + plugin.getConfig().getString("database.url") + "/" + plugin.getConfig().getString("database.database");
        connection = DriverManager.getConnection(url, plugin.getConfig().getString("database.username"), plugin.getConfig().getString("database.password"));
    }

    private void connectSQLite() throws SQLException {
        String url = "jdbc:sqlite:" + plugin.getConfig().getString("database.database") + ".db";
        connection = DriverManager.getConnection(url);
    }

    public Connection getConnection() {
        return connection;
    }

    public void addTable(Table<?> table) {
        tables.put(table.getName(), table);
    }

    public Table<?> getTable(String name) {
        return tables.get(name);
    }

    public <T extends TableRow> Table<T> getTable(Class<T> type) throws SQLException {
        Table<?> table = tables.get(UPPER_CAMEL.to(LOWER_UNDERSCORE, type.getSimpleName()));
        if (table != null)
            return (Table<T>) table;
        else
            return null;
    }

}
