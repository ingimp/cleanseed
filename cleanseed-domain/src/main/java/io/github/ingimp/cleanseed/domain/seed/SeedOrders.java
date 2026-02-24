package io.github.ingimp.cleanseed.domain.seed;

import java.util.List;
import java.util.Optional;

    public interface SeedOrders {
    void add(SeedOrder order);
    List<SeedOrder> all();
    Optional<SeedOrder> withId(String id);
    void remove(String id);
    List<SeedOrder> findRecent();
}
