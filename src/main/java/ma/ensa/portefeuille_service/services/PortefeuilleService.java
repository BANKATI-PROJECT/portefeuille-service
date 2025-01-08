package ma.ensa.portefeuille_service.services;

import java.util.List;
import java.util.UUID;

import com.mysql.cj.xdevapi.Schema;
import ma.ensa.portefeuille_service.requests.CreatePortfeuilleRequest;
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



    public Portefeuille getPortefeuille(String id) {
        return portefeuilleRepository.findById(id).orElseThrow(() -> new RuntimeException("Portefeuille not found"));
    }
    public Portefeuille getPortefeuilleByClientId(Long clientId) {
        return portefeuilleRepository.findByClientId(clientId)
                .orElseThrow(() -> new RuntimeException("Portefeuille introuvable pour le client ID : " + clientId));
    }

    public List<Portefeuille> getAllPortefeuilles() {
        return portefeuilleRepository.findAll();
    }

    public Portefeuille updatePortefeuilleById(String id, String currency, String plafond) {
        Portefeuille portefeuille = portefeuilleRepository.findById(id).orElseThrow(() -> new RuntimeException("Portefeuille not found"));
        if (currency != null) {
            portefeuille.setCurrency(currency);
        }
        if (plafond != null) {
            portefeuille.setPlafond(plafond);
        }
        return portefeuilleRepository.save(portefeuille);
    }


    public Portefeuille incrementSolde(String id, Double amount) {
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
    public Portefeuille updatePortefeuille(String id, Portefeuille portefeuille) {
        return portefeuilleRepository.findById(id)
                .map(existingPortefeuille -> {
                    existingPortefeuille.setSolde(portefeuille.getSolde()); return portefeuilleRepository.save(existingPortefeuille);
                })
                .orElse(null);
    }

    public Portefeuille createPortefeuille(CreatePortfeuilleRequest createPortfeuilleRequest){
        Portefeuille portefeuille=new Portefeuille();
        portefeuille.setId(UUID.randomUUID().toString());
        portefeuille.setSolde(0.0);
        portefeuille.setClientId(createPortfeuilleRequest.getClientId());
        portefeuille.setCurrency(createPortfeuilleRequest.getCurrency());
        portefeuille.setPlafond(createPortfeuilleRequest.getPlafond());
        return portefeuilleRepository.save(portefeuille);
    }
}
