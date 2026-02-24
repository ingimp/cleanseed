package io.github.ingimp.cleanseed.infrastructure.seed.persistence.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "seed_orders")
@Getter @Setter
public class SeedOrderJpaEntity {
    @Id
    private String id;
    private String description;
    private double price;
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private SeedUserJpaEntity owner;
}   