package ma.ensa.portefeuille_service.model;

import java.util.Date;

public class RealCardCMI {

    protected long id;
    protected String num;
    // protected String cvv;
    protected Date expire;
    protected String label;
    // protected double solde;

    /**
     * Gets the value of the id property.
     * 
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(long value) {
        this.id = value;
    }

    /**
     * Gets the value of the num property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNum() {
        return num;
    }

    /**
     * Sets the value of the num property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNum(String value) {
        this.num = value;
    }

    /**
     * Gets the value of the expire property.
     * 
     * @return
     *     possible object is
     *     {@link Date }
     *     
     */
    public Date getExpire() {
        return expire;
    }

    /**
     * Sets the value of the expire property.
     * 
     * @param value
     *     allowed object is
     *     {@link Date }
     *     
     */
    public void setExpire(Date value) {
        this.expire = value;
    }

    /**
     * Gets the value of the label property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the value of the label property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabel(String value) {
        this.label = value;
    }

}
