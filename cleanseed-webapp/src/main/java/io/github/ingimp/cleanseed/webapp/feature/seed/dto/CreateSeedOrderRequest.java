package io.github.ingimp.cleanseed.webapp.feature.seed.dto;

public record CreateSeedOrderRequest(
        String id,
        String description,
        double price,
        String userId
) {
}
