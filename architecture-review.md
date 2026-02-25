# Revisione architetturale CleanSeed (Java/Spring)

## Contesto e vincoli
- Analisi statica del repository, senza esecuzione di build/test Maven.
- Focus: Clean Architecture (hexagonal), DDD, Lean.

## A) Mappa architetturale

### Moduli e responsabilità

| Modulo | Responsabilità attuale | Dipendenze dichiarate | Dipendenze **consentite** (target) | Dipendenze **vietate** (target) |
|---|---|---|---|---|
| `cleanseed-domain` | Modello di dominio (`SeedOrder`, `SeedUser`) + porte repository (`SeedOrders`, `SeedUsers`) | Lombok | Solo JDK + eventuali librerie puramente tecniche non-framework | Spring/JPA/Web, dipendenze verso application/infrastructure/webapp |
| `cleanseed-application` | Use case (`Create...`, `Get...`) + orchestrazione applicativa | `cleanseed-domain`, `spring-tx`, Lombok | `domain` + interfacce/porte | Dipendenza diretta da Spring annotations/transazioni, dipendenze verso infrastructure/webapp |
| `cleanseed-infrastructure` | Adapter persistenza (JPA e in-memory), entity JPA, repository Spring Data | `cleanseed-domain`, Spring Data JPA, H2 | `domain` (implementazione porte) + framework tecnici | Dipendenza da application/webapp |
| `cleanseed-webapp` | Bootstrap Spring Boot, delivery REST, wiring use case, seed dati iniziali | `cleanseed-application`, `cleanseed-infrastructure`, Spring Web/Thymeleaf/Test | `application` (+ eventualmente dto propri) | Accesso diretto a dettagli infrastrutturali (repository/entity), logica dominio/use case dispersa |

### Flusso dipendenze ricostruito dai pom
- Parent aggrega 4 moduli: `domain`, `application`, `infrastructure`, `webapp`.
- `application -> domain`.
- `infrastructure -> domain`.
- `webapp -> application` **e** `webapp -> infrastructure`.
- Non risultano dipendenze inverse esplicite tra moduli, ma ci sono leak di framework/layer nel codice.

### Regole target (Clean/Hexagonal)
- **Domain**: puro, nessun dettaglio tecnico.
- **Application**: use case + orchestrazione, ma framework-agnostic (idealmente senza annotation Spring).
- **Infrastructure**: implementa porte definite internamente (application/domain) e mappa modelli tecnici.
- **Webapp**: adapter di delivery (REST), converte request/response in command/query use case.

## B) PRO (cosa è fatto bene)

1. **Separazione a moduli coerente con intent hexagonal**: `domain`, `application`, `infrastructure`, `webapp` già distinti.
2. **Porte repository nel dominio** (`SeedOrders`, `SeedUsers`) e adapter infrastrutturali che le implementano.
3. **Entity JPA separate dal dominio** (`SeedOrderJpaEntity`, `SeedUserJpaEntity`) con mapping esplicito verso domain model.
4. **Use case espliciti** (`CreateSeedOrderUseCase`, `GetSeedOrdersUseCase`) invece di service “onnicomprensivi”.
5. **Factory di creazione nel dominio** (`SeedOrder.create`) che centralizza alcune invariant di business.

## C) RISCHI / SMELL

1. **Leak framework in application layer**
   - `SeedOrderOrchestrator` usa `@Transactional` (Spring) nel modulo application.
   - Impatto: application non più framework-agnostic, testabilità e portabilità ridotte.

2. **Port placement discutibile (DDD/Clean)**
   - Le interfacce `SeedOrders` / `SeedUsers` sono nel dominio.
   - Se trattate come “repository tecnici di persistenza”, spesso è più chiaro definirle in application come output port dei use case (dipende dallo stile, ma qui il dominio appare anemico e orientato a CRUD).

3. **Webapp accoppiata a dettagli infrastructure**
   - `SeedDataConfig` usa direttamente `JpaSeedUserRepository` e `SeedUserJpaEntity`.
   - Violazione boundary: delivery/bootstrap dipende da dettagli JPA, bypassando porte/use case.

4. **Boundary use case incompleto su error handling**
   - `CreateSeedOrderUseCase` lancia `RuntimeException` generica per utente mancante.
   - Controller lancia `RuntimeException("Order not found")`.
   - Manca tassonomia errori applicativi (es. `UserNotFound`, `OrderNotFound`) e mappatura HTTP coerente.

5. **Incoerenza DDD naming/semantiche aggregate**
   - Prefisso `Seed` ovunque (`SeedOrder`, `SeedUser`, `SeedOrders`) riduce espressività ubiquitous language; sembra naming tecnico/template.
   - Non è chiaro aggregate root e confini transazionali (ordine dipende da user completo invece di identity/value object dedicato).

6. **Comportamento di dominio con forte dipendenza dal tempo di sistema**
   - `SeedOrder.create` usa `LocalDateTime.now()` direttamente.
   - Rende più difficile test predicibili e policy temporali (timezone/clock centralizzato).

7. **Leak di policy applicative in adapter**
   - `findRecent()` implementa regola “ultime 24h” direttamente in adapter in-memory e JPA.
   - La policy temporale dovrebbe stare in use case/domain service; l’adapter dovrebbe eseguire query parametrica (`after(cutoff)`).

8. **Orchestrator con side effect non governato**
   - `System.out.println` in `SeedOrderOrchestrator` è logging non strutturato e cross-cutting non gestito.

9. **Doppio adapter attivo sullo stesso port**
   - `InMemorySeedOrderAdapter` e `JpaSeedOrderAdapter` coexistono; risolto con `@Primary`.
   - Funziona, ma è fragile senza strategy/profile esplicito (`@Profile`) e può creare ambiguità in test/ambienti.

## D) Backlog miglioramenti consigliati

### P0 (immediati, piccoli, ad alto valore)
1. **Spostare transazione fuori da application**
   - Rimuovere `@Transactional` da `SeedOrderOrchestrator`.
   - Applicare transazione in un adapter/config Spring nel modulo webapp/infrastructure.
   - Verifica: nessuna annotation Spring in `cleanseed-application`.

2. **Introdurre errori applicativi tipizzati + exception handler REST**
   - Creare eccezioni checked/runtime specifiche (`UserNotFoundException`, `OrderNotFoundException`) nel layer application.
   - Mappare in `@ControllerAdvice` a 404/400.
   - Verifica: assenza di `RuntimeException` generiche nei use case/controller.

3. **Disaccoppiare SeedDataConfig da JPA details**
   - Sostituire uso diretto di `JpaSeedUserRepository`/`SeedUserJpaEntity` con una porta/use case (`EnsureDefaultUserUseCase` o `SeedUsers`).
   - Verifica: package webapp non importa classi `infrastructure...jpa`.

4. **Rendere policy “recent” esplicita nel use case**
   - Cambiare porta in `findAfter(Instant/LocalDateTime cutoff)`.
   - Calcolo cutoff nel use case, non negli adapter.
   - Verifica: nessun `now().minusHours(24)` in infrastructure.

### P1 (breve termine)
1. **Stabilire command/query object per use case**
   - Es. `CreateSeedOrderCommand` con validazioni base.
   - Riduce primitive obsession e firma fragile (4 parametri).

2. **Rinominare verso UL più pulito**
   - Progressivamente da `SeedOrder`/`SeedUser` a nomi di dominio reali (se “seed” non è termine business).
   - Verifica: glossary condiviso e naming allineato.

3. **Separare mapper DTO dal DTO**
   - `SeedOrderResponse.from(domain)` può diventare mapper dedicato (o assembler) nel delivery adapter.

4. **Uniformare gestione logging**
   - Sostituire `System.out.println` con logger strutturato.

### P2 (medio termine)
1. **Rivedere placement delle porte repository**
   - Valutare spostamento porte in application (output ports) se dominio resta senza logica comportamentale ricca.

2. **Introduzione test architetturali**
   - ArchUnit per regole di dipendenza e assenza leak framework.

3. **Profili espliciti adapter**
   - `@Profile("dev")` per in-memory, `@Profile("prod")` per JPA.

## E) Guardrail automatici suggeriti

1. **ArchUnit – dependency rule per layer**
   - Regola: package `..domain..` non deve dipendere da `org.springframework..`, `jakarta.persistence..`, `..infrastructure..`, `..webapp..`.
   - Regola: package `..application..` non deve dipendere da `org.springframework..` (o al massimo whitelist ristretta se necessario).

2. **ArchUnit – boundary webapp/infrastructure**
   - Regola: classi in `..webapp..` non devono accedere a `..infrastructure..jpa..` (entity/repository tecnici).
   - Consentire in webapp solo dipendenze a `..application..` (+ DTO propri).

3. **Maven Enforcer – dipendenze vietate per modulo**
   - In `cleanseed-domain`: `bannedDependencies` per `spring-*`, `jakarta.persistence*`, `hibernate*`.
   - In `cleanseed-application`: vietare starter Spring Boot e JPA; consentire solo API strettamente necessarie (idealmente nessuna).

## Evidenze principali consultate
- POM multi-modulo e dipendenze modulo-modulo.
- Use case/orchestrator applicativi.
- Adapter JPA/in-memory e repository/entity.
- Configurazioni webapp e controller REST.
