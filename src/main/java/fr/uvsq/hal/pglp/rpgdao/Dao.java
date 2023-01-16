package fr.uvsq.hal.pglp.rpgdao;

import java.util.Optional;

/**
 * Interface DAO servant de base aux opérations CRUD
 */
public interface Dao<T> {
    boolean create(T objet);

    Optional<T> read(String identifier);

    boolean update(T objet);

    void delete(T objet);
}