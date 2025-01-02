package ma.ensa.portefeuille_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.ensa.portefeuille_service.entities.Portefeuille;

@Repository
public interface PortefeuilleRepository extends JpaRepository<Portefeuille, Long> {
}
