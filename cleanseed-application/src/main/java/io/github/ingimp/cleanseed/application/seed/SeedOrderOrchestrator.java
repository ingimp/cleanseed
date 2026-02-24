package io.github.ingimp.cleanseed.application.seed;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class SeedOrderOrchestrator {

    private final CreateSeedOrderUseCase createUseCase;
    private final GetSeedOrdersUseCase getUseCase;

    @Transactional
    public void createOrderWithLog(String id, String description, double price, String userId) {
        // 1. Delega la creazione effettiva allo Use Case specifico
        createUseCase.execute(id, description, price, userId);

        // 2. Esegue logica di coordinamento (es: contare gli ordini totali dopo l'aggiunta)
        int count = getUseCase.executeAll().size();
        System.out.println("[ORCHESTRATOR] Ordine creato con successo. Totale ordini nel sistema: " + count);
    }
}