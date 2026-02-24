package io.github.ingimp.cleanseed.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "io.github.ingimp.cleanseed")
// Indispensabile per trovare le interfacce JpaRepository in altri moduli
@EnableJpaRepositories(basePackages = "io.github.ingimp.cleanseed.infrastructure")
// Indispensabile per trovare le classi @Entity in altri moduli
@EntityScan(basePackages = "io.github.ingimp.cleanseed.infrastructure")
public class CleanSeedApplication {

	public static void main(String[] args) {
		SpringApplication.run(CleanSeedApplication.class, args);
	}

}
