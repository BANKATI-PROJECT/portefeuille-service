package ma.ensa.portefeuille_service.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Portefeuille {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double solde;

    private Double plafond;

    private String currency;

    @OneToMany(mappedBy = "portefeuille", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VirtualCard> virtualCards = new ArrayList<>();

    // Getters and Setters
}
