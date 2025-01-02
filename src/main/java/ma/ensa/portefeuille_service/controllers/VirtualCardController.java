package ma.ensa.portefeuille_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ma.ensa.portefeuille_service.entities.VirtualCard;
import ma.ensa.portefeuille_service.services.VirtualCardService;

@RestController
@RequestMapping("/api/virtualcards")
public class VirtualCardController {

    @Autowired
    private VirtualCardService virtualCardService;

    @PostMapping("/{portefeuilleId}")
    public ResponseEntity<?> createVirtualCard(@PathVariable Long portefeuilleId, @RequestParam(required = false) Double solde) {
        try {
            VirtualCard virtualCard = virtualCardService.createVirtualCard(portefeuilleId, solde);
            return ResponseEntity.ok(virtualCard);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @PostMapping("/use/{cardId}")
    public ResponseEntity<?> useVirtualCard(@PathVariable Long cardId, @RequestParam Double transactionAmount) {
        try {
            VirtualCard virtualCard = virtualCardService.useVirtualCard(cardId, transactionAmount);
            return ResponseEntity.ok(virtualCard);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<?> cancelVirtualCard(@PathVariable Long cardId) {
        try {
            virtualCardService.cancelVirtualCard(cardId);
            return ResponseEntity.ok("Virtual card canceled and funds returned to the portefeuille");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<?> getVirtualCard(@PathVariable Long cardId) {
        try {
            VirtualCard virtualCard = virtualCardService.getVirtualCard(cardId);
            return ResponseEntity.ok(virtualCard);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
