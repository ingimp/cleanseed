package io.github.ingimp.cleanseed.infrastructure.seed.persistence;

import io.github.ingimp.cleanseed.domain.seed.SeedOrder;
import io.github.ingimp.cleanseed.domain.seed.SeedOrders;
import io.github.ingimp.cleanseed.domain.seed.SeedUser;
import io.github.ingimp.cleanseed.infrastructure.seed.persistence.jpa.SeedOrderJpaEntity;
import io.github.ingimp.cleanseed.infrastructure.seed.persistence.jpa.SeedUserJpaEntity;
import io.github.ingimp.cleanseed.infrastructure.seed.persistence.jpa.JpaSeedOrderRepository;
import io.github.ingimp.cleanseed.infrastructure.seed.persistence.jpa.JpaSeedUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Primary // Questo assicura che Spring scelga questa versione invece di quella In-Memory
@RequiredArgsConstructor
public class JpaSeedOrderAdapter implements SeedOrders {

    private final JpaSeedOrderRepository orderRepository;
    private final JpaSeedUserRepository userRepository;


    @Override
    public void add(SeedOrder order) {
        // Recuperiamo il riferimento all'utente (L'utente deve esistere nel DB)
        SeedUserJpaEntity userEntity = userRepository.findById(order.getOwner().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        SeedOrderJpaEntity entity = new SeedOrderJpaEntity();
        entity.setId(order.getId());
        entity.setDescription(order.getDescription());
        entity.setPrice(order.getPrice());
        entity.setTimestamp(order.getTimestamp());
        entity.setOwner(userEntity);

        orderRepository.save(entity);
    }

    @Override
    public List<SeedOrder> all() {
        // Usiamo la versione ordinata anche qui come definito nel repository
        return orderRepository.findAllByOrderByTimestampDesc().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private SeedOrder toDomain(SeedOrderJpaEntity entity) {
        // 1. Mappiamo l'owner (JPA -> Dominio)
        SeedUser domainUser = new SeedUser(
                entity.getOwner().getId(),
                entity.getOwner().getUsername()
        );

        // 2. Usiamo il metodo RESTORE per ricostituire il dominio dal DB
        return SeedOrder.restore(
                entity.getId(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getTimestamp(),
                domainUser
        );
    }

    @Override
    public Optional<SeedOrder> withId(String id) {
        return orderRepository.findById(id).map(this::toDomain);
    }

    @Override
    public void remove(String id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<SeedOrder> findRecent() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(24);
        return orderRepository.findAllByTimestampAfterOrderByTimestampDesc(cutoff).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

}
