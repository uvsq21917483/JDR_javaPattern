package fr.uvsq.hal.pglp.rpgdao;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import fr.uvsq.hal.pglp.rpg.Character;

import static fr.uvsq.hal.pglp.rpg.Ability.*;
import static fr.uvsq.hal.pglp.rpg.Skill.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Optional;

public class CharacterJdbcDaoTest {

  private static final String DB_URL = "jdbc:derby:memory:testdb;create=true";
  private static Connection connection;

  private Character charWithoutSkills;
  private Character charWithSkills;

  @BeforeClass
  public static void beforeAll() throws SQLException {
    connection = DriverManager.getConnection(DB_URL);
    Statement statement = connection.createStatement();
    // Création des tables en mémoire
    statement.execute(
        "CREATE TABLE characters(name VARCHAR(40) PRIMARY KEY,strength INT, dexterity INT, constitution INT, intelligence INT, wisdom INT, charisma INT,proficiencyBonus INT)");

    statement.execute(
        "CREATE TABLE skills(name VARCHAR(40) PRIMARY KEY, characterName VARCHAR(40) REFERENCES characters(name))");
  }

  @Before
  public void setup() throws SQLException {
    charWithoutSkills = new Character.Builder("Without",
        Arrays.asList(Wisdom, Intelligence, Charisma, Dexterity, Strength, Constitution))
        .build();
    charWithSkills = new Character.Builder("With",
        Arrays.asList(Wisdom, Intelligence, Charisma, Dexterity, Strength, Constitution))
        .setSkills(Acrobatics, Medicine)
        .build();
    Statement statement = connection.createStatement();
    statement.execute("DELETE FROM skills");
    statement.execute("DELETE FROM characters");
  }

  @Test
  public void createTestWithoutSkills() {
    Dao<Character> characterDAO = new CharacterJdbcDao(connection);
    assertTrue(characterDAO.create(charWithoutSkills));
    assertEquals(Optional.of(charWithoutSkills), characterDAO.read("Without"));
  }

  @Test
  public void createTestWithSkills() {
    Dao<Character> characterDAO = new CharacterJdbcDao(connection);
    assertTrue(characterDAO.create(charWithSkills));
    assertEquals(Optional.of(charWithSkills), characterDAO.read("With"));
  }

  @Test
  public void updateTest() {
    Character elyn2 = new Character.Builder("With",
        Arrays.asList(Wisdom, Intelligence, Charisma, Dexterity, Strength, Constitution))
        .build();
    Dao<Character> characterDAO = new CharacterJdbcDao(connection);
    assertTrue(characterDAO.create(charWithSkills));
    assertTrue(characterDAO.update(elyn2));
    assertEquals(Optional.of(elyn2), characterDAO.read("With"));
  }

  @Test
  public void shouldReturnOptEmpty() {
    Dao<Character> characterDAO = new CharacterJdbcDao(connection);
    assertEquals(Optional.empty(), characterDAO.read("empty"));
  }

  @Test
  public void deleteTest() {
    Dao<Character> characterDAO = new CharacterJdbcDao(connection);
    assertTrue(characterDAO.create(charWithSkills));
    characterDAO.delete(charWithSkills);
    assertTrue(characterDAO.read("With").isEmpty());
  }
}