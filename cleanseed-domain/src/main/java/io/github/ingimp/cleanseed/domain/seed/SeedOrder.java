package io.github.ingimp.cleanseed.domain.seed;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Costruttore privato per obbligare l'uso della factory
public final class SeedOrder {

    private final String id;
    private final String description;
    private final double price;
    private final LocalDateTime timestamp;
    private final SeedUser owner;

    /**
     * Static Factory Method - Il punto unico di creazione
     */
    public static SeedOrder create(String id, String description, double price, SeedUser owner) {
        // Validazioni di Business
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id is required");
        if (description == null || description.isBlank()) throw new IllegalArgumentException("description is required");
        if (price < 0) throw new IllegalArgumentException("price must be >= 0");

        Objects.requireNonNull(owner, "owner is required");

        return new SeedOrder(
                id,
                description,
                price,
                LocalDateTime.now(), // Il timestamp lo decidiamo noi al momento della creazione
                owner
        );
    }

    /**
     * Factory per la ricostituzione (usata dagli Adapter/DB)
     * Serve per caricare dati esistenti senza cambiare il timestamp originale
     */
    public static SeedOrder restore(String id, String description, double price, LocalDateTime timestamp, SeedUser owner) {
        return new SeedOrder(id, description, price, timestamp, owner);
    }
}