package io.github.ingimp.cleanseed.application.seed;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class SeedOrderOrchestrator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeedOrderOrchestrator.class);

    private final CreateSeedOrderUseCase createUseCase;
    private final GetSeedOrdersUseCase getUseCase;

    @Transactional
    public void createOrderWithLog(String id, String description, double price, String userId) {
        createUseCase.execute(id, description, price, userId);

        int count = getUseCase.executeAll().size();
        LOGGER.info("[ORCHESTRATOR] Ordine creato con successo. Totale ordini nel sistema: {}", count);
    }
}
