package io.github.ingimp.cleanseed.domain.seed;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

    public interface SeedOrders {
    void add(SeedOrder order);
    List<SeedOrder> all();
    Optional<SeedOrder> withId(String id);
    void remove(String id);
    List<SeedOrder> findAfter(LocalDateTime cutoff);
}
