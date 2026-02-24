package io.github.ingimp.cleanseed.webapp.config;

import io.github.ingimp.cleanseed.application.seed.CreateSeedOrderUseCase;
import io.github.ingimp.cleanseed.application.seed.SeedOrderOrchestrator;
import io.github.ingimp.cleanseed.application.seed.GetSeedOrdersUseCase;
import io.github.ingimp.cleanseed.domain.seed.SeedOrders;
import io.github.ingimp.cleanseed.domain.seed.SeedUsers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateSeedOrderUseCase createDummyOrderUseCase(SeedOrders orders, SeedUsers users) {
        // Ora passiamo entrambi i collaboratori richiesti dal costruttore
        return new CreateSeedOrderUseCase(orders, users);
    }

    @Bean
    public GetSeedOrdersUseCase getDummyOrdersUseCase(SeedOrders orders) {
        return new GetSeedOrdersUseCase(orders);
    }

    @Bean
    public SeedOrderOrchestrator dummyOrderOrchestrator(
            CreateSeedOrderUseCase createUseCase,
            GetSeedOrdersUseCase getUseCase) {
        return new SeedOrderOrchestrator(createUseCase, getUseCase);
    }
}