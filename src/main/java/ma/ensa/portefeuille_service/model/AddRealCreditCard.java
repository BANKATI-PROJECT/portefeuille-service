package ma.ensa.portefeuille_service.model;

import lombok.Data;

@Data
public class AddRealCreditCard {
    private String safeToken;
    private String cardNum;
    private String cvv;
    private String expire;
    private String label;
    private double solde;
}
