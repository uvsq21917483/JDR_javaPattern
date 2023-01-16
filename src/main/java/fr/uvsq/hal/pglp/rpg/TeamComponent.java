package fr.uvsq.hal.pglp.rpg;

// TeamComponent (pattern Composite)
public interface TeamComponent {
    // Implémentation de la recherche récursive
    public boolean contains(TeamComponent other);

    // Détermine récursivement le nombre de personnages dans un groupe
    public int getSize();
}
