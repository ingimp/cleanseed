# CleanSeed 💡

**CleanSeed** è un template "Gold Standard" per applicazioni Java moderne basate su **Spring Boot 3** e **Java 17+**.
È progettato seguendo i principi della **Clean Architecture** (Esagonale) per garantire disaccoppiamento, testabilità e manutenibilità.

## 🏗️ Architettura dei Moduli

Il progetto è diviso in 4 moduli logici:

* **`cleanseed-domain`**: Il cuore del business. Contiene le entità di dominio e le interfacce (Repository). Zero dipendenze esterne (tranne Lombok).
* **`cleanseed-application`**: La logica applicativa. Contiene gli **Use Case** e gli **Orchestrator** (Application Services). Gestisce la transazionalità applicativa.
* **`cleanseed-infrastructure`**: L'implementazione tecnica. Qui risiedono gli adapter per la persistenza (Spring Data JPA), i database (H2/PostgreSQL) e i servizi esterni.
* **`cleanseed-webapp`**: L'entry point. Configura il contesto Spring, contiene i Controller REST e i DTO per separare il contratto API dal dominio.

## 🚀 Caratteristiche principali

- **Transazionalità**: Gestita al livello Application per garantire l'atomicità degli Use Case complessi.
- **DTO Mapping**: Separazione netta tra Domain Objects e API Responses.
- **Modern Java**: Utilizzo di `record` per i DTO e supporto nativo al flag `-parameters` per Controller puliti.
- **Maven Multi-Module**: Build strutturato per scalare su progetti di grandi dimensioni.

## 🛠️ Come iniziare

1. Clona il repository.
2. Esegui il build con Maven:
   ```bash
   mvn clean install