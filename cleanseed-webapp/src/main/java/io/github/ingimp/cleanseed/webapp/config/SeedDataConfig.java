package io.github.ingimp.cleanseed.webapp.config;

import io.github.ingimp.cleanseed.infrastructure.seed.persistence.jpa.SeedUserJpaEntity;
import io.github.ingimp.cleanseed.infrastructure.seed.persistence.jpa.JpaSeedUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeedDataConfig {

    private final JpaSeedUserRepository userRepository;

    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        if (userRepository.count() == 0) {
            SeedUserJpaEntity user = new SeedUserJpaEntity();
            user.setId("user-1");
            user.setUsername("admin");
            userRepository.save(user);
        }
    }
}
