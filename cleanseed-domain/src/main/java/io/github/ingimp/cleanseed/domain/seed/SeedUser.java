package io.github.ingimp.cleanseed.domain.seed;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SeedUser {
    private final String id;
    private final String username;
}