package fr.uvsq.hal.pglp.rpg;

/**
 * La classe <code>Ability</code> représente une compétence d'un personnage.
 *
 * @author hal
 * @version 2022
 */
public enum Skill {
    Acrobatics,
    Arcana,
    Athletics,
    Medicine,
    Persuasion;

    public Ability getLinkedAbility() {
        switch (this) {
            case Acrobatics:
                return Ability.Dexterity;
            case Arcana:
                return Ability.Intelligence;
            case Athletics:
                return Ability.Strength;
            case Medicine:
                return Ability.Wisdom;
            case Persuasion:
                return Ability.Charisma;
            default:
                return null;
        }
    }
}

// Strength :
// Athletics

// Dexterity :
// Acrobatics
// Sleight of Hand
// Stealth

// Intelligence :
// Arcana
// History
// Investigation
// Nature
// Religion

// Wisdom :
// Animal Handling
// Insight
// Medicine
// Perception
// Survival

// Charisma :
// Deception
// Intimidation
// Performance
// Persuasion