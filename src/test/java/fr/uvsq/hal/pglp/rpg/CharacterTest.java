package fr.uvsq.hal.pglp.rpg;

import org.junit.Test;

import static fr.uvsq.hal.pglp.rpg.Ability.*;
import static fr.uvsq.hal.pglp.rpg.Skill.*;
import static org.junit.Assert.*;

import java.util.Arrays;

public class CharacterTest {

  @Test
  public void aCharacterShouldReturnItsVariables() {
    Character character = new Character.Builder("Elyn",
        Arrays.asList(Wisdom, Intelligence, Charisma, Dexterity, Strength, Constitution))
        .setAbilityScore(Wisdom, 18)
        .setProficiencyBonus(5)
        .build();

    assertEquals(18, character.getScore(Wisdom));
    assertEquals(4, character.getModifier(Wisdom));
    assertEquals(5, character.getProficiencyBonus());
  }

  @Test
  public void shouldMakeAnAbilityCheck() {
    Character character = new Character.Builder("Elyn",
        Arrays.asList(Wisdom, Intelligence, Charisma, Dexterity, Strength, Constitution))
        .setAbilityScore(Wisdom, 18)
        .setProficiencyBonus(5)
        .build();

    assertNotNull(character.abilityCheck(Wisdom, 10));
  }

  @Test
  public void shouldReturnRightSkillProficiency() {
    Character character = new Character.Builder("Elyn",
        Arrays.asList(Wisdom, Intelligence, Charisma, Dexterity, Strength, Constitution))
        .nonRandomAbilities()
        .setProficiencyBonus(5)
        .setSkills(Medicine, Arcana)
        .build();

    // Le personnage maîtrise Medicine: reçoit 2 (valeur de Wisdom) + 5 (proficiency
    // bonus)
    assertEquals(7, character.getSkillProficiency(Medicine));

    // Le personnage ne maîtrise pas Persuation : reçoit 1 (valeur de Charisma)
    assertEquals(1, character.getSkillProficiency(Persuasion));
  }

  @Test
  public void shouldMakeASkillCheck() {
    Character character = new Character.Builder("Elyn",
        Arrays.asList(Wisdom, Intelligence, Charisma, Dexterity, Strength, Constitution))
        .nonRandomAbilities()
        .setProficiencyBonus(5)
        .setSkills(Persuasion)
        .build();

    assertNotNull(character.skillCheck(Persuasion, 7));
  }
}
