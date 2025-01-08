package ma.ensa.portefeuille_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.ensa.portefeuille_service.entities.Portefeuille;

import java.util.Optional;

@Repository
public interface PortefeuilleRepository extends JpaRepository<Portefeuille, String> {
    Optional<Portefeuille> findByClientId(Long clientId);
}
