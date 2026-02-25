package io.github.ingimp.cleanseed.application.seed;

import io.github.ingimp.cleanseed.domain.seed.SeedOrder;
import io.github.ingimp.cleanseed.domain.seed.SeedOrders;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class GetSeedOrdersUseCase {

    private final SeedOrders orders;
    private final Clock clock;

    public GetSeedOrdersUseCase(SeedOrders orders, Clock clock) {
        this.orders = orders;
        this.clock = clock;
    }

    public List<SeedOrder> executeAll() {
        return orders.all();
    }

    public List<SeedOrder> executeRecent() {
        LocalDateTime cutoff = LocalDateTime.now(clock).minusHours(24);
        return orders.findAfter(cutoff);
    }

    public Optional<SeedOrder> executeWithId(String id) {
        return orders.withId(id);
    }
}
