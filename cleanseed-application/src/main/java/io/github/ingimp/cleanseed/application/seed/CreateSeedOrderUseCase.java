package io.github.ingimp.cleanseed.application.seed;

import io.github.ingimp.cleanseed.domain.seed.SeedOrder;
import io.github.ingimp.cleanseed.domain.seed.SeedOrders;
import io.github.ingimp.cleanseed.domain.seed.SeedUser;
import io.github.ingimp.cleanseed.domain.seed.SeedUsers; // Dovrai creare questa interfaccia

public class CreateSeedOrderUseCase {
    private final SeedOrders orders;
    private final SeedUsers users; // Serve per recuperare l'owner

    public CreateSeedOrderUseCase(SeedOrders orders, SeedUsers users) {
        this.orders = orders;
        this.users = users;
    }

    public void execute(String id, String description, double price, String userId) {
        // 1. Recuperiamo l'utente dal dominio
        SeedUser owner = users.withId(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        // 2. Creiamo l'ordine usando la factory di business (create)
        // Nota: LocalDateTime.now() lo gestisce la factory internamente
        SeedOrder order = SeedOrder.create(id, description, price, owner);

        // 3. Salviamo
        orders.add(order);
    }
}