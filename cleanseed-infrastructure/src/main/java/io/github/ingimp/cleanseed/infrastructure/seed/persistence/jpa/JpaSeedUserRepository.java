package io.github.ingimp.cleanseed.infrastructure.seed.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository tecnico per l'entità JPA DummyUser.
 * Questo è un dettaglio implementativo del modulo infrastructure.
 */
@Repository
public interface JpaSeedUserRepository extends JpaRepository<SeedUserJpaEntity, String> {
    // Qui potrai aggiungere query custom in futuro, ad esempio:
    // Optional<DummyUserJpaEntity> findByUsername(String username);
}
