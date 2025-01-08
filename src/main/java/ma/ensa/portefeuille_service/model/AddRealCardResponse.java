package ma.ensa.portefeuille_service.model;


public class AddRealCardResponse {

    protected String safeToken;
    protected long cardId;

    /**
     * Gets the value of the safeToken property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSafeToken() {
        return safeToken;
    }

    /**
     * Sets the value of the safeToken property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSafeToken(String value) {
        this.safeToken = value;
    }

    /**
     * Gets the value of the cardId property.
     * 
     */
    public long getCardId() {
        return cardId;
    }

    /**
     * Sets the value of the cardId property.
     * 
     */
    public void setCardId(long value) {
        this.cardId = value;
    }

}
