package io.github.ingimp.cleanseed.domain.seed;

import java.util.Optional;

public interface SeedUsers {
    Optional<SeedUser> withId(String id);
    void add(SeedUser user);
}