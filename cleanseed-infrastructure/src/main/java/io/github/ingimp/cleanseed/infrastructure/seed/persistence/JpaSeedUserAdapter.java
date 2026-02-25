package io.github.ingimp.cleanseed.infrastructure.seed.persistence;

import io.github.ingimp.cleanseed.domain.seed.SeedUser;
import io.github.ingimp.cleanseed.domain.seed.SeedUsers;
import io.github.ingimp.cleanseed.infrastructure.seed.persistence.jpa.JpaSeedUserRepository;
import io.github.ingimp.cleanseed.infrastructure.seed.persistence.jpa.SeedUserJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaSeedUserAdapter implements SeedUsers {

    private final JpaSeedUserRepository userRepository;

    @Override
    public Optional<SeedUser> withId(String id) {
        return userRepository.findById(id)
                .map(entity -> new SeedUser(entity.getId(), entity.getUsername()));
    }

    @Override
    public void add(SeedUser user) {
        SeedUserJpaEntity entity = new SeedUserJpaEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        userRepository.save(entity);
    }
}
