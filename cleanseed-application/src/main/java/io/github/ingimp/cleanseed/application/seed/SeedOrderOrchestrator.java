package io.github.ingimp.cleanseed.application.seed;

import lombok.RequiredArgsConstructor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class SeedOrderOrchestrator {


    private final CreateSeedOrderUseCase createUseCase;
    private final GetSeedOrdersUseCase getUseCase;

    @Transactional
    public void createOrderWithLog(String id, String description, double price, String userId) {
        createUseCase.execute(id, description, price, userId);

        int count = getUseCase.executeAll().size();
        log.info("Messaggio di log aggiunto da Codex");
        log.info("[ORCHESTRATOR] Ordine creato con successo. Totale ordini nel sistema: {}", count);
    }
}
