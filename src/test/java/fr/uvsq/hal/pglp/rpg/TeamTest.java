package fr.uvsq.hal.pglp.rpg;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static fr.uvsq.hal.pglp.rpg.Ability.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeamTest {
  private Character elyn;
  private Character guerric;
  private Team ekip;

  @Before
  public void setup() {
    elyn = new Character.Builder("Elyn",
        Arrays.asList(Wisdom, Intelligence, Charisma, Dexterity, Strength, Constitution)).build();
    guerric = new Character.Builder("Guerric",
        Arrays.asList(Wisdom, Intelligence, Charisma, Dexterity, Strength, Constitution)).build();

    ekip = new Team();
    ekip.add(elyn);
    ekip.add(guerric);
  }

  @Test
  public void aTeamShouldContainCharacters() {
    assertTrue(ekip.contains(elyn));
    assertTrue(ekip.contains(guerric));
  }

  @Test
  public void aTeamShouldContainCharactersAndTeams() {
    Team ekip2 = new Team();
    ekip.add(ekip2);
    assertTrue(ekip.contains(ekip2));
  }

  // Ne pas permettre la création de groupes se contenant eux-même même
  // indirectement
  @Test
  public void aTeamShouldNotContainItself() {
    Team ekip2 = new Team();
    ekip2.add(elyn);

    Team team2 = new Team();
    team2.add(guerric);
    team2.add(ekip2);

    ekip2.add(team2);
    assertFalse(ekip2.contains(ekip2));
  }

  @Test
  public void shouldReturnRecursiveTeamSize() {
    Team ekip2 = new Team();
    ekip2.add(guerric);
    ekip.add(ekip2);
    assertEquals(3, ekip.getSize());
  }

  // Pattern Iterator:
  @Test
  public void aTeamShouldBeIterable() {
    final List<TeamComponent> expectedEmployees = List.of(elyn, guerric);
    List<TeamComponent> visitedEmployees = new ArrayList<>();
    for (TeamComponent element : ekip) {
      visitedEmployees.add(element);
    }
    assertEquals(expectedEmployees, visitedEmployees);
  }

  @Test
  public void unGroupeImbriquePeutEtreParcourus() {
    final List<TeamComponent> expectedEmployees = List.of(elyn, guerric);
    List<TeamComponent> visitedEmployees = new ArrayList<>();

    Team team1 = new Team();
    team1.add(elyn);

    Team team2 = new Team();
    team2.add(guerric);

    team1.add(team2);

    for (TeamComponent element : team1) {
      visitedEmployees.add(element);
    }
    assertEquals(expectedEmployees, visitedEmployees);
  }

  @Test
  public void unAutreGroupeImbriquePeutEtreParcourus() {
    final List<TeamComponent> expectedEmployees = List.of(elyn, guerric, elyn, guerric, elyn, guerric, guerric, elyn,
        guerric, elyn);
    List<TeamComponent> visitedEmployees = new ArrayList<>();

    Team team1 = new Team();
    team1.add(elyn);

    Team team2 = new Team();
    team2.add(guerric);

    Team team3 = new Team();
    team3.add(elyn);
    team3.add(guerric);

    team2.add(team3);
    team2.add(elyn);

    team1.add(team2);
    team1.add(guerric);
    team1.add(team2);
    for (TeamComponent element : team1) {
      visitedEmployees.add(element);
    }
    assertEquals(expectedEmployees, visitedEmployees);
  }
}
