package io.github.ingimp.cleanseed.webapp.feature.seed;

import io.github.ingimp.cleanseed.application.seed.SeedOrderOrchestrator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SeedOrderFacade {

    private final SeedOrderOrchestrator orchestrator;

    @Transactional
    public void createOrder(String id, String description, double price, String userId) {
        orchestrator.createOrderWithLog(id, description, price, userId);
    }
}
