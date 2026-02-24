package io.github.ingimp.cleanseed.webapp.feature.seed;

import io.github.ingimp.cleanseed.application.seed.SeedOrderOrchestrator;
import io.github.ingimp.cleanseed.application.seed.GetSeedOrdersUseCase;
import io.github.ingimp.cleanseed.domain.seed.SeedOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seed/orders")
@RequiredArgsConstructor
public class SeedOrderController {

    private final SeedOrderOrchestrator orchestrator; // Orchestratore per azioni complesse
    private final GetSeedOrdersUseCase getUseCase;    // Use case semplice per le letture

    @PostMapping
    public void create(@RequestParam String id,
                       @RequestParam String description,
                       @RequestParam double price,
                       @RequestParam String userId) {
        // Il controller chiama l'orchestratore
        orchestrator.createOrderWithLog(id, description, price, userId);
    }

    @GetMapping
    public List<SeedOrder> all() {
        return getUseCase.executeAll();
    }

    @GetMapping("/recent")
    public List<SeedOrder> recent() {
        return getUseCase.executeRecent();
    }

    @GetMapping("/{id}")
    public SeedOrder byId(@PathVariable String id) {
        return getUseCase.executeWithId(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}