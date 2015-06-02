package net.wayward_realms.waywardlib.util.database.table;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.util.database.Database;
import net.wayward_realms.waywardlib.util.database.Table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CharacterTable extends Table<Character> {

    public CharacterTable(Database database, String name) throws SQLException {
        super(database, name, Character.class);
    }

    public CharacterTable(Database database) throws SQLException {
        super(database, Character.class);
    }

    @Override
    public void create() throws SQLException {
        Connection connection = getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS character (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name VARCHAR(255)," +
                        "name_hidden BOOLEAN," +
                        "age INTEGER," +
                        "age_hidden BOOLEAN," +
                        "player_uuid VARCHAR(36)," +
                        "gender VARCHAR(255)," +
                        "gender_hidden BOOLEAN," +
                        "race VARCHAR(255)," +
                        "race_hidden BOOLEAN," +
                        "description VARCHAR(255)," +
                        "description_hidden BOOLEAN," +
                        "location INTEGER," +
                        "helmet INTEGER," +
                        "chestplate INTEGER," +
                        "leggings INTEGER," +
                        "boots INTEGER," +
                        "health DOUBLE," +
                        "food_level INTEGER," +
                        "thirst_level INTEGER," +
                        "mana INTEGER," +
                        "dead BOOLEAN," +
                        "class_hidden BOOLEAN," +
                        "FOREIGN KEY(location) REFERENCES location(id)," +
                        "FOREIGN KEY(helmet) REFERENCES itemstack(id)," +
                        "FOREIGN KEY(chestplate) REFERENCES itemstack(id)," +
                        "FOREIGN KEY(leggings) REFERENCES itemstack(id)," +
                        "FOREIGN KEY(boots) REFERENCES itemstack(id)" +
                ")"
        )) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public int insert(Character object) throws SQLException {
        return 0;
    }

    @Override
    public int update(Character object) throws SQLException {
        return 0;
    }

    @Override
    public Character get(int id) throws SQLException {
        return null;
    }

}
