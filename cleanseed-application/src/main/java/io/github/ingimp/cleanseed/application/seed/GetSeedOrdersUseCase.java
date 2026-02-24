package io.github.ingimp.cleanseed.application.seed;

import io.github.ingimp.cleanseed.domain.seed.SeedOrder;
import io.github.ingimp.cleanseed.domain.seed.SeedOrders;

import java.util.List;
import java.util.Optional;

public class GetSeedOrdersUseCase {

    private final SeedOrders orders;

    public GetSeedOrdersUseCase(SeedOrders orders) {
        this.orders = orders;
    }

    public List<SeedOrder> executeAll() {
        return orders.all();
    }

    public List<SeedOrder> executeRecent() {
        return orders.findRecent();
    }

    public Optional<SeedOrder> executeWithId(String id) {
        return orders.withId(id);
    }
}