package ma.ensa.portefeuille_service.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ma.ensa.portefeuille_service.entities.Portefeuille;
import ma.ensa.portefeuille_service.services.PortefeuilleService;

@RestController
@RequestMapping("/api/portefeuilles")
public class PortefeuilleController {

    @Autowired
    private PortefeuilleService portefeuilleService;

    @PostMapping
    public ResponseEntity<Portefeuille> createPortefeuille(@RequestBody Portefeuille portefeuille) {
        return ResponseEntity.ok(portefeuilleService.createPortefeuille(portefeuille));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Portefeuille> getPortefeuille(@PathVariable String id) {
        return ResponseEntity.ok(portefeuilleService.getPortefeuille(id));
    }

    @GetMapping
    public ResponseEntity<List<Portefeuille>> getAllPortefeuilles() {
        return ResponseEntity.ok(portefeuilleService.getAllPortefeuilles());
    }

    @PutMapping("currencyPlafond/{id}")
    public ResponseEntity<Portefeuille> updatePortefeuilleById(@PathVariable String id, @RequestParam(required = false) String currency, @RequestParam(required = false) Double plafond) {
        try {
            Portefeuille updatedPortefeuille = portefeuilleService.updatePortefeuilleById(id, currency, plafond);
            return ResponseEntity.ok(updatedPortefeuille);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/{id}/increment")
    public ResponseEntity<?> incrementSolde(@PathVariable String id, @RequestParam Double amount) {
        try {
            Portefeuille updatedPortefeuille = portefeuilleService.incrementSolde(id, amount);
            return ResponseEntity.ok(updatedPortefeuille);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    ///byIdClient
    @GetMapping("getByClientId/{clientId}")
    public ResponseEntity<Portefeuille> getPortefeuilleByClientId(@PathVariable Long clientId) {
        return ResponseEntity.ok(portefeuilleService.getPortefeuilleByClientId(clientId));
    }

    @PutMapping("updateByClientId/{clientId}")
    public ResponseEntity<Portefeuille> updatePortefeuilleByClientId(@PathVariable Long clientId, @RequestBody Portefeuille portefeuille) {
        Portefeuille updatedPortefeuille = portefeuilleService.updatePortefeuilleByClientId(clientId, portefeuille.getSolde());
        return ResponseEntity.ok(updatedPortefeuille);
    }


    //transaction part
    @PutMapping("/{id}")
    public ResponseEntity<Portefeuille> updatePortefeuille(@PathVariable("id") String id, @RequestBody Portefeuille portefeuille) {

        Portefeuille updatedPortefeuille = portefeuilleService.updatePortefeuille(id, portefeuille);

        if (updatedPortefeuille == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedPortefeuille);
    }
}
