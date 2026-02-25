package io.github.ingimp.cleanseed.application.seed;

import io.github.ingimp.cleanseed.application.seed.exception.UserNotFoundException;
import io.github.ingimp.cleanseed.domain.seed.SeedOrder;
import io.github.ingimp.cleanseed.domain.seed.SeedOrders;
import io.github.ingimp.cleanseed.domain.seed.SeedUser;
import io.github.ingimp.cleanseed.domain.seed.SeedUsers;

public class CreateSeedOrderUseCase {
    private final SeedOrders orders;
    private final SeedUsers users;

    public CreateSeedOrderUseCase(SeedOrders orders, SeedUsers users) {
        this.orders = orders;
        this.users = users;
    }

    public void execute(String id, String description, double price, String userId) {
        SeedUser owner = users.withId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        SeedOrder order = SeedOrder.create(id, description, price, owner);
        orders.add(order);
    }
}
