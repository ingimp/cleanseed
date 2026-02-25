package io.github.ingimp.cleanseed.webapp.config;

import io.github.ingimp.cleanseed.application.seed.CreateSeedOrderUseCase;
import io.github.ingimp.cleanseed.application.seed.SeedOrderOrchestrator;
import io.github.ingimp.cleanseed.application.seed.GetSeedOrdersUseCase;
import io.github.ingimp.cleanseed.domain.seed.SeedOrders;
import io.github.ingimp.cleanseed.domain.seed.SeedUsers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class UseCaseConfig {

    @Bean
    public Clock systemClock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public CreateSeedOrderUseCase createSeedOrderUseCase(SeedOrders orders, SeedUsers users) {
        return new CreateSeedOrderUseCase(orders, users);
    }

    @Bean
    public GetSeedOrdersUseCase getSeedOrdersUseCase(SeedOrders orders, Clock clock) {
        return new GetSeedOrdersUseCase(orders, clock);
    }

    @Bean
    public SeedOrderOrchestrator seedOrderOrchestrator(
            CreateSeedOrderUseCase createUseCase,
            GetSeedOrdersUseCase getUseCase) {
        return new SeedOrderOrchestrator(createUseCase, getUseCase);
    }
}
