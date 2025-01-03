package ma.ensa.portefeuille_service;

import ma.ensa.portefeuille_service.entities.Portefeuille;
import ma.ensa.portefeuille_service.repositories.PortefeuilleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class PortefeuilleServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortefeuilleServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner initPortefeuilleDatabase(PortefeuilleRepository portefeuilleRepository) {
		return args -> {
			portefeuilleRepository.save(new Portefeuille(1000.0, 5000.0, "USD", 1L, new ArrayList<>()));
			portefeuilleRepository.save(new Portefeuille(2000.0, 10000.0, "EUR", 2L, new ArrayList<>()));
			portefeuilleRepository.save(new Portefeuille(500.0, 3000.0, "MAD", 3L, new ArrayList<>()));
		};
	}
}
