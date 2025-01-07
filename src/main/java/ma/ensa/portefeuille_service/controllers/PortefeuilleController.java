package ma.ensa.portefeuille_service.controllers;

import java.util.List;

// import javax.sound.sampled.Port;

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

import jakarta.xml.soap.SOAPMessage;
import ma.ensa.portefeuille_service.entities.Portefeuille;
import ma.ensa.portefeuille_service.feign.ClientPortefeuilleFeign;
import ma.ensa.portefeuille_service.model.AddRealCardResponse;
import ma.ensa.portefeuille_service.model.AddRealCreditCard;
// import ma.ensa.portefeuille_service.model.Client;
import ma.ensa.portefeuille_service.model.MessageResponse;
import ma.ensa.portefeuille_service.model.RealCardCMI;
import ma.ensa.portefeuille_service.services.PortefeuilleService;
import ma.ensa.portefeuille_service.util.SoapHandler;

@RestController
@RequestMapping("/api/portefeuilles")
public class PortefeuilleController {

    @Autowired
    private PortefeuilleService portefeuilleService;

    @Autowired
    private ClientPortefeuilleFeign clientPortefeuilleFeign;

    @PostMapping
    public ResponseEntity<Portefeuille> createPortefeuille(@RequestBody Portefeuille portefeuille) {
        return ResponseEntity.ok(portefeuilleService.createPortefeuille(portefeuille));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Portefeuille> getPortefeuille(@PathVariable Long id) {
        return ResponseEntity.ok(portefeuilleService.getPortefeuille(id));
    }

    @GetMapping
    public ResponseEntity<List<Portefeuille>> getAllPortefeuilles() {
        return ResponseEntity.ok(portefeuilleService.getAllPortefeuilles());
    }

    @PutMapping("currencyPlafond/{id}")
    public ResponseEntity<Portefeuille> updatePortefeuilleById(@PathVariable Long id, @RequestParam(required = false) String currency, @RequestParam(required = false) Double plafond) {
        try {
            Portefeuille updatedPortefeuille = portefeuilleService.updatePortefeuilleById(id, currency, plafond);
            return ResponseEntity.ok(updatedPortefeuille);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/{id}/increment")
    public ResponseEntity<?> incrementSolde(@PathVariable Long id, @RequestParam Double amount) {
        try {
            Portefeuille portefeuille = portefeuilleService.getPortefeuille(id);
            if(portefeuille==null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("portefeuille doesn't exist");
            }
            String savetoken = clientPortefeuilleFeign.getSavetokenByClientId(portefeuille.getClientId()).getBody();

            SOAPMessage soapRequest;
            soapRequest = SoapHandler.createTransactionRequest(savetoken, portefeuille.getDefaultCardId(), amount);
            SOAPMessage soapResponse = SoapHandler.sendSoapRequest("https://cmi-service-production.up.railway.app/ws/requests_responses", soapRequest);
            MessageResponse r = SoapHandler.parseCreateTransactionResponse(soapResponse);

            if(r.getMessage()=="Transaction successful"){
                portefeuille = portefeuilleService.incrementSolde(id, amount);
                return ResponseEntity.ok(portefeuille);
            }
            return null;
        } catch (Exception e) {
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
    public ResponseEntity<Portefeuille> updatePortefeuille(@PathVariable("id") Long id, @RequestBody Portefeuille portefeuille) {

        Portefeuille updatedPortefeuille = portefeuilleService.updatePortefeuille(id, portefeuille);

        if (updatedPortefeuille == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedPortefeuille);
    }

    @PutMapping("/{portefeuilleId}/defaultcard/{id}")
    public ResponseEntity<Portefeuille> setDefaultCard(@PathVariable("portefeuilleId") Long portefeuilleId,@PathVariable("id") long id) {

        Portefeuille updatedPortefeuille = portefeuilleService.getPortefeuille(portefeuilleId);
        
        if (updatedPortefeuille == null) {
            return ResponseEntity.notFound().build();
        }
        
        updatedPortefeuille.setDefaultCardId(id);
        updatedPortefeuille = portefeuilleService.updatePortefeuille(portefeuilleId, updatedPortefeuille);
        return ResponseEntity.ok(updatedPortefeuille);
    }

    @PostMapping("/addRealCard/{id}")
    public ResponseEntity<?> addRealCard(@PathVariable Long id,@RequestBody AddRealCreditCard realCreditCard) {
        SOAPMessage soapRequest;
        try {
            soapRequest = SoapHandler.buildAddRealCardRequest(
                realCreditCard.getSafeToken(),
                realCreditCard.getCardNum(),
                realCreditCard.getCvv(),
                realCreditCard.getExpire(),
                realCreditCard.getLabel());
            SOAPMessage soapResponse = SoapHandler.sendSoapRequest("https://cmi-service-production.up.railway.app/ws/requests_responses", soapRequest);
            AddRealCardResponse r = SoapHandler.parsebuildAddRealCardResponse(soapResponse);

            Portefeuille portefeuille = portefeuilleService.getPortefeuille(id);
            portefeuille.setDefaultCardId(r.getCardId());
            portefeuilleService.updatePortefeuille(id, portefeuille);

            clientPortefeuilleFeign.updateClientSaveTokenById(portefeuille.getClientId(), r.getSafeToken());
            return ResponseEntity.ok("Real Card added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }   
    }

    @GetMapping("/getRealCards/{id}")
    public ResponseEntity<?> getRealCards(@PathVariable Long id) {
        SOAPMessage soapRequest;
        try {
            Portefeuille portefeuille = portefeuilleService.getPortefeuille(id);
            if(portefeuille==null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("portefeuille doesn't exist");
            }
            String savetoken = clientPortefeuilleFeign.getSavetokenByClientId(portefeuille.getClientId()).getBody();

            soapRequest = SoapHandler.createGetAllCardsRequest(savetoken);
            SOAPMessage soapResponse = SoapHandler.sendSoapRequest("https://cmi-service-production.up.railway.app/ws/requests_responses", soapRequest);
            List<RealCardCMI> r = SoapHandler.parseGetAllCardsResponse(soapResponse);

            return ResponseEntity.ok(r);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }   
    }
}
