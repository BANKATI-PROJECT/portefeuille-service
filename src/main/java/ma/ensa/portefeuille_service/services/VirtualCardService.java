package ma.ensa.portefeuille_service.services;

import java.time.LocalDate;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.ensa.portefeuille_service.entities.Portefeuille;
import ma.ensa.portefeuille_service.entities.VirtualCard;
import ma.ensa.portefeuille_service.repositories.PortefeuilleRepository;
import ma.ensa.portefeuille_service.repositories.VirtualCardRepository;

@Service
public class VirtualCardService {

    @Autowired
    private VirtualCardRepository virtualCardRepository;

    @Autowired
    private PortefeuilleRepository portefeuilleRepository;

    public VirtualCard createVirtualCard(Long portefeuilleId, Double solde) {
        Portefeuille portefeuille = portefeuilleRepository.findById(portefeuilleId)
                .orElseThrow(() -> new RuntimeException("Portefeuille not found"));

        if (solde != null && solde > portefeuille.getSolde()) {
            throw new IllegalArgumentException("Insufficient funds in the portefeuille");
        }

        VirtualCard virtualCard = new VirtualCard();
        /*
         
        virtualCard.setNum(generateRandomNumber(16));
        virtualCard.setCvv(generateRandomNumber(3));
        virtualCard.setExpire(LocalDate.now().plusMonths(6)); // carte virtuelle katb9a sal7a 6 mois
        virtualCard.setSolde(solde != null ? solde : 0.0);
        virtualCard.setPortefeuille(portefeuille);
        */

        if (solde != null) {
            portefeuille.setSolde(portefeuille.getSolde() - solde);
            portefeuilleRepository.save(portefeuille);
        }

        return virtualCardRepository.save(virtualCard);
    }

    public VirtualCard useVirtualCard(Long cardId, Double transactionAmount) {
        VirtualCard virtualCard = virtualCardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("VirtualCard not found"));

        if (transactionAmount > virtualCard.getSolde()) {
            throw new IllegalArgumentException("Insufficient funds on the virtual card");
        }

        if (virtualCard.getExpire().isBefore(LocalDate.now())) {
            throw new RuntimeException("The card is expired");
        }

        virtualCard.setSolde(virtualCard.getSolde() - transactionAmount);
        return virtualCardRepository.save(virtualCard);
    }

    public void cancelVirtualCard(Long cardId) {
        VirtualCard virtualCard = virtualCardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("VirtualCard not found"));

        Portefeuille portefeuille = virtualCard.getPortefeuille();
        portefeuille.setSolde(portefeuille.getSolde() + virtualCard.getSolde());

        virtualCardRepository.delete(virtualCard);
        portefeuilleRepository.save(portefeuille);
    }

    public VirtualCard getVirtualCard(Long cardId) {
        return virtualCardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("VirtualCard not found"));
    }

    private String generateRandomNumber(int length) {
        Random random = new Random();
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < length; i++) {
            number.append(random.nextInt(10));
        }
        return number.toString();
    }
}
