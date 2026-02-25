package io.github.ingimp.cleanseed.webapp.config;

import io.github.ingimp.cleanseed.domain.seed.SeedUser;
import io.github.ingimp.cleanseed.domain.seed.SeedUsers;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeedDataConfig {

    private final SeedUsers users;

    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        users.withId("user-1").orElseGet(() -> {
            SeedUser user = new SeedUser("user-1", "admin");
            users.add(user);
            return user;
        });
    }
}
