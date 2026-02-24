package io.github.ingimp.cleanseed.infrastructure.seed.persistence;

import io.github.ingimp.cleanseed.domain.seed.SeedOrder;
import io.github.ingimp.cleanseed.domain.seed.SeedOrders;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemorySeedOrderAdapter implements SeedOrders {

    private final Map<String, SeedOrder> store = new ConcurrentHashMap<>();

    @Override
    public void add(SeedOrder order) {
        store.put(order.getId(), order);
    }

    @Override
    public List<SeedOrder> all() {
        return store.values().stream()
                .sorted(Comparator.comparing(SeedOrder::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SeedOrder> withId(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void remove(String id) {
        store.remove(id);
    }

    @Override
    public List<SeedOrder> findRecent() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(24);
        return store.values().stream()
                .filter(o -> o.getTimestamp().isAfter(cutoff))
                .sorted(Comparator.comparing(SeedOrder::getTimestamp).reversed())
                .collect(Collectors.toList());
    }
}