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
@Primary
@RequiredArgsConstructor
public class JpaSeedOrderAdapter implements SeedOrders {

    private final JpaSeedOrderRepository orderRepository;
    private final JpaSeedUserRepository userRepository;


    @Override
    public void add(SeedOrder order) {
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
        return orderRepository.findAllByOrderByTimestampDesc().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private SeedOrder toDomain(SeedOrderJpaEntity entity) {
        SeedUser domainUser = new SeedUser(
                entity.getOwner().getId(),
                entity.getOwner().getUsername()
        );

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
    public List<SeedOrder> findAfter(LocalDateTime cutoff) {
        return orderRepository.findAllByTimestampAfterOrderByTimestampDesc(cutoff).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

}
