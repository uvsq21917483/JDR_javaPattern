package fr.uvsq.hal.pglp.rpgdao;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import fr.uvsq.hal.pglp.rpg.Character;
import fr.uvsq.hal.pglp.rpgdao.DaoAbstractFactory.DaoType;

public class JdbcDaoFactoryTest {
    private static final String DB_URL = "jdbc:derby:memory:testdb;create=true";
    private static Connection connection;
    InputStream inputStream;
    DaoType daoType;

    @BeforeClass
    public static void beforeAll() throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
    }

    @Test
    public void shouldCreateAJDBCCharacterDao() throws IOException {
        Dao<Character> characterDao = new JdbcDaoFactory(connection).getCharacterDao();
        assertTrue(characterDao instanceof CharacterJdbcDao);
    }

    @Test
    public void shouldChooseJDBCFactory() throws SQLException, IOException {
        // JDBC est sélectionné dans le fichier config
        DaoAbstractFactory daoFactory = DaoAbstractFactory.getDaoFactory("resources/config.properties");
        Dao<Character> characterDao = daoFactory.getCharacterDao();
        assertTrue(characterDao instanceof CharacterJdbcDao);
    }

    @Test(expected = NullPointerException.class)
    public void aNullDaoTypeShouldGenerateNpe() throws SQLException, IOException {
        DaoAbstractFactory.getDaoFactory("resources/empty.properties");
    }
}
