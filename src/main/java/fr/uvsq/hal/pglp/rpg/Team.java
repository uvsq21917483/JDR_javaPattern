package fr.uvsq.hal.pglp.rpg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * La classe <code>Team</code> représente un groupe de personnages.
 *
 * @author hal
 * @version 2022
 */
public class Team implements TeamComponent, Iterable<TeamComponent> {
    // La liste peut à la fois contenir des Groupes et des Personnages
    private List<TeamComponent> characters;

    public Team() {
        characters = new ArrayList<TeamComponent>();
    }

    public void add(TeamComponent toAdd) {
        // Ne pas permettre la création de groupes se contenant eux-même même
        // indirectement : implémenter la recherche récursive
        // Il ne faut pas que l'élément à ajouter CONTIENNE la team courante
        if (toAdd == this || toAdd.contains(this))
            return;
        characters.add(toAdd);
    }

    @Override
    public boolean contains(TeamComponent toSearch) {
        // Recherche récursive :
        // Si l'élément est un personnage : il renvoie si c'est lui qu'on cherche
        // Si l'élément est une team : on rappelle contains
        // -> On ajoute une méthode booléenne dans l'interface TeamComponent pour
        // qu'elle puisse applicable dans les leafs
        if (characters.contains(toSearch))
            return true;

        for (TeamComponent c : characters) {
            if (c.contains(toSearch))
                return true;
        }
        return false;
    }

    /**
     * Détermine récursivement le nombre de personnages dans un groupe
     * 
     * @return Nombre de personnages dans un groupe
     */
    public int getSize() {
        int size = 0;
        for (TeamComponent c : characters) {
            size += c.getSize();
        }
        return size;
    }

    @Override
    public Iterator<TeamComponent> iterator() {
        return new TeamIterator(characters);
    }

    public class TeamIterator implements Iterator<TeamComponent> {
        // Implémentation de base en utilisant l'iterator Java sur la Liste characters
        // private Iterator<TeamComponent> iterator;

        // Implémentation avec une pile
        private Stack<Iterator<TeamComponent>> iteratorStack;

        public TeamIterator(List<TeamComponent> characters) {
            // iterator = characters.iterator();
            iteratorStack = new Stack<>();
            iteratorStack.push(characters.iterator());
        }

        @Override
        public boolean hasNext() {
            // return iterator.hasNext();

            boolean hasNext = iteratorStack.peek().hasNext(); // On get le hasNext du dernier itérateur dans la pile
            // Si (et tant que) le dernier élément n'a pas de next
            while (!hasNext) {
                iteratorStack.pop();
                if (iteratorStack.isEmpty()) {
                    return false;
                }
                hasNext = iteratorStack.peek().hasNext();
            }
            return hasNext;
        }

        @Override
        public TeamComponent next() {
            // return iterator.next();

            TeamComponent nextElement = iteratorStack.peek().next();
            while (nextElement instanceof Team) { // Si ce next élément est une team, on veut renvoyer son next
                Team team = (Team) nextElement;
                Iterator<TeamComponent> newIterator = team.iterator();
                iteratorStack.push(newIterator);
                nextElement = iteratorStack.peek().next();
            }
            return nextElement;
        }
    }
}
