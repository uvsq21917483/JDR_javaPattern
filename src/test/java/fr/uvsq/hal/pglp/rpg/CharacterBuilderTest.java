package fr.uvsq.hal.pglp.rpg;

import org.junit.Test;

import static fr.uvsq.hal.pglp.rpg.Ability.*;
import static org.junit.Assert.*;
import java.util.Arrays;

public class CharacterBuilderTest {

  @Test
  public void aCharacterShouldBeInitializedWithRequiredFields() {
    Character character = new Character.Builder("Elyn",
        Arrays.asList(Wisdom, Intelligence, Charisma, Dexterity, Strength, Constitution)).build();
    assertEquals("Elyn", character.getName());
  }

  @Test
  public void aCharacterShouldHaveHisAbilityScoresWellInitialized() {
    Character character = new Character.Builder("Elyn",
        Arrays.asList(Wisdom, Intelligence, Charisma, Dexterity, Strength, Constitution)).build();
    assertEquals("Elyn", character.getName());

    assertTrue("First ability score shoud be greater or equal than the next one",
        character.getScore(Wisdom) >= character.getScore(Intelligence) &&
            character.getScore(Intelligence) >= character.getScore(Charisma) &&
            character.getScore(Charisma) >= character.getScore(Dexterity) &&
            character.getScore(Dexterity) >= character.getScore(Strength) &&
            character.getScore(Strength) >= character.getScore(Constitution));
    System.out.println(character);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowAnExceptionIfMismatchArguments() {
    new Character.Builder("Elyn", Arrays.asList(Wisdom)).build();
  }

  @Test
  public void aCharacterShouldBeInitializedWithNonRandomAbilities() {
    Character character = new Character.Builder("Elyn",
        Arrays.asList(Wisdom, Intelligence, Charisma, Dexterity, Strength, Constitution))
        .nonRandomAbilities()
        .build();
    assertEquals("Elyn", character.getName());

    assertEquals(15, character.getScore(Wisdom));
    assertEquals(14, character.getScore(Intelligence));
    assertEquals(13, character.getScore(Charisma));
    assertEquals(12, character.getScore(Dexterity));
    assertEquals(10, character.getScore(Strength));
    assertEquals(8, character.getScore(Constitution));

    assertEquals(2, character.getModifier(Wisdom));
    assertEquals(2, character.getModifier(Intelligence));
    assertEquals(1, character.getModifier(Charisma));
    assertEquals(1, character.getModifier(Dexterity));
    assertEquals(0, character.getModifier(Strength));
    assertEquals(-1, character.getModifier(Constitution));
  }

  @Test
  public void aCharacterShouldBeAbleToSetAbilityScore() {
    Character character = new Character.Builder("Elyn",
        Arrays.asList(Wisdom, Intelligence, Charisma, Dexterity, Strength, Constitution))
        .setAbilityScore(Wisdom, 18)
        .setAbilityScore(Constitution, 3)
        .build();

    assertEquals(18, character.getScore(Wisdom));
    assertEquals(3, character.getScore(Constitution));
  }

  @Test
  public void aCharacterShouldBeAbleToSetProficiencyBonus() {
    Character character = new Character.Builder("Elyn",
        Arrays.asList(Wisdom, Intelligence, Charisma, Dexterity, Strength, Constitution))
        .setAbilityScore(Wisdom, 18)
        .setProficiencyBonus(5)
        .build();

    assertEquals(5, character.getProficiencyBonus());
  }
}
