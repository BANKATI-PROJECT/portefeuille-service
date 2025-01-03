package ma.ensa.portefeuille_service.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
public class Portefeuille {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double solde;

    private Double plafond;

    private String currency;
    @Column(nullable = false)
    private Long clientId;
    @OneToMany(mappedBy = "portefeuille", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VirtualCard> virtualCards = new ArrayList<>();

    public Long getId() {
        return id;
    }


    public Double getSolde() {
        return solde;
    }

    public Double getPlafond() {
        return plafond;
    }

    public String getCurrency() {
        return currency;
    }

    public List<VirtualCard> getVirtualCards() {
        return virtualCards;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSolde(Double solde) {
        this.solde = solde;
    }

    public void setPlafond(Double plafond) {
        this.plafond = plafond;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setVirtualCards(List<VirtualCard> virtualCards) {
        this.virtualCards = virtualCards;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Portefeuille(Double solde, Double plafond, String currency, Long clientId, List<VirtualCard> virtualCards) {
        this.solde = solde;
        this.plafond = plafond;
        this.currency = currency;
        this.clientId = clientId;
        this.virtualCards = virtualCards;
    }

    public Portefeuille() {
    }
}
