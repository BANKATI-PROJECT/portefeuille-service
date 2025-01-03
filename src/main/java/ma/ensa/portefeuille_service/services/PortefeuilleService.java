package ma.ensa.portefeuille_service.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.ensa.portefeuille_service.entities.Portefeuille;
import ma.ensa.portefeuille_service.repositories.PortefeuilleRepository;

@Service
public class PortefeuilleService {

    @Autowired
    private PortefeuilleRepository portefeuilleRepository;

    public Portefeuille createPortefeuille(Portefeuille portefeuille) {
        return portefeuilleRepository.save(portefeuille);
    }

    public Portefeuille getPortefeuille(Long id) {
        return portefeuilleRepository.findById(id).orElseThrow(() -> new RuntimeException("Portefeuille not found"));
    }
    public Portefeuille getPortefeuilleByClientId(Long clientId) {
        return portefeuilleRepository.findByClientId(clientId)
                .orElseThrow(() -> new RuntimeException("Portefeuille introuvable pour le client ID : " + clientId));
    }

    public List<Portefeuille> getAllPortefeuilles() {
        return portefeuilleRepository.findAll();
    }

    public Portefeuille updatePortefeuilleById(Long id, String currency, Double plafond) {
        Portefeuille portefeuille = portefeuilleRepository.findById(id).orElseThrow(() -> new RuntimeException("Portefeuille not found"));
        if (currency != null) {
            portefeuille.setCurrency(currency);
        }
        if (plafond != null) {
            portefeuille.setPlafond(plafond);
        }
        return portefeuilleRepository.save(portefeuille);
    }


    public Portefeuille incrementSolde(Long id, Double amount) {
        Portefeuille portefeuille = portefeuilleRepository.findById(id).orElseThrow(() -> new RuntimeException("Portefeuille not found"));

        // Simulating a confirmation from a remote service
        boolean isConfirmed = true; // Replace with actual logic later

        if (!isConfirmed) {
            throw new RuntimeException("Failed to confirm the increment with the remote service");
        }

        portefeuille.setSolde(portefeuille.getSolde() + amount);
        return portefeuilleRepository.save(portefeuille);
    }


    //////////// by clientId

    public Portefeuille updatePortefeuille(Long clientId, Double newSolde) {
        Portefeuille portefeuille = getPortefeuilleByClientId(clientId);
        portefeuille.setSolde(newSolde);
        return portefeuilleRepository.save(portefeuille);
    }
}
