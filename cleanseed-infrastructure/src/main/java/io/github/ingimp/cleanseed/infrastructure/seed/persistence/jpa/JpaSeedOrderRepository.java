package io.github.ingimp.cleanseed.infrastructure.seed.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface JpaSeedOrderRepository extends JpaRepository<SeedOrderJpaEntity, String> {
    // Spring genererà la query automaticamente per noi
    List<SeedOrderJpaEntity> findAllByTimestampAfterOrderByTimestampDesc(LocalDateTime cutoff);

    // Per il metodo all() ordinato
    List<SeedOrderJpaEntity> findAllByOrderByTimestampDesc();
}