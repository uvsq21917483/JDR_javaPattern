package fr.uvsq.hal.pglp.rpgdao;

import java.io.IOException;
import java.sql.Connection;
import fr.uvsq.hal.pglp.rpg.Character;

/**
 * La classe JdbcDaoFactory permet de créer des DAO JDBC
 */
public class JdbcDaoFactory extends DaoAbstractFactory {
    private final Connection connection;

    public JdbcDaoFactory(Connection connection) throws IOException {
        this.connection = connection;
    }

    @Override
    public Dao<Character> getCharacterDao() {
        return new CharacterJdbcDao(connection);
    }
}
