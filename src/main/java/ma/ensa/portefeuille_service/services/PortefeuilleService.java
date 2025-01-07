package ma.ensa.portefeuille_service.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.ensa.portefeuille_service.entities.Portefeuille;
import ma.ensa.portefeuille_service.feign.TransactionPortefeuilleFeign;
import ma.ensa.portefeuille_service.model.DepositRequest;
import ma.ensa.portefeuille_service.repositories.PortefeuilleRepository;

@Service
public class PortefeuilleService {

    @Autowired
    private PortefeuilleRepository portefeuilleRepository;
    @Autowired TransactionPortefeuilleFeign transactionFeign;

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
        portefeuille.setSolde(portefeuille.getSolde() + amount);

        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setAmount(amount);
        depositRequest.setClientId(portefeuille.getClientId());
        depositRequest.setSolde(portefeuille.getSolde());
        
        transactionFeign.depositToPortefeuille(depositRequest);
        return portefeuilleRepository.save(portefeuille);
    }


    //////////// by clientId

    public Portefeuille updatePortefeuilleByClientId(Long clientId, Double newSolde) {
        Portefeuille portefeuille = getPortefeuilleByClientId(clientId);
        portefeuille.setSolde(newSolde);
        return portefeuilleRepository.save(portefeuille);
    }

    ////transaction part
    public Portefeuille updatePortefeuille(Long id, Portefeuille portefeuille) {
        return portefeuilleRepository.findById(id)
                .map(existingPortefeuille -> {
                    existingPortefeuille.setSolde(portefeuille.getSolde()); return portefeuilleRepository.save(existingPortefeuille);
                })
                .orElse(null);
    }
}
