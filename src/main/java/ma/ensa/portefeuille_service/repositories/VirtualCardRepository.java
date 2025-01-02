package ma.ensa.portefeuille_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.ensa.portefeuille_service.entities.VirtualCard;

@Repository
public interface VirtualCardRepository extends JpaRepository<VirtualCard, Long> {
}