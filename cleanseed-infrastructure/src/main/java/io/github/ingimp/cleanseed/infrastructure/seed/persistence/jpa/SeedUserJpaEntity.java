package io.github.ingimp.cleanseed.infrastructure.seed.persistence.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "seed_users")
@Getter
@Setter
public class SeedUserJpaEntity {
    @Id
    private String id;
    private String username;
}
