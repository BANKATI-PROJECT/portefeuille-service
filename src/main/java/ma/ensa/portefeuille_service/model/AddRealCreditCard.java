package ma.ensa.portefeuille_service.model;

import lombok.Data;


public class AddRealCreditCard {
    private String safeToken;
    private String cardNum;
    private String cvv;
    private String expire;
    private String label;
    private double solde;

    public String getSafeToken() {
        return safeToken;
    }

    public void setSafeToken(String safeToken) {
        this.safeToken = safeToken;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }
}
