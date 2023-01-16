package fr.uvsq.hal.pglp.rpgdao;

import java.sql.*;
import java.util.Arrays;
import java.util.Optional;
// import fr.uvsq.hal.pglp.rpg.*;
import static fr.uvsq.hal.pglp.rpg.Ability.*;
import fr.uvsq.hal.pglp.rpg.Character;
import fr.uvsq.hal.pglp.rpg.Skill;

/**
 * La classe <code>CharacterJdbcDao</code> est un DAO pour les personnages.
 *
 * @author hal
 * @version 2022
 */
public class CharacterJdbcDao implements Dao<Character> {

    private Connection connection;

    public CharacterJdbcDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean create(Character objet) {
        try {
            PreparedStatement psInsert = connection
                    .prepareStatement("INSERT INTO characters VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
            psInsert.setString(1, objet.getName());
            psInsert.setInt(2, objet.getScore(Strength));
            psInsert.setInt(3, objet.getScore(Dexterity));
            psInsert.setInt(4, objet.getScore(Constitution));
            psInsert.setInt(5, objet.getScore(Intelligence));
            psInsert.setInt(6, objet.getScore(Wisdom));
            psInsert.setInt(7, objet.getScore(Charisma));
            psInsert.setInt(8, objet.getProficiencyBonus());
            psInsert.executeUpdate();

            psInsert = connection.prepareStatement("INSERT INTO skills VALUES(?, ?)");
            for (Skill skill : objet.getSkills()) {
                psInsert.setString(1, skill.name());
                psInsert.setString(2, objet.getName());
                psInsert.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Optional<Character> read(String identifier) {
        Character character = null;
        try {
            PreparedStatement psInsert = connection.prepareStatement("SELECT * FROM characters WHERE name = ?");
            psInsert.setString(1, identifier);
            ResultSet rs = psInsert.executeQuery();

            if (rs.next()) {
                character = new Character.Builder(rs.getString(1),
                        Arrays.asList(Wisdom, Intelligence, Charisma, Dexterity, Strength, Constitution))
                        .setAbilityScore(Strength, rs.getInt(2))
                        .setAbilityScore(Dexterity, rs.getInt(3))
                        .setAbilityScore(Constitution, rs.getInt(4))
                        .setAbilityScore(Intelligence, rs.getInt(5))
                        .setAbilityScore(Wisdom, rs.getInt(6))
                        .setAbilityScore(Charisma, rs.getInt(7))
                        .setProficiencyBonus(rs.getInt(8))
                        .build();

                psInsert = connection.prepareStatement("SELECT * FROM skills WHERE characterName = ?");
                psInsert.setString(1, identifier);

                rs = psInsert.executeQuery();
                while (rs.next()) {
                    character.addSkill(Skill.valueOf(rs.getString(1)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.ofNullable(character);
    }

    @Override
    public boolean update(Character objet) {
        try {
            PreparedStatement ps = connection
                    .prepareStatement("UPDATE characters SET " +
                            "strength = ?," +
                            "dexterity = ?," +
                            "constitution = ?," +
                            "intelligence  = ?," +
                            "wisdom = ?," +
                            "charisma = ?," +
                            "proficiencyBonus = ?" +
                            "WHERE name = ?");
            ps.setInt(1, objet.getScore(Strength));
            ps.setInt(2, objet.getScore(Dexterity));
            ps.setInt(3, objet.getScore(Constitution));
            ps.setInt(4, objet.getScore(Intelligence));
            ps.setInt(5, objet.getScore(Wisdom));
            ps.setInt(6, objet.getScore(Charisma));
            ps.setInt(7, objet.getProficiencyBonus());
            ps.setString(8, objet.getName());
            ps.executeUpdate();

            ps = connection.prepareStatement("DELETE FROM skills WHERE characterName = ?");
            ps.setString(1, objet.getName());
            ps.executeUpdate();

            ps = connection.prepareStatement("INSERT INTO skills VALUES(?, ?)");
            for (Skill skill : objet.getSkills()) {
                ps.setString(1, skill.name());
                ps.setString(2, objet.getName());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void delete(Character character) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM skills WHERE characterName = ?");
            ps.setString(1, character.getName());
            ps.executeUpdate();
            ps = connection.prepareStatement("DELETE FROM characters WHERE name = ?");
            ps.setString(1, character.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
