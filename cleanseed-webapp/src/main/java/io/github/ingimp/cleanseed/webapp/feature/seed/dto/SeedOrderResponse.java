package io.github.ingimp.cleanseed.webapp.feature.seed.dto;

import io.github.ingimp.cleanseed.domain.seed.SeedOrder;

import java.time.LocalDateTime;

public record SeedOrderResponse(
        String id,
        String description,
        double price,
        LocalDateTime timestamp,
        String ownerId,
        String ownerUsername
) {
    public static SeedOrderResponse from(SeedOrder order) {
        return new SeedOrderResponse(
                order.getId(),
                order.getDescription(),
                order.getPrice(),
                order.getTimestamp(),
                order.getOwner().getId(),
                order.getOwner().getUsername()
        );
    }
}
