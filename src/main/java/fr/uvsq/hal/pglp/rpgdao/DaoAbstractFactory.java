package fr.uvsq.hal.pglp.rpgdao;

import fr.uvsq.hal.pglp.rpg.Character;

import java.io.FileInputStream;

import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

/**
 * La classe <code>DaoAbstractFactory</code> permet de créer des fabriques pour
 * un type de DAO.
 *
 * @author hal
 * @version 2022
 */
public abstract class DaoAbstractFactory {
    public enum DaoType {
        JDBC;
    }

    public abstract Dao<Character> getCharacterDao();

    public static DaoAbstractFactory getDaoFactory(String propFileName) throws IOException, SQLException {
        DaoType daoType = null;
        Properties prop = new Properties();

        try (FileInputStream fis = new FileInputStream(propFileName)) {
            prop.load(fis);
            String daoTypeRead = prop.getProperty("DaoFactory");
            daoType = DaoType.valueOf(daoTypeRead);
            fis.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        if (daoType == DaoType.JDBC) {
            String DB_URL = "jdbc:derby:memory:testdb;create=true"; // prop.getProperty("DB_URL");
            Connection connection = DriverManager.getConnection(DB_URL);
            return new JdbcDaoFactory(connection);
        }
        // ici les autres types de Dao supportés

        Objects.requireNonNull(daoType);

        return null;
    }
}
