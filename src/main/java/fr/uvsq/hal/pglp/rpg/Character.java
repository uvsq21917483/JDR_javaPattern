package fr.uvsq.hal.pglp.rpg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * La classe <code>Character</code> représente un personnage de JdR.
 *
 * @author hal
 * @version 2022
 */
public class Character implements TeamComponent {
    private final String name;
    private List<Ability> abilitiesOrder;
    private Map<Ability, Integer> abilities;
    // Note: Le modificateur de caractéristique n'est pas stocké car instantané à
    // re-calculer

    private List<Skill> skillsList;
    private final int proficiencyBonus;

    // Contructeur privé de la classe Character avec Builder en paramètre
    private Character(Builder builder) {
        // Required parameters
        name = builder.name;
        abilitiesOrder = builder.abilitiesOrder;
        abilities = builder.abilities;

        // Optional parameters
        proficiencyBonus = builder.proficiencyBonus;
        skillsList = builder.skillsList;
    }

    public String getName() {
        return name;
    }

    public List<Ability> getOrder() {
        return abilitiesOrder;
    }

    public Map<Ability, Integer> getAbilities() {
        return abilities;
    }

    public List<Skill> getSkills() {
        return skillsList;
    }

    public int getProficiencyBonus() {
        return proficiencyBonus;
    }

    public int getScore(Ability ability) {
        return abilities.get(ability);
    }

    public int getModifier(Ability ability) {
        return (abilities.get(ability) - 10) / 2;
    }

    public void addSkill(Skill skill) {
        skillsList.add(skill);
    }

    /**
     * Retourne le bonus pour une compétence donnée.
     * 
     *
     */
    public int getSkillProficiency(Skill skill) {
        if (skillsList.contains(skill))
            return getModifier(skill.getLinkedAbility()) + proficiencyBonus;
        else
            return getModifier(skill.getLinkedAbility());
    }

    @Override
    public String toString() {
        String res = name + "\n";
        for (Ability ability : abilities.keySet()) {
            res += ability + ": " + abilities.get(ability) + " (";
            if (getModifier(ability) >= 0)
                res += "+";
            res += getModifier(ability) + ")\n";
        }
        return res;
    }

    /**
     * Jet de caractéristique : lance un D20 et y ajoute le modificateur, renvoie
     * vrai si >= au degré de difficulté.
     * 
     * @param ability Caractéristique liée au jet
     * @param dd      Degré de difficulté
     */
    public boolean abilityCheck(Ability ability, int dd) {
        Random rand = new Random();
        int diceRoll = rand.nextInt(20) + 1;
        diceRoll += getModifier(ability); // Ajout du modificateur
        return diceRoll >= dd;
    }

    public boolean skillCheck(Skill skill, int dd) {
        Random rand = new Random();
        int diceRoll = rand.nextInt(20) + 1;
        diceRoll += getSkillProficiency(skill);
        return diceRoll >= dd;
    }

    // Pattern Composite: Implémentation de la recherche récursive
    @Override
    public boolean contains(TeamComponent other) {
        return other == this;
    }

    // Pattern Composite: Détermine récursivement le nombre de personnages dans un
    // groupe
    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Character character = (Character) o;
        return name.equals(character.name) && abilitiesOrder.equals(character.abilitiesOrder)
                && abilities.equals(character.abilities)
                && skillsList.equals(character.skillsList)
                && proficiencyBonus == character.proficiencyBonus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, abilitiesOrder, abilities, skillsList, proficiencyBonus);
    }

    // Classe imbriquée Builder dans la classe Character
    public static class Builder {
        private final String name;
        private List<Ability> abilitiesOrder;
        private Map<Ability, Integer> abilities;

        // Attributs optionnels
        private List<Skill> skillsList;
        private int proficiencyBonus = 0;

        /**
         * Initialise un personnage en lui attribuant des valeurs au caractéristiques
         * selon son ordre de préférence.
         *
         * @param _name  Nom du personnage
         * @param first  Première caractéristique préférée
         * @param second Deuxième caractéristique préférée
         * @param third  Troisième caractéristique préférée
         * @param fourth Quatrième caractéristique préférée
         * @param fifth  Cinquième caractéristique préférée
         * @param sixth  Sixième caractéristique préférée
         * @throws IllegalArgumentException
         */
        public Builder(String _name, List<Ability> order) throws IllegalArgumentException {

            if (order.size() != 6) {
                throw new IllegalArgumentException();
            }

            // Création d'un personnage :
            // Nom + ordre de préférence des 6 caractéristiques
            // Génération de la suite de valeurs pour les caractéristiques:
            // Faire 6 lancers de 4 dés à 6 valeurs
            // La suite est composée des 3 valeurs max de chaque lancer
            // On attribue les valeurs décroissante selon l'ordre de préférence
            name = _name;
            abilitiesOrder = order;
            abilities = new HashMap<>();
            skillsList = new ArrayList<>();

            // Tirage aléatoire des valeurs des caractéristiques
            Random rand = new Random();
            int[] scores = new int[6]; // Tableau de la suite des valeurs des caractéristiques
            int[] diceRoll = new int[4]; // Tableau du lancer des 4 dés
            int sum = 0; // La somme des valeurs de caractéristique doit être comprise entre 60 et 80
            while (sum < 60 || sum > 80) {
                sum = 0;
                for (int i = 0; i < scores.length; i++) {
                    for (int j = 0; j < diceRoll.length; j++) {
                        diceRoll[j] = rand.nextInt(6) + 1; // Tirage aléatoire entre 1 et 6
                    }
                    Arrays.sort(diceRoll);
                    scores[i] = diceRoll[3] + diceRoll[2] + diceRoll[1];
                }
                Arrays.sort(scores);
                sum = Arrays.stream(scores).sum();
            }
            // Chaque caractéristique reçoit sa valeur (ordre décroissant selon l'ordre de
            // préférence)
            int i = 5;
            for (Ability a : order) {
                abilities.put(a, scores[i]);
                i -= 1;
            }
        }

        public Builder nonRandomAbilities() {
            // Suite décroissante: 15, 14, 13, 12, 10, 8
            int[] descendingSuite = { 15, 14, 13, 12, 10, 8 };
            int i = 0;
            for (Ability a : abilitiesOrder) {
                abilities.put(a, descendingSuite[i]);
                i++;
            }
            return this;
        }

        public Builder setAbilityScore(Ability ability, int score) {
            abilities.put(ability, score);
            return this;
        }

        public Builder setProficiencyBonus(int bonus) {
            proficiencyBonus = bonus;
            return this;
        }

        public Builder setSkills(Skill... skills) {
            for (Skill s : skills)
                skillsList.add(s);
            return this;
        }

        // Fonction Build
        public Character build() {
            // Appel du constructeur private de la classe Character avec le builder this en
            // paramètre
            return new Character(this);
        }
    }

}
