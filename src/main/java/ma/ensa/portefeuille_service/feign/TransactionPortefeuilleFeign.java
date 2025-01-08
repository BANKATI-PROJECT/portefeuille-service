package ma.ensa.portefeuille_service.feign;

// import ma.ensa.portefeuille_service.enti.Portefeuille;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ma.ensa.portefeuille_service.model.DepositRequest;

@FeignClient(name = "transaction-service" ,url = "http://localhost:8086")
public interface TransactionPortefeuilleFeign {

    @PostMapping("/deposit")
    public ResponseEntity<String> depositToPortefeuille(@RequestBody DepositRequest request);
}
