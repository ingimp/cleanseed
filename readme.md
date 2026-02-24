# CleanSeed 💡

**CleanSeed** is a "Gold Standard" template for modern Java applications based on **Spring Boot 3.2.12+** and **Java 17+**.
It is designed following **Clean Architecture** (Hexagonal) principles to ensure strict decoupling, testability, and enterprise-grade maintainability.

---

## 🏗️ Architectural Philosophy

The project is structured to enforce the **Dependency Rule**: dependencies point inwards toward the Domain. This ensures that the core business logic remains independent of frameworks, databases, or UI concerns.

### Module Breakdown
* **`cleanseed-domain`**: The Core. Contains Entities and Domain Collection interfaces. **Zero external dependencies** (except Lombok). It uses domain-driven naming (e.g., `SeedUsers` instead of `UserRepository`) to treat persistence as an abstraction.
* **`cleanseed-application`**: The Orchestrator. Contains **Use Cases** and Application Services. This layer handles transaction boundaries (`@Transactional`) and coordinates data flow between the domain and external adapters.
* **`cleanseed-infrastructure`**: The Technical Adapters. Contains implementation details: **Spring Data JPA**, database configurations (H2/PostgreSQL), and external service integrations.
* **`cleanseed-webapp`**: The Entry Point. The only module containing the `@SpringBootApplication` class. It hosts REST Controllers, Thymeleaf views, and DTOs to isolate the API contract from the internal domain.

---

## 🚀 Step-by-Step Creation Tutorial (The "Gold Standard" Workflow)

This guide documents the exact process used to build this template, serving as a blueprint for future professional projects.

### Step 1: Initializing the Web Core
Instead of starting from the IDE, generate the "heart" of the application first to ensure a clean Spring Boot context.
1.  Use **Spring Initializr**: Maven, Java 17+, Boot 3.2.12+.
2.  Set Artifact to `cleanseed-webapp`.
3.  Add base dependencies: `Spring Web`, `Lombok`, `Validation`.
4.  **Pro Tip**: Unzip this into a root directory named `cleanseed` to prepare for the multi-module shift.

### Step 2: Manually Creating the Parent POM
To avoid IDE-generated mess, create the Parent `pom.xml` in the root folder by hand.
* **Packaging**: Set to `<packaging>pom</packaging>`.
* **Centralized Management**: Move the `spring-boot-starter-parent` and global properties (like Java version) here.
* **Module Linking**: Connect the webapp as the first module:
    ```xml
    <modules>
        <module>cleanseed-webapp</module>
    </modules>
    ```

### Step 3: Domain & Logic Decoupling
Create `cleanseed-domain` and `cleanseed-application` as sibling modules.
* **Architectural Guardrail**: By keeping the Domain in a separate Maven module, you physically prevent the business logic from importing Web or Persistence classes, strictly enforcing the Clean Architecture layers.

### Step 4: Infrastructure & Dependency Sanitization
Implement the `cleanseed-infrastructure` module to bridge the Domain interfaces with Spring Data JPA.
* **Security Audit**: Run `mvn dependency:tree` to verify that transitive dependencies (like Tomcat or SnakeYAML) are updated to non-vulnerable versions.

### Step 5: Git Professional Initialization
Before the first commit, configure the environment for cross-platform collaboration:
1.  **`.gitignore`**: Exclude IDE files (`.idea/`), build artifacts (`target/`), and future frontend assets (`node_modules/`).
2.  **`.gitattributes`**: Define `* text=auto` and force specific line endings (`eol=lf` for Linux/Mac, `eol=crlf` for Windows scripts) to prevent "ghost" changes in Git.

---

## 🛠️ Development Best Practices

- **Transactional Use Cases**: Always place `@Transactional` at the Application layer, not the Infrastructure layer, to ensure use-case atomicity.
- **Pluralized Domain Collections**: Use interfaces like `SeedUsers` instead of `SeedUserRepository` in the domain. This promotes a "Collection" mental model rather than a "Database" one.
- **Records for DTOs**: Utilize Java `record` for Web responses to ensure immutability and concise code.
- **Safety**: The project is patched against **CVE-2024-57699** by ensuring Tomcat 10.1.33+ via Spring Boot 3.2.12.

---

## 📈 Roadmap
- [ ] **Tailwind CSS Integration**: Using `frontend-maven-plugin` for a unified Java/Node build process.
- [ ] **CI/CD**: GitHub Actions for automated testing.
- [ ] **Security**: Spring Security integration with a Clean Architecture approach.

---

## 🛠️ Build Instructions
```bash
mvn clean install
```

### Release Gate
```bash
mvn -Prelease clean verify
```
Use this profile in CI release pipelines to enforce non-SNAPSHOT dependency constraints.

## 🚀 Running the Application

Since this is a multi-module project, you must run the application from the **root** directory or specifically target the `webapp` module.

### Option 1: Using Maven (Recommended for Dev)
```bash
mvn spring-boot:run -pl cleanseed-webapp
```