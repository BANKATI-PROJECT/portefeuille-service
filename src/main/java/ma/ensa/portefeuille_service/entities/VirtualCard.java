package ma.ensa.portefeuille_service.entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class VirtualCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String num;

    private String cvv;

    private LocalDate expire;

    private Double solde;

    @ManyToOne
    @JoinColumn(name = "portefeuille_id")
    @JsonIgnore
    private Portefeuille portefeuille;

    // Getters and Setters
}