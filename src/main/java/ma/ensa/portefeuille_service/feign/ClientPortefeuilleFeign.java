package ma.ensa.portefeuille_service.feign;

// import ma.ensa.portefeuille_service.enti.Portefeuille;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "accountManagement-service")
public interface ClientPortefeuilleFeign {

    @PutMapping("/api/client/{id}/saveToken")
    void updateClientSaveTokenById(@PathVariable Long id, @RequestBody String saveToken);

    @GetMapping("/api/client/getSaveToken//{id}")
    public ResponseEntity<String> getSavetokenByClientId(@PathVariable Long id);
}
