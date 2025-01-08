package ma.ensa.portefeuille_service.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
// import lombok.Data;

@Entity
public class Portefeuille {

    @Id
    private String id;

    private Double solde;

    private String plafond;

    private String currency;
    @Column(nullable = false)
    private Long clientId;
    @OneToMany(mappedBy = "portefeuille", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VirtualCard> virtualCards = new ArrayList<>();
    
    private long defaultCardId;


    public long getDefaultCardId() {
        return defaultCardId;
    }


    public void setDefaultCardId(long defaultCardId) {
        this.defaultCardId = defaultCardId;
    }


    public Long getDefaultRealCard() {
        return defaultRealCard;
    }

    public void setDefaultRealCard(Long defaultRealCard) {
        this.defaultRealCard = defaultRealCard;
    }

    private Long defaultRealCard;

    public String getId() {
        return id;
    }


    public Double getSolde() {
        return solde;
    }

    public String getPlafond() {
        return plafond;
    }

    public String getCurrency() {
        return currency;
    }

    public List<VirtualCard> getVirtualCards() {
        return virtualCards;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSolde(Double solde) {
        this.solde = solde;
    }

    public void setPlafond(String plafond) {
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

    public Portefeuille(Double solde, String plafond, String currency, Long clientId, List<VirtualCard> virtualCards) {
        this.solde = solde;
        this.plafond = plafond;
        this.currency = currency;
        this.clientId = clientId;
        this.virtualCards = virtualCards;
    }

    public Portefeuille() {
    }
}
