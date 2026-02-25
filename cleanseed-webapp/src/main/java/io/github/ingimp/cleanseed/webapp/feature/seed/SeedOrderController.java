package io.github.ingimp.cleanseed.webapp.feature.seed;

import io.github.ingimp.cleanseed.application.seed.SeedOrderOrchestrator;
import io.github.ingimp.cleanseed.application.seed.GetSeedOrdersUseCase;
import io.github.ingimp.cleanseed.application.seed.exception.OrderNotFoundException;
import io.github.ingimp.cleanseed.webapp.feature.seed.dto.CreateSeedOrderRequest;
import io.github.ingimp.cleanseed.webapp.feature.seed.dto.SeedOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seed/orders")
@RequiredArgsConstructor
public class SeedOrderController {

    private final SeedOrderOrchestrator orchestrator;
    private final GetSeedOrdersUseCase getUseCase;

    @PostMapping
    public void create(@RequestBody CreateSeedOrderRequest request) {
        orchestrator.createOrderWithLog(
                request.id(),
                request.description(),
                request.price(),
                request.userId()
        );
    }

    @GetMapping
    public List<SeedOrderResponse> all() {
        return getUseCase.executeAll().stream()
                .map(SeedOrderResponse::from)
                .toList();
    }

    @GetMapping("/recent")
    public List<SeedOrderResponse> recent() {
        return getUseCase.executeRecent().stream()
                .map(SeedOrderResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public SeedOrderResponse byId(@PathVariable String id) {
        return getUseCase.executeWithId(id)
                .map(SeedOrderResponse::from)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }
}
